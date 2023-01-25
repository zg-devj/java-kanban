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

    /**
     * Валидация задачи на пересечение с отсортированным списком
     * @param task классы наследуемые от BaseTask
     * @return true - не пересекается
     */
    public boolean validate(BaseTask task) {
        if (task.getStartTime() == null || listSorted.size() < 1) {
            return true;
        }
        // исключаем задачи с startTime = null
        Iterator<BaseTask> iter = listSorted.stream().filter(x->x.getStartTime()!=null).iterator();
        BaseTask prevTask = null;
        while (iter.hasNext()) {
            // текущий итерируемый объект
            BaseTask value = iter.next();
            // проверка только при обновлении задачи
            // задача пропускает саму себя чтоб не пересекаться с собой
            if(value.getId() == task.getId()){
                continue;
            }
            // начало итерируемого объекта совпадает с началом проверяемого
            if (value.getStartTime().equals(task.getStartTime())) {
                return false;
            }
            // если итерируемый объект последний
            if (!iter.hasNext()) {
                // начало добавляемого после конца итерируемого не пересекаются
                if (task.getStartTime().isAfter(value.getEndTime())) {
                    return true;
                }
            }
            // начало добавляемого после начала итерируемого
            if (task.getStartTime().isAfter(value.getStartTime())) {
                // устанавливаем итерируемый как текущий
                prevTask = value;
                // и если итерируемый не последний переходим к следующему
                if (iter.hasNext()) {
                    continue;
                }
            }
            // A || B
            // A - если есть предыдущий и конец предыдущего после начала добавляемого
            //     то существует пересечение
            // B - конец добавляемого после начала итерируемого
            //     то существует пересечение
            if (prevTask != null && prevTask.getEndTime().isAfter(task.getStartTime())
                    || task.getEndTime().isAfter(value.getStartTime())) {
                return false;
            }
        }
        return true;
    }
}
