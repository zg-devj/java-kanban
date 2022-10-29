import model.Subtask;
import model.Task;

public class Main {
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

        Subtask subtask1 = new Subtask("Подзадача 1");
        managerTask.addSubtask(subtask1);
        managerTask.addTaskToSubtask(subtask1, task1);
        managerTask.addTaskToSubtask(subtask1, task3);

        Subtask subtask2 = new Subtask("Подзадача 2");
        managerTask.addSubtask(subtask2);
        managerTask.addTaskToSubtask(subtask2, task4);

        System.out.println("\nTASK");
        System.out.println(managerTask.getAllTasks());
        System.out.println("\nSUBTASK");
        System.out.println(managerTask.getAllSubtasks());
    }
}
