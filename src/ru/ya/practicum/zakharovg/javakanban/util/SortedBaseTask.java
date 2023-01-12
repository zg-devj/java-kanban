package ru.ya.practicum.zakharovg.javakanban.util;

import ru.ya.practicum.zakharovg.javakanban.model.BaseTask;

import java.util.Iterator;
import java.util.TreeSet;

public class SortedBaseTask {
    private TreeSet<BaseTask> listSorted;

    public SortedBaseTask() {
        listSorted = new TreeSet<>(new TaskComparator());
    }

    public void add(BaseTask task) {
        listSorted.add(task);
    }

    public void remove(BaseTask task) {
        listSorted.removeIf(t -> t.getId() == task.getId());
    }

    public TreeSet<BaseTask> getList() {
        return listSorted;
    }

    // код оставил свой, на все возможных случаях проверил, работает
    // Скриншот и комментарии в папке /feedback
    public boolean validate(BaseTask task) {
        if (task.getStartTime() == null || listSorted.size() < 1) {
            return true;
        }
        Iterator<BaseTask> iter = listSorted.iterator();
        BaseTask prevTask = null;
        while (iter.hasNext()) {
            BaseTask value = iter.next();
            if (value.getStartTime() == null) {
                return true;
            }
            if (value.getStartTime().equals(task.getStartTime())) {
                return false;
            }
            if (!iter.hasNext()) {
                // последняя итерация
                if (task.getStartTime().isAfter(value.getEndTime())) {
                    return true;
                }
            }
            if (value.getStartTime() == null || task.getStartTime().isAfter(value.getStartTime())) {
                prevTask = value;
                if (iter.hasNext()) {
                    continue;
                }
            }
            if (prevTask != null && prevTask.getEndTime().isAfter(task.getStartTime())
                    || task.getEndTime().isAfter(value.getStartTime())) {
                return false;
            }
        }
        return true;
    }
}
