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

    public boolean validate(BaseTask task) {
        boolean isValid = true;
        if (task.getStartTime() == null || listSorted.size() < 1) {
            return isValid;
        }
        Iterator<BaseTask> iter = listSorted.iterator();
        BaseTask prevTask = null;
        while (iter.hasNext()) {
            BaseTask value = iter.next();
            if (value.getStartTime() == null) {
                continue;
            }
            if (value.getStartTime().equals(task.getStartTime())) {
                isValid = false;
                break;
            }
            if (!iter.hasNext()) {
                // последняя итерация
                if (task.getStartTime().isAfter(value.getEndTime())) {
                    isValid = true;
                    break;
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
                isValid = false;
                break;
            }
        }
        return isValid;
    }
}
