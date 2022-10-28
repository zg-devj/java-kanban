package model;

// Задача
public class Task extends Base {
    public Task(String title) {
        super(title);
        setStatus(Status.NEW);
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
