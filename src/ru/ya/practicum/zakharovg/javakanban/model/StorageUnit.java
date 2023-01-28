package ru.ya.practicum.zakharovg.javakanban.model;

import java.util.Collection;

public class StorageUnit {
    Collection<Task> tasks;
    Collection<Epic> epics;
    Collection<Subtask> subtasks;
    String history;

    public StorageUnit(Collection<Task> tasks,
                       Collection<Epic> epics,
                       Collection<Subtask> subtasks,
                       String history) {
        this.tasks = tasks;
        this.epics = epics;
        this.subtasks = subtasks;
        this.history = history;
    }

    public Collection<Task> getTasks() {
        return tasks;
    }

    public Collection<Epic> getEpics() {
        return epics;
    }

    public Collection<Subtask> getSubtasks() {
        return subtasks;
    }

    public String getHistory() {
        return history;
    }
}
