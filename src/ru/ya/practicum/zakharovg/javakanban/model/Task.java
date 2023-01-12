package ru.ya.practicum.zakharovg.javakanban.model;

// Задача
public class Task extends BaseTask {

    public Task(String title, String descriptions) {
        this(title, descriptions, null, 0);
    }

    private Task(String title, String descriptions, String startTime, long minuteDuration) {
        super(title, descriptions);
        setDurationMinute(minuteDuration);
        if (startTime != null) {
            setStartTime(startTime);
        }
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
