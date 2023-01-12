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
        this(title, descriptions, (String) null, 0);
    }

    public Subtask(String title, String descriptions, String startTime) {
        this(title, descriptions, startTime, 0);
    }

    public Subtask(String title, String descriptions, long minuteDuration) {
        this(title, descriptions, (String) null, minuteDuration);
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
        return "Subtask{" +
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
