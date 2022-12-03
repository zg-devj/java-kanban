import model.Epic;
import model.Status;
import model.Task;
import service.TaskManager;
import util.Managers;

public class MainTest1 {
    public static void main(String[] args) {
        TaskManager managerTask = Managers.getDefault();

        Task task1 = new Task("Задача 1");
        Task task2 = new Task("Задача 2", "Описание задачи 2");
        Task task3 = new Task("Задача 3", "Описание задачи 3");
        Task task4 = new Task("Задача 4");
        managerTask.addTask(task1);
        managerTask.addTask(task2);
        managerTask.addTask(task3);
        managerTask.addTask(task4);


        Epic epic1 = new Epic("Эпик 1");
        managerTask.addEpic(epic1);
        int epic1Id = epic1.getId();
        managerTask.addSubtaskToEpic(epic1Id, "Подзадача 1", null);
        managerTask.addSubtaskToEpic(epic1Id, "Подзадача 2", "Описание подзадачи");
        managerTask.addSubtaskToEpic(epic1Id, "Подзадача 3", null);

        Epic epic2 = new Epic("Эпик 2");
        managerTask.addEpic(epic2);
        int epic2Id = epic2.getId();
        managerTask.addSubtaskToEpic(epic2Id, "Подзадача 4", null);
        managerTask.addSubtaskToEpic(epic2Id, "Подзадача 5", "Описание подзадачи");

        Task update1 = managerTask.getTask(1);
        update1.setStatus(Status.IN_PROGRESS);
        managerTask.updateTask(update1);

        Task update2 = managerTask.getTask(2);
        update2.setStatus(Status.NEW);
        managerTask.updateTask(update2);

        Task update3 = managerTask.getTask(3);
        update3.setStatus(Status.DONE);
        managerTask.updateTask(update3);

        Task update4 = managerTask.getTask(4);
        update4.setStatus(Status.DONE);
        managerTask.updateTask(update4);

        // -------------------------------------

        // Здесь указывать команды для тестирования


        System.out.println("\nTASK");
        System.out.println(managerTask.getAllTasks());
        System.out.println("\nSUBTASK");
        System.out.println(managerTask.getAllSubtasks());
        System.out.println("\nEPIC");
        System.out.println(managerTask.getAllEpics());
    }
}
