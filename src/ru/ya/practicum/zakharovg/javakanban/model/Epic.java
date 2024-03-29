package ru.ya.practicum.zakharovg.javakanban.model;

import java.time.Instant;
import java.util.HashSet;

// Эпик
public class Epic extends BaseTask {

    private HashSet<Integer> subtaskIds = new HashSet<>();

    private Instant endTime;

    public Epic() {
        setStatus(Status.NEW);
        setType("Epic");
    }

    public Epic(String title, String description) {
        super(title, description);
        setStatus(Status.NEW);
        setType("Epic");
    }

    public HashSet<Integer> getSubtaskIds() {
        return subtaskIds;
    }

    public void setEndTime(Instant instant) {
        this.endTime = instant;
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
