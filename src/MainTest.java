import model.BaseTask;
import model.Epic;
import model.Task;
import service.TaskManager;
import service.impl.InMemoryTaskManager;
import util.Managers;

public class MainTest {
    public static void main(String[] args) {
        TaskManager manager = Managers.getDefault();

        manager.addTask(new Task("Task 1")); //1
        manager.addTask(new Task("Task 2")); //2
        manager.addTask(new Task("Task 3")); //3
        manager.addTask(new Task("Task 4")); //4
        manager.addTask(new Task("Task 5")); //5
        manager.addTask(new Task("Task 6")); //6
        manager.addTask(new Task("Task 7")); //7
        manager.addTask(new Task("Task 8")); //8
        manager.addTask(new Task("Task 9")); //9
        manager.addTask(new Task("Task 10")); //10
        manager.addTask(new Task("Task 11")); //11
        Epic epic = new Epic("Epic 1");
        manager.addEpic(epic); //12
        int epicId = epic.getId();
        manager.addSubtaskToEpic(epicId, "Subtask 1", null); //13

        manager.getEpic(12);
        manager.getTask(8);
        manager.getTask(8);
        manager.getTask(22); // null
        manager.getSubtask(13);
        manager.getTask(3);

        for (BaseTask task : manager.getHistory()) {
            System.out.println("id=" + task.getId() + ", " + task.getTitle());
        }
    }
}
