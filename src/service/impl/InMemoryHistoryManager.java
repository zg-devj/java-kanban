package service.impl;

import model.BaseTask;
import service.HistoryManager;

import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {
    // максимальное число элементов в истории
    private static final Integer MAX_ELEMENTS = 10;
    // список истории
    private List<BaseTask> historyList;

    public InMemoryHistoryManager() {
        this.historyList = new ArrayList<>(MAX_ELEMENTS);
    }

    @Override
    public void add(BaseTask task) {
        if (historyList.size() == MAX_ELEMENTS) {
            historyList.remove(0);
        }
        historyList.add(task);
    }

    @Override
    public List<BaseTask> getHistory() {
        return historyList;
    }
}
