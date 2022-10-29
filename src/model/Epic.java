package model;

import util.Identifier;

import java.util.HashSet;

// Эпик
public class Epic extends Base {
    private static final Identifier IDENTIFIER = new Identifier();
    private HashSet<Integer> subtaskIds;

    public Epic(String title) {
        super(title);
        setId(IDENTIFIER.next());
        setStatus(Status.NEW);
        subtaskIds = new HashSet<>();
    }

    public HashSet<Integer> getSubtaskIds() {
        return subtaskIds;
    }

    public void add(Subtask subtask) {
        if(!subtaskIds.contains(subtask.getId())) {
            // Устанавливаем у Подзадачи Id родителя
            subtask.setParentId(getId());
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
