import model.Base;
import model.Status;
import model.Task;
import util.Identifier;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class ManagerTask {
    // Идентификаторы для Task
    private Identifier tasksId;

    HashMap<Long, Task> tasks;

    public ManagerTask() {
        this.tasksId = new Identifier();
        this.tasks = new HashMap<>();
    }

    public void start() {
        Task task1 = new Task(tasksId.next(), "Переезд");
        task1.setDescriptions(new ArrayList<>(Arrays.asList("Собрать вещи", "Отнести в машину")));
        Task task2 = new Task(tasksId.next(), "Покупки");

        task1.setStatus(Status.IN_PROGRESS);

        tasks.put(task1.getId(),task1);
        tasks.put(task2.getId(),task2);

        for (Task value : tasks.values()) {
            System.out.println(value);
        }

    }
}
