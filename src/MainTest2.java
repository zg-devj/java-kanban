import model.BaseTask;
import model.Epic;
import model.Subtask;
import model.Task;
import service.TaskManager;
import util.Managers;

public class MainTest2 {
    public static void main(String[] args) {
        TaskManager manager = Managers.getDefault();

        manager.addTask(new Task("Task 1", "Description")); //1
        manager.addTask(new Task("Task 2", "Description")); //2
        manager.addTask(new Task("Task 3", "Description")); //3
        manager.addTask(new Task("Task 4", "Description")); //4
        manager.addTask(new Task("Task 5", "Description")); //5
        manager.addTask(new Task("Task 6", "Description")); //6
        manager.addTask(new Task("Task 7", "Description")); //7
        manager.addTask(new Task("Task 8", "Description")); //8
        manager.addTask(new Task("Task 9", "Description")); //9
        manager.addTask(new Task("Task 10", "Description")); //10
        manager.addTask(new Task("Task 11", "Description")); //11
        Epic epic = new Epic("Epic 1","Description1");
        manager.addEpic(epic); //12
        int epicId = epic.getId();
        manager.addSubtask(new Subtask(epicId,"Subtask", "Description")); //13

        manager.getTask(5);
        manager.getEpic(12);
        manager.getTask(8);
        manager.getSubtask(13);
        manager.getTask(3);


        for (BaseTask task : manager.getHistory()) {
            System.out.println("id=" + task.getId() + ", " + task.getTitle());
        }
    }
}
