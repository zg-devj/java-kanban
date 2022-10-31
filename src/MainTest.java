import model.Epic;
import model.Status;
import model.Subtask;
import model.Task;

public class MainTest {
    public static void main(String[] args) {
        ManagerTask managerTask = new ManagerTask();

        Task task1 = new Task("Задача 1");
        Task task2 = new Task("Задача 2");
        Task task3 = new Task("Задача 3");
        Task task4 = new Task("Задача 4");
        managerTask.addTask(task1);
        managerTask.addTask(task2);
        managerTask.addTask(task3);
        managerTask.addTask(task4);

//        Subtask subtask1 = new Subtask("Подзадача 1");
//        managerTask.addSubtask(subtask1);
//
//        Subtask subtask2 = new Subtask("Подзадача 2");
//        managerTask.addSubtask(subtask2);

        Epic epic1 = new Epic("Эпик 1");
        managerTask.addEpic(epic1);
//        managerTask.addSubtaskToSEpic(epic1, subtask1);
//        managerTask.addSubtaskToSEpic(epic1, subtask2);

        Epic epic2 = new Epic("Эпик 2");
        managerTask.addEpic(epic2);

        Task update1 = managerTask.getTaskById(1);
        update1.setStatus(Status.NEW);
        managerTask.updateTask(update1);

        Task update2 = managerTask.getTaskById(2);
        update2.setStatus(Status.NEW);
        managerTask.updateTask(update2);

        Task update3 = managerTask.getTaskById(3);
        update3.setStatus(Status.DONE);
        managerTask.updateTask(update3);

        Task update4 = managerTask.getTaskById(4);
        update4.setStatus(Status.DONE);
        managerTask.updateTask(update4);

        // Здесь указывать команды для тестирования
        // managerTask.deleteSubtask(2);

        System.out.println("\nTASK");
        System.out.println(managerTask.getAllTasks());
        System.out.println("\nSUBTASK");
        System.out.println(managerTask.getAllSubtasks());
        System.out.println("\nEPIC");
        System.out.println(managerTask.getAllEpics());
    }
}
