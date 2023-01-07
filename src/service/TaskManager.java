package service;

import model.BaseTask;
import model.Epic;
import model.Subtask;
import model.Task;

import java.util.List;

public interface TaskManager {
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
