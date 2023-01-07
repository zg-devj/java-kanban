import model.BaseTask;
import model.Epic;
import model.Subtask;
import model.Task;
import service.TaskManager;
import util.Managers;

public class MainTest3 {
    public static void main(String[] args) {
        TaskManager manager = Managers.getDefault();

        manager.addTask(new Task("Task 1", "Description")); //1
        manager.addTask(new Task("Task 2", "Description")); //2
        Epic epic = new Epic("Epic 1","Description");
        manager.addEpic(epic); //3
        int epicId = epic.getId();
        manager.addSubtask(new Subtask(epicId, "Subtask 1", "Description")); //4
        manager.addSubtask(new Subtask(epicId, "Subtask 2", "Description")); //5
        manager.addSubtask(new Subtask(epicId, "Subtask 3", "Description")); //6
        Epic epic2 = new Epic("Epic 2","Description");
        manager.addEpic(epic2); //7

        System.out.println(manager.getAllTasks());
        System.out.println(manager.getAllEpics());
        System.out.println(manager.getAllSubtasks());

        manager.getTask(1);
        manager.getTask(2);
        manager.getSubtask(4);
        manager.getEpic(7);
        printHistory(manager); // 1,2,4,7

        manager.getSubtask(6);
        manager.getTask(2);
        manager.getEpic(3);
        printHistory(manager); // 1,4,7,6,2,3

        manager.deleteTask(1);
        printHistory(manager); // 4,7,6,2,3

        manager.deleteEpic(3);
        printHistory(manager); // 7,2
    }

    public static void printHistory(TaskManager manager) {
        System.out.println();
        for (BaseTask task : manager.getHistory()) {
            System.out.println("id=" + task.getId() + ", " + task.getTitle());
        }
    }
}
