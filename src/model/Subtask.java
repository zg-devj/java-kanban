package model;

import util.Identifier;

import java.util.HashSet;

// Подзадача
public class Subtask extends Base {
    private static final Identifier IDENTIFIER = new Identifier();
    private int parentId;
    private HashSet<Integer> taskIds;

    public Subtask(String title) {
        super(title);
        this.parentId = 0;
        setId(IDENTIFIER.next());
        setStatus(Status.NEW);
        taskIds = new HashSet<>();
    }

    public HashSet<Integer> getTaskIds() {
        return taskIds;
    }

    // Добавляем id Задачи
    public void add(Task task) {
        if(!taskIds.contains(task.getId())) {
            // Устанавливаем у Задачи Id родителя
            task.setParentId(getId());
            // добавляем задачу к подзадачам
            taskIds.add(task.getId());
        }
    }

    // Вернуть Id родителя
    public int getParentId() {
        return parentId;
    }

    // Установить Id родителя
    public void setParentId(int parentId) {
        this.parentId = parentId;
    }

    @Override
    public String toString() {
        return "Subtask{" +
                "id=" + id +
                ", parentId=" + parentId +
                ", title='" + title + '\'' +
                ", taskIds=" + taskIds +
                ", status=" + status +
                '}';
    }
}
