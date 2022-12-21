import model.BaseTask;
import model.Epic;
import model.Task;
import service.TaskManager;
import util.Managers;

public class MainTest5 {
    public static void main(String[] args) {
        // тестирование считывания и заполнения данных из файла

        TaskManager manager = Managers.getFileStorage();

        //manager.addTask(new Task("Example"));
        //manager.addEpic(new Epic("EPIC"));
        manager.addSubtaskToEpic(6,"SubTaskA",null);

        System.out.println(manager.getAllTasks());
        System.out.println(manager.getAllEpics());
        System.out.println(manager.getAllSubtasks());

        printHistory(manager);
    }

    public static void printHistory(TaskManager manager) {
        System.out.println();
        for (BaseTask task : manager.getHistory()) {
            System.out.println("id=" + task.getId() + ", " + task.getTitle());
        }
    }
}
