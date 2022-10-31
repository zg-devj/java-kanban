package model;

// Мне кажется или Subtask и Task это один и та-же сущность, если
// вынести parentId в Base, то
// parentId = 0 -> Task
// parentId = epicId -> Subtask
// не нарушает ли разделение этих сущностей принципам DRY

// Подзадача
public class Subtask extends Base {
    private int epicId;

    public Subtask(String title, int epicId, String descriptions) {
        super(title, descriptions);
        setStatus(Status.NEW);
        this.epicId = epicId;
    }

    public Subtask(String title, int epicId) {
        this(title, epicId, null);
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
