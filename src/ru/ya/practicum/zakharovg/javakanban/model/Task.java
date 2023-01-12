package ru.ya.practicum.zakharovg.javakanban.model;

// Задача
public class Task extends BaseTask {

    public Task(String title, String descriptions) {
        this(title, descriptions, null, 0);
    }

    public Task(String title, String descriptions, String startTime) {
        this(title, descriptions, startTime, 0);
    }

    public Task(String title, String descriptions, long minuteDuration) {
        this(title, descriptions, null, minuteDuration);
    }

    public Task(String title, String descriptions, String startTime, long minuteDuration) {
        super(title, descriptions, startTime, minuteDuration);
        setStatus(Status.NEW);
    }

    @Override
    public String toString() {
        return "Task{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", descriptions='" + descriptions + '\'' +
                ", status=" + status +
                ", startTime=" + startTime +
                ", duration=" + getDurationMinute() +
                '}';
    }
}
