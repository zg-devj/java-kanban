package model;

import util.Identifier;

// Задача
public class Task extends Base {
    private static final Identifier IDENTIFIER = new Identifier();

    public Task(String title) {
        super(title);
        setId(IDENTIFIER.next());
        setStatus(Status.NEW);
    }

    @Override
    public String toString() {
        return "\nTask{" +
                "id=" + id +
                ", parentId=" + getParentId() +
                ", title='" + title + '\'' +
                ", descriptions=" + descriptions +
                ", status=" + status +
                '}';
    }
}
