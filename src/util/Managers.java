package util;

import service.TaskManager;
import service.impl.InMemoryTaskManager;

public class Managers<T extends TaskManager> {
    public T getDefault() {
        return (T) new InMemoryTaskManager();
    }
}
