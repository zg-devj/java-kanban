package util;

import service.HistoryManager;
import service.TaskManager;
import service.impl.InMemoryHistoryManager;
import service.impl.InMemoryTaskManager;

public class Managers {

    public static TaskManager getDefault() {
        return new InMemoryTaskManager();
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }
}
