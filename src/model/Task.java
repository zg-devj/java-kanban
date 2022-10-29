package model;

import util.Identifier;

// Задача
public class Task extends Base {
    private static final Identifier IDENTIFIER = new Identifier();

    private int parentId;

    public Task(String title) {
        super(title);
        this.parentId = 0;
        setId(IDENTIFIER.next());
        setStatus(Status.NEW);
    }

    // Вернуть Id родителя
    public int getParentId() {
        return parentId;
    }

    // Установить Id родителя
    public void setParentId(int parentId) {
        this.parentId = parentId;
    }

    @Override
    public String toString() {
        return "Task{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", descriptions=" + descriptions +
                ", status=" + status +
                '}';
    }
}
