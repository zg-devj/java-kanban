package model;

// Мне кажется или Subtask и Task это один и та-же сущность, если
// вынести parentId в Base, то
// parentId = 0 -> Task
// parentId = epicId -> Subtask
// не нарушает ли разделение этих сущностей принципам DRY

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

// Подзадача
public class Subtask extends BaseTask {
    private int epicId;

    public Subtask(int epicId, String title, String descriptions) {
        this(epicId,title,descriptions,null,0);
    }

    public Subtask(int epicId, String title, String descriptions, String startTime, long minuteDuration) {
        super(title, descriptions);
        this.epicId = epicId;
        if (minuteDuration != 0) {
            setDuration(minuteDuration);
        }
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
    public String toString() {
        return "Subtask{" +
                "id=" + id +
                ", epicId=" + epicId +
                ", title='" + title + '\'' +
                ", description='" + descriptions + '\'' +
                ", status=" + status +
                "}";
    }
}
