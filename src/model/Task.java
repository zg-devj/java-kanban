package model;

import util.Identifier;

// Задача
public class Task extends Base {
    private static final Identifier IDENTIFIER = new Identifier();

    public Task(String title, String descriptions) {
        super(title, descriptions);
        setId(IDENTIFIER.next());
        setStatus(Status.NEW);
    }

    public Task(String title) {
        this(title, null);
    }

    @Override
    public String toString() {
        return "\nTask{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", descriptions='" + descriptions + '\'' +
                ", status=" + status +
                '}';
    }
}
