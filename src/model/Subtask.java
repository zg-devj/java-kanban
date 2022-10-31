package model;

import util.Identifier;

// Подзадача
public class Subtask extends Base {
    private static final Identifier IDENTIFIER = new Identifier();
    private int epicId;

    public Subtask(String title, int epicId, String descriptions) {
        super(title, descriptions);
        setId(IDENTIFIER.next());
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
        return "\nSubtask{" +
                "id=" + id +
                ", epicId=" + epicId +
                ", title='" + title + '\'' +
                ", description='" + descriptions + '\'' +
                ", status=" + status +
                "}";
    }
}
