package ru.ya.practicum.zakharovg.javakanban.model;

import java.time.Instant;

// Задача
public class Task extends BaseTask {

    public Task(String title, String descriptions) {
        super(title, descriptions);
        setStatus(Status.NEW);
        this.type = "Task";
    }

    public Task(String title, String descriptions, String startTime) {
        super(title, descriptions, startTime);
        setStatus(Status.NEW);
        this.type = "Task";
    }

    public Task(String title, String descriptions, Instant startTime) {
        super(title, descriptions, startTime);
        setStatus(Status.NEW);
        this.type = "Task";
    }

    public Task(String title, String descriptions, long minuteDuration) {
        super(title, descriptions, (Instant) null, minuteDuration);
        setStatus(Status.NEW);
        this.type = "Task";
    }

    public Task(String title, String descriptions, String startTime, long minuteDuration) {
        super(title, descriptions, startTime, minuteDuration);
        setStatus(Status.NEW);
        this.type = "Task";
    }

    public Task(String title, String descriptions, Instant startTime, long minuteDuration) {
        super(title, descriptions, startTime, minuteDuration);
        setStatus(Status.NEW);
        this.type = "Task";
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
