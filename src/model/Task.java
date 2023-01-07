package model;

// Задача
public class Task extends BaseTask {

    public Task(String title, String descriptions) {
        super(title, descriptions);
        setStatus(Status.NEW);
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
