package model;

import util.Identifier;

import java.util.HashSet;

// Подзадача
public class Subtask extends Base {
    private static final Identifier IDENTIFIER = new Identifier();
    private HashSet<Integer> taskIds;

    public Subtask(String title) {
        super(title);
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

    @Override
    public String toString() {
        return "\nSubtask{" +
                "id=" + id +
                ", parentId=" + getParentId() +
                ", title='" + title + '\'' +
                ", taskIds=" + taskIds +
                ", status=" + status +
                "}";
    }
}
