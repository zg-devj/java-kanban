package model;

// Задача
public class Task extends Base {

    public Task(String title, String descriptions) {
        super(title, descriptions);
        setStatus(Status.NEW);
    }

    public Task(String title) {
        this(title, null);
    }

    @Override
    public String toString() {
        return "Task{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", descriptions='" + descriptions + '\'' +
                ", status=" + status +
                '}';
    }
}
