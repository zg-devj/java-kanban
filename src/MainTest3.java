import model.BaseTask;
import model.Epic;
import model.Task;
import service.TaskManager;
import util.Managers;

public class MainTest3 {
    public static void main(String[] args) {
        TaskManager manager = Managers.getDefault();

        manager.addTask(new Task("Task 1")); //1
        manager.addTask(new Task("Task 2")); //2
        Epic epic = new Epic("Epic 1");
        manager.addEpic(epic); //3
        int epicId = epic.getId();
        manager.addSubtaskToEpic(epicId, "Subtask 1", null); //4
        manager.addSubtaskToEpic(epicId, "Subtask 2", null); //5
        manager.addSubtaskToEpic(epicId, "Subtask 3", null); //6
        Epic epic2 = new Epic("Epic 2");
        manager.addEpic(epic2); //7

        System.out.println(manager.getAllTasks());
        System.out.println(manager.getAllEpics());
        System.out.println(manager.getAllSubtasks());

        manager.getTask(1);
        manager.getTask(2);
        manager.getSubtask(4);
        manager.getEpic(7);
        printHistory(manager); // 7,4,2,1

        manager.getSubtask(6);
        manager.getTask(2);
        manager.getEpic(3);
        printHistory(manager); // 3,2,6,7,4,1

        manager.deleteTask(1);
        printHistory(manager); // 3,2,6,7,4

        manager.deleteEpic(3);
        printHistory(manager); // 2,7
    }

    public static void printHistory(TaskManager manager) {
        System.out.println();
        for (BaseTask task : manager.getHistory()) {
            System.out.println("id=" + task.getId() + ", " + task.getTitle());
        }
    }
}
