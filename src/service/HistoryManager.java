package service;

import model.BaseTask;

import java.util.List;

public interface HistoryManager {
    void add(BaseTask task);

    List<BaseTask> getHistory();
}
