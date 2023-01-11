package service;

import model.BaseTask;
import model.Epic;
import model.Subtask;
import model.Task;
import util.SortedBaseTask;

import java.util.List;
import java.util.Set;

public interface TaskManager {
    // TODO: 11.01.2023 Delete
    Set<BaseTask> getPrioritizedTasks();
    SortedBaseTask getSortedTasks();

    //region Task
    Task getTask(int id);

    List<Task> getAllTasks();

    int addTask(Task task);

    void updateTask(Task task);

    void deleteTask(int id);

    void deleteAllTasks();
    //endregion

    //region Subtask
    Subtask getSubtask(int id);

    List<Subtask> getAllSubtasks();

    List<Subtask> getSubtasksByEpicId(int epicId);

    void updateSubtask(Subtask subtask);

    void deleteSubtask(int id);

    void deleteAllSubtasks();
    //endregion

    //region Epic
    Epic getEpic(int id);

    List<Epic> getAllEpics();

    int addEpic(Epic epic);

    int addSubtask(Subtask subtask);

    void updateEpic(Epic epic);

    void deleteEpic(int id);

    void deleteAllEpics();
    //endregion

    List<BaseTask> getHistory();
}
