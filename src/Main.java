import model.Task;

public class Main {
    public static void main(String[] args) {
        ManagerTask managerTask = new ManagerTask();

        managerTask.addTask(new Task("Task 1"));
        managerTask.addTask(new Task("Task 2"));

        System.out.println(managerTask);
    }
}
