package util;

import model.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class TaskConverter {
    // преобразуем таск в строку
    public static String taskToString(Task task) {
        return String.format("%d,%s,%s,%s,%s%n",
                task.getId(), TaskType.TASK.name(), task.getTitle(),
                task.getStatus().name(), task.getDescriptions());
    }

    public static String taskToString(Subtask subtask) {
        return String.format("%d,%s,%s,%s,%s,%d%n",
                subtask.getId(), TaskType.SUBTASK.name(), subtask.getTitle(),
                subtask.getStatus().name(), subtask.getDescriptions(),
                subtask.getEpicId());
    }

    public static String taskToString(Epic epic) {
        return String.format("%d,%s,%s,%s,%s,%s%n",
                epic.getId(), TaskType.EPIC.name(), epic.getTitle(),
                epic.getStatus().name(), epic.getDescriptions(),
                getSubtasksIdToString(epic.getSubtaskIds()));
    }

    // преобразуем строку в таск
    public static BaseTask taskFromString(String value) {
        String[] taskLine = value.split(",");
        Integer id = Integer.valueOf(taskLine[0]);
        TaskType type = TaskType.valueOf(taskLine[1]);
        String title = taskLine[2].toString();
        Status status = Status.valueOf(taskLine[3]);
        String desc = taskLine[4].toString().equals("null") ? null : taskLine[4].toString();
        switch (type) {
            case TASK:
                Task task = new Task(title, desc);
                task.setId(id);
                task.setStatus(status);
                return task;
            case EPIC:
                String[] subtasks = taskLine[5].split(":");
                Epic epic = new Epic(title, desc);
                epic.setId(id);
                epic.setStatus(status);
                if (Integer.valueOf(subtasks[0]) != 0) {
                    for (String unit : subtasks) {
                        epic.add(Integer.valueOf(unit));
                    }
                }
                return epic;
            case SUBTASK:
                Integer epicId = Integer.valueOf(taskLine[5]);
                Subtask subtask = new Subtask(epicId, title, desc);
                subtask.setId(id);
                subtask.setStatus(status);
                return subtask;
        }
        return null;
    }

    // преобразуем историю в строку
    public static String historyToString(List<BaseTask> list) {
        List<String> ret = new ArrayList<>();
        for (BaseTask task : list) {
            ret.add(String.valueOf(task.getId()));
        }
        return String.join(",", ret);

    }

    // преобразуем строку в историю
    public static List<Integer> historyFromString(String value) {
        String[] historyArr = value.split(",");
        if (historyArr.length > 0) {
            List<Integer> ret = new ArrayList<>();
            for (int i = 0; i < historyArr.length; i++) {
                ret.add(Integer.valueOf(historyArr[i]));
            }
            return ret;
        }
        return null;
    }

    // преобразуем в строку ссылки эпика на сабтаски
    // пример:
    // return: 2:3:4  -> эпик ссылается на сабтаски c id: 2,3,4
    private static String getSubtasksIdToString(HashSet<Integer> list) {
        if (list.size() > 0) {
            List<String> ret = new ArrayList<>();
            for (Integer id : new ArrayList<>(list)) {
                ret.add(String.valueOf(id));
            }
            return String.join(":", ret);
        } else {
            // если у эпика еще нет сабтасков
            return "0";
        }
    }
}
