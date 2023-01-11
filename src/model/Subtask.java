package model;

// Мне кажется или Subtask и Task это один и та-же сущность, если
// вынести parentId в Base, то
// parentId = 0 -> Task
// parentId = epicId -> Subtask
// не нарушает ли разделение этих сущностей принципам DRY

import util.DateTimeConverter;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

// Подзадача
public class Subtask extends BaseTask {
    private int epicId;

    public Subtask(int epicId, String title, String descriptions) {
        this(epicId,title,descriptions,null,0);
    }

    private Subtask(int epicId, String title, String descriptions, String startTime, long minuteDuration) {
        super(title, descriptions);
        this.epicId = epicId;
        setDurationMinute(minuteDuration);
        if (startTime != null) {
            setStartTime(startTime);
        }
        setStatus(Status.NEW);
    }

    public int getEpicId() {
        return epicId;
    }

    public void setEpicId(int epicId) {
        this.epicId = epicId;
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
