package util;

import service.HistoryManager;
import service.TaskManager;
import service.impl.InMemoryHistoryManager;

public class Managers<T extends TaskManager> {
    private T obj;

    public Managers(T obj) {
        this.obj = obj;
    }

    public T getDefault() {
        return obj;
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }
}
