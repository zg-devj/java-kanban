package model;

import util.Identifier;

// Подзадача
public class Subtask extends Base {
    private static final Identifier IDENTIFIER = new Identifier();
    // Id эпика - это parentId класса Base

    public Subtask(String title, String descriptions) {
        super(title, descriptions);
        setId(IDENTIFIER.next());
        setStatus(Status.NEW);
    }

    public Subtask(String title) {
        super(title);
        setId(IDENTIFIER.next());
        setStatus(Status.NEW);
    }

    @Override
    public String toString() {
        return "\nSubtask{" +
                "id=" + id +
                ", parentId=" + getParentId() +
                ", title='" + title + '\'' +
                ", description='" + descriptions + '\'' +
                ", status=" + status +
                "}";
    }
}
