import service.TaskManager;
import util.Managers;

public class MainTest5 {
    public static void main(String[] args) {
        // тестирование считывания и заполнения данных из файла

        TaskManager manager = Managers.getFileStorage();

        System.out.println(manager.getAllTasks());
        System.out.println(manager.getAllEpics());
        System.out.println(manager.getAllSubtasks());
    }
}
