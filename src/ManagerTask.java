import model.Task;
import util.Identifier;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ManagerTask {
    // Идентификаторы для Task

    public HashMap<Integer, Task> tasks;

    public ManagerTask() {
        this.tasks = new HashMap<>();
    }

    // Получение всех записей Task-ов
    public List<Task> getAllTasks() {
        return new ArrayList<>(tasks.values());
    }

    // Удаление все Tasks
    public void deleteAllTasks() {
        tasks.clear();
    }

    // Получение Task по идентификатору.
    public Task getTaskById(int id) {
        return tasks.get(id);
    }

    // Cоздание Task
    public void addTask(Task task) {
        tasks.putIfAbsent(task.getId(), task);
    }

    // Обновление Task
    public void updateTask(Task task) {
        tasks.put(task.getId(), task);
    }

    // Удаление Task
    public void deleteTask(int id) {
        if (tasks.containsKey(id)) {
            tasks.remove(id);
        }
    }

    @Override
    public String toString() {
        return "ManagerTask{" +
                "tasks=" + tasks +
                '}';
    }
}
