package model;

import java.time.Instant;
import java.util.HashSet;

// Эпик
public class Epic extends BaseTask {

    private HashSet<Integer> subtaskIds;

    private Instant endTime;

    public Epic(String title, String description) {
        super(title, description);
        subtaskIds = new HashSet<>();
        setStatus(Status.NEW);
    }

    public HashSet<Integer> getSubtaskIds() {
        return subtaskIds;
    }

    public void setEndTime(Instant endTime) {
        this.endTime = endTime;
    }

    @Override
    public Instant getEndTime() {
        return endTime;
    }


    public void add(Subtask subtask) {
        if (!subtaskIds.contains(subtask.getId())) {
            // Устанавливаем у Подзадачи Id родителя
            subtask.setEpicId(getId());
            // добавляем подзадачу к эпику
            subtaskIds.add(subtask.getId());
        }
    }

    // метод только для загрузки данных из файла
    public void add(Integer id) {
        if (!subtaskIds.contains(id)) {
            subtaskIds.add(id);
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
