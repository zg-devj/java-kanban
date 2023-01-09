package model;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

// Задача
public class Task extends BaseTask {

    public Task(String title, String descriptions) {
        this(title, descriptions, null, 0);
    }

    public Task(String title, String descriptions, String startTime, long minuteDuration) {
        super(title, descriptions);
        if (minuteDuration != 0) {
            setDuration(minuteDuration);
        }
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
                '}';
    }
}
