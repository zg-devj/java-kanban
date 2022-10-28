import model.Task;
import util.Identifier;

import java.util.ArrayList;
import java.util.Arrays;

public class ManagerTask {
    // Идентификаторы для Task
    private Identifier tasksId;


    public ManagerTask() {
        this.tasksId = new Identifier();
    }

    public void start() {
        /*Task task1 = new Task(tasksId.next(), "Переезд");
        Task task2 = new Task(tasksId.next(), "Покупки");

        task1.setDescriptions(new ArrayList<>(Arrays.asList("Собрать вещи", "Отнести в машину")));

        System.out.println(task1);
        System.out.println(task2);*/
    }
}
