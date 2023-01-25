package ru.ya.practicum.zakharovg.javakanban.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.ya.practicum.zakharovg.javakanban.model.BaseTask;
import ru.ya.practicum.zakharovg.javakanban.model.Task;
import ru.ya.practicum.zakharovg.javakanban.service.HistoryManager;
import ru.ya.practicum.zakharovg.javakanban.service.TaskManager;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class InMemoryHistoryManagerTest {
    public static HistoryManager historyManager;

    @BeforeEach
    public void beforeEach() {
        historyManager = new InMemoryHistoryManager();
    }

    @Test
    public void checkEmptyTaskHistory() {
        List<BaseTask> empty = new ArrayList<>();
        List<BaseTask> emptyHistory = historyManager.getHistory();
        assertEquals(empty, emptyHistory, "История должна быть пустой");
    }

    @Test
    public void checkDuplicationWithTaskManager() {
        TaskManager taskManager = new InMemoryTaskManager();
        Task task1 = new Task("Task1", "Description1");
        int taskId1 = taskManager.addTask(task1);
        Task task2 = new Task("Task2", "Description2");
        int taskId2 = taskManager.addTask(task2);
        Task task3 = new Task("Task3", "Description3");
        int taskId3 = taskManager.addTask(task3);

        taskManager.getTask(taskId1); //1
        taskManager.getTask(taskId2); //1,2
        taskManager.getTask(taskId3); //1,2,3
        taskManager.getTask(taskId1); //2,3,1
        taskManager.getTask(taskId3); //2,1,3

        assertEquals(3, taskManager.getHistory().size(),
                "Не соответствие с количеством записей в истории");
    }

    @Test
    public void checkDuplicationWithHistoryManager() {
        Task task1 = new Task("Task1", "Description1");
        task1.setId(1);
        Task task2 = new Task("Task2", "Description2");
        task2.setId(2);
        Task task3 = new Task("Task3", "Description3");
        task3.setId(3);

        historyManager.add(task1);
        historyManager.add(task2);
        historyManager.add(task3);
        historyManager.add(task1);
        historyManager.add(task3);

        assertEquals(3, historyManager.getHistory().size(),
                "Не соответствие с количеством записей в истории");

        Integer[] ids = historyManager.getHistory().stream()
                .map(BaseTask::getId)
                .collect(Collectors.toList()).toArray(new Integer[0]);

        assertArrayEquals(new Integer[]{3, 1, 2}, ids, "Результаты не совпадают");
    }

    @Test
    public void deletionFromHistoryBeginningMiddleEnd() {
        Task task1 = new Task("Task1", "Description1");
        task1.setId(1);
        Task task2 = new Task("Task2", "Description2");
        task2.setId(2);
        Task task3 = new Task("Task3", "Description3");
        task3.setId(3);
        Task task4 = new Task("Task4", "Description4");
        task4.setId(4);
        Task task5 = new Task("Task5", "Description5");
        task5.setId(5);

        historyManager.add(task1);
        historyManager.add(task2);
        historyManager.add(task3);
        historyManager.add(task4);
        historyManager.add(task5);

        Integer[] ids = historyManager.getHistory().stream()
                .map(BaseTask::getId)
                .collect(Collectors.toList()).toArray(new Integer[0]);

        assertArrayEquals(new Integer[]{5, 4, 3, 2, 1}, ids, "Результаты не совпадают");

        // удаление с начала
        historyManager.remove(task1.getId());
        Integer[] idsBegin = historyManager.getHistory().stream()
                .map(BaseTask::getId)
                .collect(Collectors.toList()).toArray(new Integer[0]);
        assertArrayEquals(new Integer[]{5, 4, 3, 2}, idsBegin, "не удален с начала");

        // удаление с конца
        historyManager.remove(task5.getId());
        Integer[] idsEnd = historyManager.getHistory().stream()
                .map(BaseTask::getId)
                .collect(Collectors.toList()).toArray(new Integer[0]);
        assertArrayEquals(new Integer[]{4, 3, 2}, idsEnd, "не удален с конца");


        // удаление с середины
        historyManager.remove(task3.getId());
        Integer[] idsMiddle = historyManager.getHistory().stream()
                .map(BaseTask::getId)
                .collect(Collectors.toList()).toArray(new Integer[0]);
        assertArrayEquals(new Integer[]{4, 2}, idsMiddle, "не удален с середины");
    }

}