package service.impl;

import model.BaseTask;
import service.HistoryManager;

import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {
    // список истории
    private List<BaseTask> historyList;

    public InMemoryHistoryManager() {
        this.historyList = new ArrayList<>();
    }

    @Override
    public void add(BaseTask task) {
        if (historyList.size() == 10) {
            historyList.remove(0);
        }
        historyList.add(task);
    }

    @Override
    public List<BaseTask> getHistory() {
        return historyList;
    }
}
