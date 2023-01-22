package ru.ya.practicum.zakharovg.javakanban.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StorageUnit {
    HashMap<Integer, Task> tasks;
    HashMap<Integer, Epic> epics;
    HashMap<Integer, Subtask> subtasks;
    List<BaseTask> history;

    public StorageUnit(HashMap<Integer, Task> tasks,
                       HashMap<Integer, Epic> epics,
                       HashMap<Integer, Subtask> subtasks,
                       List<BaseTask> history) {
        this.tasks = tasks;
        this.epics = epics;
        this.subtasks = subtasks;
        this.history = history;
    }

    public HashMap<Integer, Task> getTasks() {
        return tasks;
    }

    public HashMap<Integer, Subtask> getSubtasks() {
        return subtasks;
    }

    public HashMap<Integer, Epic> getEpics() {
        return epics;
    }

    public List<BaseTask> getHistory() {
        return history;
    }
}
