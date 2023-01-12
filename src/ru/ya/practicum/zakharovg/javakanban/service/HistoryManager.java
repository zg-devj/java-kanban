package ru.ya.practicum.zakharovg.javakanban.service;

import ru.ya.practicum.zakharovg.javakanban.model.BaseTask;

import java.util.List;

public interface HistoryManager {
    void add(BaseTask task);
    void remove(int id);
    List<BaseTask> getHistory();
}
