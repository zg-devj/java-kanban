package util;

import service.HistoryManager;
import service.TaskManager;
import service.impl.InMemoryHistoryManager;
import service.impl.InMemoryTaskManager;

public class Managers {

    public static TaskManager getDefault() {
        return new InMemoryTaskManager();
    }

    /*
     * Из ТЗ:
     * Добавьте в служебный класс Managers статический метод HistoryManager getDefaultHistory().
     * Он должен возвращать объект InMemoryHistoryManager — историю просмотров.
     */
    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }
}
