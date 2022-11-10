package model;

import java.util.HashSet;

// Эпик
public class Epic extends Base {
    private HashSet<Integer> subtaskIds;

    public Epic(String title, String description) {
        super(title, description);
        subtaskIds = new HashSet<>();
        setStatus(Status.NEW);
    }

    public Epic(String title) {
        this(title, null);
    }

    public HashSet<Integer> getSubtaskIds() {
        return subtaskIds;
    }

    public void add(Subtask subtask) {
        if (!subtaskIds.contains(subtask.getId())) {
            // Устанавливаем у Подзадачи Id родителя
            subtask.setEpicId(getId());
            // добавляем подзадачу к эпику
            subtaskIds.add(subtask.getId());
        }
    }

    @Override
    public String toString() {
        return "Epic{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", subtaskIds=" + subtaskIds +
                ", status=" + status +
                '}';
    }
}
