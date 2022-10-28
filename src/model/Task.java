package model;

import java.util.Arrays;

// Задача
public class Task extends Base {
    public Task(int id, String title) {
        super(id, title);
        setStatus(Status.NEW);
    }

    @Override
    public void setStatus(Status status) {
        this.status = status;
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
