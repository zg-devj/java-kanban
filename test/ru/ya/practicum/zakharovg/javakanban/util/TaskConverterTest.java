package ru.ya.practicum.zakharovg.javakanban.util;


import org.junit.jupiter.api.Test;
import ru.ya.practicum.zakharovg.javakanban.model.*;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TaskConverterTest {
    // 1673427600000
    private static final String DATETIME_FOR_TEST = "11.01.2023 12:00";

    @Test
    public void checkTaskToString() {
        Task task = new Task("Task1", "Description1");
        task.setId(1);

        String actual = TaskConverter.taskToString(task);
        String expected = "1,TASK,Task1,NEW,Description1,null,null\n";
        assertEquals(expected, actual, "Конвертация Task не соответствует ожиданию");
    }

    @Test
    public void taskToString_ConvertTaskToStringCsv() {
        Task task1 = new Task("Task", "Desc");
        task1.setId(1);
        Task task2 = new Task("Task", "Desc", DATETIME_FOR_TEST);
        task2.setId(1);
        Task task3 = new Task("Task", "Desc", 30);
        task3.setId(1);
        Task task4 = new Task("Task", "Desc", DATETIME_FOR_TEST, 120);
        task4.setId(1);

        String expected1 = "1,TASK,Task,NEW,Desc,null,null\n";
        String expected2 = "1,TASK,Task,NEW,Desc,1673427600000,null\n";
        String expected3 = "1,TASK,Task,NEW,Desc,null,30\n";
        String expected4 = "1,TASK,Task,NEW,Desc,1673427600000,120\n";

        assertEquals(expected1, TaskConverter.taskToString(task1));
        assertEquals(expected2, TaskConverter.taskToString(task2));
        assertEquals(expected3, TaskConverter.taskToString(task3));
        assertEquals(expected4, TaskConverter.taskToString(task4));

    }

    @Test
    public void taskToString_ConvertTaskAndEpicToStringCsv() {
        Epic epic = new Epic("Epic1", "Desc");
        epic.setId(1);

        Subtask subtask2 = new Subtask("Subtask2", "Desc");
        subtask2.setId(2);
        epic.add(subtask2);

        Subtask subtask3 = new Subtask("Subtask3", "Desc", DATETIME_FOR_TEST, 45);
        subtask3.setId(3);
        epic.add(subtask3);

        // Epic To String
        String actual1 = TaskConverter.taskToString(epic);
        String expected1 = "1,EPIC,Epic1,NEW,Desc,2:3\n";
        assertEquals(expected1, actual1, "Конвертация Epic не соответствует ожиданию");

        // Subtask To String
        String actual2 = TaskConverter.taskToString(subtask2);
        String expected2 = "2,SUBTASK,Subtask2,NEW,Desc,null,null,1\n";
        assertEquals(expected2, actual2, "Конвертация Subtask не соответствует ожиданию");

        // Subtask To String
        String actual3 = TaskConverter.taskToString(subtask3);
        String expected3 = "2,SUBTASK,Subtask2,NEW,Desc,1673427600000,45,1\n";
        assertEquals(expected2, actual2, "Конвертация Subtask не соответствует ожиданию");
    }

    @Test
    public void taskFromString_FromStringToTask() {
        String[] taskArray = "1,TASK,Task,NEW,Desc,1673427600000,120".split(",");
        BaseTask actual1 = TaskConverter.taskFromString(taskArray);
        Task task1 = new Task("Task", "Desc", DATETIME_FOR_TEST, 120);
        task1.setId(1);

        assertEquals(task1, actual1, "Задачи не совпадают");
        assertEquals("Task", actual1.getTitle());
        assertEquals(Status.NEW, actual1.getStatus());
        assertEquals(120, actual1.getDurationMinute());
    }

    @Test
    public void taskFromString_FromStringToEpic() {
        String[] epicArray = "1,EPIC,Epic,NEW,Desc".split(",");
        BaseTask actual1 = TaskConverter.taskFromString(epicArray);
        Epic epic1 = new Epic("Epic", "Desc");
        epic1.setId(1);

        assertEquals(epic1, actual1, "Эпики не совпадают");
        assertEquals("Epic", actual1.getTitle());
        assertEquals(Status.NEW, actual1.getStatus());
        assertNull(actual1.getDurationMinute());
    }

    @Test
    public void taskFromString_FromStringToSubtask() {
        String[] subtaskArray = "2,SUBTASK,Subtask,NEW,Desc,1673427600000,45,1".split(",");
        BaseTask actual = TaskConverter.taskFromString(subtaskArray);
        Epic epic = new Epic("Epic", "Desc");
        epic.setId(1);
        Subtask subtask = new Subtask("Subtask", "Desc", DATETIME_FOR_TEST, 45);
        subtask.setId(2);
        subtask.setEpicId(1);

        assertEquals(subtask, actual, "Подзадачи не совпадают");
        assertEquals("Subtask", actual.getTitle());
        assertEquals(Status.NEW, actual.getStatus());
        assertEquals(45, actual.getDurationMinute());
        assertEquals(DateTimeConverter.fromInstantToString(Instant.ofEpochMilli(1673427600000L)),
                DateTimeConverter.fromInstantToString(actual.getStartTime()));
    }

    @Test
    public void historyToString_ReturnString() {
        Task task = new Task("Task", "Desc");
        task.setId(1);
        Epic epic = new Epic("Epic", "Desc");
        epic.setId(2);
        Subtask subtask = new Subtask("Subtask", "Desc");
        subtask.setId(3);
        subtask.setEpicId(2);

        List<BaseTask> list = new ArrayList<>() {{
            add(task);
            add(epic);
            add(subtask);
        }};
        String actual = TaskConverter.historyToString(list);
        String expected = "1,2,3";
        assertEquals(expected, actual);
    }

    @Test
    public void historyFromString_ReturnListIntager() {
        String historyString = "5,10,15";

        List<Integer> extend = new ArrayList<>() {{
            add(5);
            add(10);
            add(15);
        }};
        List<Integer> actual = TaskConverter.historyFromString(historyString);
        assertEquals(extend.size(), actual.size(), "Списки разного размера");
        assertEquals(extend.get(1), actual.get(1), "Значения не совпадают");
        assertEquals(15, actual.get(2), "Полученное значение не 15");
    }

    @Test
    public void historyFromString_ReturnNull_WhenAddedEmptyString() {
        String historyString = "";

        List<Integer> actual = TaskConverter.historyFromString(historyString);

        assertNotNull(actual, "Не должно быть null");
        assertEquals(0, actual.size(), "Список не пуст");
    }

}