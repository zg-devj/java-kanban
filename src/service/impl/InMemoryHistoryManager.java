package service.impl;

import model.BaseTask;
import service.HistoryManager;
import util.Node;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryHistoryManager implements HistoryManager {
    // список истории
    CustomLinkedList<BaseTask> list;

    public InMemoryHistoryManager() {
        this.list = new CustomLinkedList<>();
    }

    @Override
    public void add(BaseTask task) {
        list.removeNode(task.getId());
        list.linkLast(task);
    }

    @Override
    public void remove(int id) {
        list.removeNode(id);
    }

    @Override
    public List<BaseTask> getHistory() {
        return list.getTasks();
    }

    class CustomLinkedList<T extends BaseTask> {
        private int size = 0;
        private Node<T> head;
        private Node<T> tail;

        private Map<Integer, Node<T>> nodeMap = new HashMap<>();

        // добавляет задачу в конец списка
        public void linkLast(T element) {
            final Node<T> oldTail = tail;
            final Node<T> newTail = new Node<>(oldTail, element, null);
            tail = newTail;
            if (head == null) {
                head = newTail;
            }
            if (oldTail == null) {
                tail = newTail;
            } else {
                oldTail.next = newTail;
            }
            nodeMap.put(element.getId(), newTail);
            size++;
        }

        // возвращает задачи в виде ArrayList
        public List<T> getTasks() {
            List<T> list = new ArrayList<>();
            if (size > 0) {
                Node<T> cur = tail;
                for (int i = 0; i < size; i++) {
                    list.add(cur.data);
                    cur = cur.prev;
                }
            }
            return list;
        }

        // удаляем узел
        public void removeNode(int id) {
            Node<T> node = nodeMap.get(id);
            if (node != null) {
                Node<T> prevNode = node.prev;
                Node<T> nextNode = node.next;
                if (prevNode != null && nextNode != null) {
                    // если элемент находится между двумя
                    nextNode.prev = prevNode;
                    prevNode.next = nextNode;
                } else if (prevNode == null && nextNode != null) {
                    nextNode.prev = null;
                    head = nextNode;
                } else if (nextNode == null && prevNode != null) {
                    prevNode.next = null;
                    tail = prevNode;
                } else {
                    // если удаляем последний элемент
                    head = null;
                    tail = null;
                    node.data = null;
                    node.next = null;
                    node.prev = null;
                }
                nodeMap.remove(id);
                size--;
            }
        }
    }
}
