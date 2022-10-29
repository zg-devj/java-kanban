import model.Epic;
import model.Subtask;
import model.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ManagerTask {
    // Идентификаторы для Task

    public HashMap<Integer, Task> tasks;
    public HashMap<Integer, Subtask> subtasks;
    public HashMap<Integer, Epic> epics;

    public ManagerTask() {
        this.tasks = new HashMap<>();
        this.subtasks = new HashMap<>();
        this.epics = new HashMap<>();
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

    // Получение всех записей Subtask-ов
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
            int parentId = getSubtaskById(id).getParentId();
            if (parentId > 0) {
                // если Subtask находится в Epic, то отвязываем
                getEpicById(parentId).getSubtaskIds().remove(id);
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

    //region Epic методы
    // Получение Epic по идентификатору.
    public Epic getEpicById(int id) {
        if (epics.containsKey(id)) {
            return epics.get(id);
        } else {
            return null;
        }
    }

    // Получение всех записей Epic-ов
    public List<Epic> getAllEpics() {
        return new ArrayList<>(epics.values());
    }

    // Cоздание Epic
    public void addEpic(Epic epic) {
        epics.putIfAbsent(epic.getId(), epic);
    }

    // Добавляем подзадачу к эпику
    public void addSubtaskToSEpic(Epic epic, Subtask subtask) {
        if (epics.containsKey(epic.getId()) && subtasks.containsKey(subtask.getId())) {
            epic.add(subtask);
        }
    }

    // Обновление Epic
    public void updateEpic(Epic epic) {
        epics.put(epic.getId(), epic);
    }

    // Удаление Epic
    public void deleteEpic(int id) {
        if (epics.containsKey(id)) {
            // Удаляем Subtask для Epic
            for (Integer subtaskId : epics.get(id).getSubtaskIds()) {
                deleteSubtask(subtaskId);
            }
            // Удаляем Epic
            epics.remove(id);
        }
    }

    // Удалить все Epic
    public void deleteAllEpics() {
        for (Epic epic : getAllEpics()) {
            deleteEpic(epic.getId());
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
