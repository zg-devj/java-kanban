import model.Epic;
import model.Task;
import service.TaskManager;
import util.Managers;

public class MainTest4 {
    public static void main(String[] args) {
        // Тестирование работы создание файла для данных

        TaskManager manager = Managers.getFileStorage();
        manager.addTask(new Task("Task1", "Task1 Description")); //1
        manager.addTask(new Task("Task2", "Task2 Description")); //2
        Epic epic = new Epic("Epic1", "Epic1 Description");
        manager.addEpic(epic);
        int epicId = epic.getId(); //3
        manager.addSubtaskToEpic(epicId, "Subtask1", "Subtask1 Description"); //4
        manager.addSubtaskToEpic(epicId, "Subtask2", "Subtask2 Description"); //5
        Epic epic2 = new Epic("Epic2", "Epic2 Description");
        manager.addEpic(epic2); // 6

        manager.getTask(1);
        manager.getSubtask(4);
        manager.getEpic(3);
        manager.getSubtask(5);
        manager.getTask(2);


        // для теста
        // manager.deleteTask(1); // удалятся 1
        // manager.deleteEpic(3); // удалятся 3,4,5
    }
}
