import model.Subtask;
import model.Task;
import util.Identifier;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ManagerTask {
    // Идентификаторы для Task

    public HashMap<Integer, Task> tasks;
    public HashMap<Integer, Subtask> subtasks;

    public ManagerTask() {
        this.tasks = new HashMap<>();
        this.subtasks = new HashMap<>();
    }

    //region Task методы
    // Получение Task по идентификатору.
    public Task getTaskById(int id) {
        return tasks.get(id);
    }

    // Получение всех записей Task-ов
    public List<Task> getAllTasks() {
        return new ArrayList<>(tasks.values());
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
            int parentId = getTaskById(id).getParentId();
            if (parentId > 0) {
                // если Task находится в Subtask, то отвязываем
                getSubtaskById(parentId).getTaskIds().remove(id);
            }
            tasks.remove(id);
        }
    }

    // Удаление все Tasks
    public void deleteAllTasks() {
        // Удаляем task и зависимости в subtask
        for (Task task : getAllTasks()) {
            deleteTask(task.getId());
        }
        // Удаляем остальные task
        tasks.clear();
    }
    //endregion

    //region Subtask методы
    // Получение Subtask по идентификатору.
    public Subtask getSubtaskById(int id) {
        if (subtasks.containsKey(id)) {
            return subtasks.get(id);
        } else {
            return null;
        }
    }

    // Получение всех записей Task-ов
    public List<Subtask> getAllSubtasks() {
        return new ArrayList<>(subtasks.values());
    }

    // Cоздание Subtask
    public void addSubtask(Subtask subtask) {
        subtasks.putIfAbsent(subtask.getId(), subtask);
    }

    // Добавляем задачу к подзадаче
    public void addTaskToSubtask(Subtask subtask, Task task) {
        if (subtasks.containsKey(subtask.getId()) && tasks.containsKey(task.getId())) {
            subtask.add(task);
        }
    }

    // Обновление Subtask
    public void updateSubtask(Subtask subtask) {
        subtasks.put(subtask.getId(), subtask);
    }

    // Удаление Subtask
    public void deleteSubtask(int id) {
        if (subtasks.containsKey(id)) {
            // Удаляем Task для Subtask
            for (Integer taskId : subtasks.get(id).getTaskIds()) {
                tasks.remove(taskId);
            }
            // Удаляем Subtask
            subtasks.remove(id);
        }
    }

    // Удалить все Subtask
    public void deleteAllSubtasks() {
        for (Subtask subtask : getAllSubtasks()) {
            deleteSubtask(subtask.getId());
        }
    }
    //endregion

    @Override
    public String toString() {
        return "ManagerTask{" +
                "tasks=" + tasks +
                ", subtasks=" + subtasks +
                '}';
    }
}
