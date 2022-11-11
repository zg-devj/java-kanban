package util;

import service.TaskManager;

public class Managers<T extends TaskManager> {
    private T obj;

    public Managers(T obj) {
        this.obj = obj;
    }

    public T getDefault() {
        return obj;
    }
}
