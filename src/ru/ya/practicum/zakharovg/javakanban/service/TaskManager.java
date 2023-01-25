package ru.ya.practicum.zakharovg.javakanban.service;

import ru.ya.practicum.zakharovg.javakanban.model.BaseTask;
import ru.ya.practicum.zakharovg.javakanban.model.Epic;
import ru.ya.practicum.zakharovg.javakanban.model.Subtask;
import ru.ya.practicum.zakharovg.javakanban.model.Task;

import java.util.List;
import java.util.Set;

public interface TaskManager {
    //region Task
    boolean containsTask(int id);

    Task getTask(int id);

    List<Task> getAllTasks();

    int addTask(Task task);

    void updateTask(Task task);

    void deleteTask(int id);

    void deleteAllTasks();
    //endregion

    //region Subtask
    boolean containsSubtask(int id);

    Subtask getSubtask(int id);

    List<Subtask> getAllSubtasks();

    List<Subtask> getSubtasksByEpicId(int epicId);

    int addSubtask(int epicId, Subtask subtask);

    void updateSubtask(Subtask subtask);

    void deleteSubtask(int id);

    void deleteAllSubtasks();
    //endregion

    //region Epic
    boolean containsEpic(int id);

    Epic getEpic(int id);

    List<Epic> getAllEpics();

    int addEpic(Epic epic);

    void updateEpic(Epic epic);

    void deleteEpic(int id);

    void deleteAllEpics();
    //endregion

    Set<BaseTask> getPrioritizedTasks();

    List<BaseTask> getHistory();
}
