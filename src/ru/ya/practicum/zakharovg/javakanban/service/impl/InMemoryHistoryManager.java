package ru.ya.practicum.zakharovg.javakanban.service.impl;

import ru.ya.practicum.zakharovg.javakanban.model.BaseTask;
import ru.ya.practicum.zakharovg.javakanban.service.HistoryManager;
import ru.ya.practicum.zakharovg.javakanban.util.Node;

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
        // добавляем в конец списка
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

        // Добавляет задачу в конец списка.
        public void linkLast(T element) {
            removeNode(element.getId());
            final Node<T> oldTail = tail;
            final Node<T> newNode = new Node<>(oldTail, element, null);
            tail = newNode;
            if (oldTail == null) {
                head = newNode;
            } else {
                oldTail.setNext(newNode);
            }
            nodeMap.put(element.getId(), newNode);
            size++;
        }

        // возвращает задачи в виде ArrayList от
        // первого просмотра до последнего
        public List<T> getTasks() {
            List<T> list = new ArrayList<>();
            if (size > 0) {
                Node<T> current = head;
                while (current != null) {
                    list.add(current.getData());
                    current = current.getNext();
                }
            }
            return list;
        }

        // удаляем узел
        public void removeNode(int id) {
            Node<T> node = nodeMap.get(id);
            if (node != null) {
                Node<T> prevNode = node.getPrev();
                Node<T> nextNode = node.getNext();
                if (prevNode != null && nextNode != null) {
                    // если элемент находится между двумя
                    nextNode.setPrev(prevNode);
                    prevNode.setNext(nextNode);
                } else if (prevNode == null && nextNode != null) {
                    nextNode.setPrev(null);
                    head = nextNode;
                } else if (nextNode == null && prevNode != null) {
                    prevNode.setNext(null);
                    tail = prevNode;
                } else {
                    // если удаляем последний элемент
                    head = null;
                    tail = null;
                }
                nodeMap.remove(id);
                size--;
            }
        }
    }
}
