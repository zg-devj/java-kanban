package model;

import java.time.Instant;

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
    public void setStartTime(Instant instant) {
        super.setStartTime(instant);
        onValid.test(this);
    }

    @Override
    public void setStartTime(String dateTime) {
        super.setStartTime(dateTime);
        onValid.test(this);
    }

    @Override
    public void setDurationMinute(long minute) {
        super.setDurationMinute(minute);
        onValid.test(this);
    }

    // TODO: 11.01.2023 Remove \n
    @Override
    public String toString() {
        return "\nTask{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", descriptions='" + descriptions + '\'' +
                ", status=" + status +
                ", startTime=" + startTime +
                ", duration=" + getDurationMinute() +
                '}';
    }
}
