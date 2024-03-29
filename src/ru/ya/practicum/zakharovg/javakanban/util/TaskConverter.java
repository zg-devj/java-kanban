package ru.ya.practicum.zakharovg.javakanban.util;

import ru.ya.practicum.zakharovg.javakanban.model.*;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

// преобразование тасков и истории в строку и обратно
public class TaskConverter {
    // преобразуем таск в строку
    // Формат: 3,TASK,Title,NEW,Description,1673337857927,15
    // Формат: 3,TASK,Title,NEW,Description,null,0
    public static String taskToString(Task task) {

        // Я намеренно использовал хранение в Unix epoch.
        // Для конвертации есть DateTimeConverter
        // В классе Task, Formatter забыл удалить, его там не должно быть.
        String savedStartTime = (task.getStartTime() == null)
                ? "null" : String.valueOf(task.getStartTime().toEpochMilli());
        String savedDuration = (task.getDurationMinute() == null)
                ? "null" : String.valueOf(task.getDurationMinute());

        return String.format("%d,%s,%s,%s,%s,%s,%s%n",
                task.getId(), TaskType.TASK.name(), task.getTitle(),
                task.getStatus().name(), task.getDescriptions(),
                savedStartTime, savedDuration);
    }

    public static String taskToString(Subtask subtask) {
        String savedStartTime = (subtask.getStartTime() == null)
                ? "null" : String.valueOf(subtask.getStartTime().toEpochMilli());
        String savedDuration = (subtask.getDurationMinute() == null)
                ? "null" : String.valueOf(subtask.getDurationMinute());

        return String.format("%d,%s,%s,%s,%s,%s,%s,%d%n",
                subtask.getId(), TaskType.SUBTASK.name(), subtask.getTitle(),
                subtask.getStatus().name(), subtask.getDescriptions(),
                savedStartTime, savedDuration,
                subtask.getEpicId());
    }

    public static String taskToString(Epic epic) {
        String str = getSubtasksIdToString(epic.getSubtaskIds());
        // нп мой взгляд запись вида ниже выглядит как буд то что-то потерялось
        // 3,EPIC,Epic1,NEW,Epic1 Description,
        // лучше запись ниже
        // 6,EPIC,Epic2,NEW,Epic2 Description
        // 6,EPIC,Epic2,NEW,Epic2 Description,10
        if (!str.isEmpty()) {
            return String.format("%d,%s,%s,%s,%s,%s%n",
                    epic.getId(), TaskType.EPIC.name(), epic.getTitle(),
                    epic.getStatus().name(), epic.getDescriptions(), str);
        } else {
            return String.format("%d,%s,%s,%s,%s%n",
                    epic.getId(), TaskType.EPIC.name(), epic.getTitle(),
                    epic.getStatus().name(), epic.getDescriptions());
        }
    }

    // преобразуем строку в таск
    public static BaseTask taskFromString(String[] taskLine) {
        Integer id = Integer.valueOf(taskLine[0]);
        TaskType type = TaskType.valueOf(taskLine[1]);
        String title = taskLine[2].toString();
        Status status = Status.valueOf(taskLine[3]);
        String desc = taskLine[4].toString().equals("null") ? null : taskLine[4].toString();
        Instant startTime;
        Long duration;
        switch (type) {
            case TASK:
                startTime = taskLine[5].toString().equals("null")
                        ? null : Instant.ofEpochMilli(Long.parseLong(taskLine[5]));
                duration = taskLine[6].toString().equals("null")
                        ? null : Long.parseLong(taskLine[6]);

                Task task = new Task(title, desc);
                task.setId(id);
                task.setStatus(status);
                task.setStartTime(startTime);
                task.setDurationMinute(duration);
                return task;
            case EPIC:
                Epic epic = new Epic(title, desc);
                epic.setId(id);
                epic.setStatus(status);
                if (taskLine.length > 5) {
                    String[] subtasks = taskLine[5].split(":");
                    for (String unit : subtasks) {
                        epic.add(Integer.valueOf(unit));
                    }
                }
                return epic;
            case SUBTASK:
                startTime = taskLine[5].toString().equals("null")
                        ? null : Instant.ofEpochMilli(Long.parseLong(taskLine[5]));
                duration = taskLine[6].toString().equals("null")
                        ? null : Long.parseLong(taskLine[6]);

                Integer epicId = Integer.valueOf(taskLine[7]);
                Subtask subtask = new Subtask(title, desc, startTime);
                subtask.setId(id);
                subtask.setEpicId(epicId);
                subtask.setDurationMinute(duration);
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
    // Я понимаю, что может возникнуть NPE, но пока этого не может случиться,
    // т.к. не так много использования этого метода
    public static List<Integer> historyFromString(String value) {
        if (value != null && !value.isEmpty() && !value.isBlank()) {
            String[] historyArr = value.split(",");
            List<Integer> ret = new ArrayList<>();
            for (int i = 0; i < historyArr.length; i++) {
                ret.add(Integer.valueOf(historyArr[i]));
            }
            return ret;
        }
        return new ArrayList<>();
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
            return "";
        }
    }

}
