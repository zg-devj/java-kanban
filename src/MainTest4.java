import model.Epic;
import model.Task;
import service.TaskManager;
import util.Managers;

public class MainTest4 {
    public static void main(String[] args) {
        // Тестирование работы с файлом

        TaskManager manager = Managers.getFileStorage();
        manager.addTask(new Task("Task1", "Task1 Description")); //1
        manager.addTask(new Task("Task2", "Task2 Description")); //2
        Epic epic = new Epic("Epic1", "Epic1 Description");
        manager.addEpic(epic);
        int epicId = epic.getId(); //3
        manager.addSubtaskToEpic(epicId, "Subtask1", "Subtask1 Description"); //4

        manager.getTask(1);
        manager.getSubtask(4);
        manager.getEpic(3);
        manager.getTask(1);
    }
}
