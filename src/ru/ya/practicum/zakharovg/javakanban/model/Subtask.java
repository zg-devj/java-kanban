package ru.ya.practicum.zakharovg.javakanban.model;

// Мне кажется или Subtask и Task это один и та-же сущность, если
// вынести parentId в Base, то
// parentId = 0 -> Task
// parentId = epicId -> Subtask
// не нарушает ли разделение этих сущностей принципам DRY

import ru.ya.practicum.zakharovg.javakanban.util.DateTimeConverter;

import java.time.Instant;

// Подзадача
public class Subtask extends BaseTask {
    private int epicId;

    public Subtask(String title, String descriptions) {
        super(title, descriptions);
        setStatus(Status.NEW);
    }

    public Subtask(String title, String descriptions, String startTime) {
        super(title, descriptions, startTime);
        setStatus(Status.NEW);
    }

    public Subtask(String title, String descriptions, Instant startTime) {
        super(title, descriptions, startTime);
        setStatus(Status.NEW);
    }

    public Subtask(String title, String descriptions, long minuteDuration) {
        super(title, descriptions, (Instant) null, minuteDuration);
        setStatus(Status.NEW);
    }

    public Subtask(String title, String descriptions, String startTime, long minuteDuration) {
        super(title, descriptions, startTime, minuteDuration);
        setStatus(Status.NEW);
    }

    public Subtask(String title, String descriptions, Instant startTime, long minuteDuration) {
        super(title, descriptions, startTime, minuteDuration);
        setStatus(Status.NEW);
    }

    public int getEpicId() {
        return epicId;
    }

    public void setEpicId(int epicId) {
        this.epicId = epicId;
    }

    @Override
    public String toString() {
        // TODO: 14.01.2023 remove
        return "\nSubtask{" +
                "id=" + id +
                ", epicId=" + epicId +
                ", title='" + title + '\'' +
                ", description='" + descriptions + '\'' +
                ", status=" + status +
                ", startTime=" + DateTimeConverter.fromInstantToString(startTime) +
                ", duration=" + getDurationMinute() +
                "}";
    }
}
