package service;

import model.Status;
import model.Subtask;
import model.Task;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public abstract class TaskManagerTest<T extends TaskManager> {
    public abstract T createInstance();

    //region Task
    @Test
    public void addTaskAndGetTask() {
        TaskManager taskManager = createInstance();
        Task task = new Task("Task", "Description1");
        final int taskId = taskManager.addTask(task);

        final Task savedTask = taskManager.getTask(taskId);

        assertNotNull(savedTask, "Задача не найдена.");
        assertEquals(task, savedTask, "Задачи не совпадают.");

        final List<Task> tasks = taskManager.getAllTasks();

        assertNotNull(tasks, "Задачи на возвращаются.");
        assertEquals(1, tasks.size(), "Неверное количество задач.");
        assertEquals(task, tasks.get(0), "Задачи не совпадают.");
    }

    @Test
    public void updateTask() {
        TaskManager taskManager = createInstance();
        Task task = new Task("Task", "Description");
        final int taskId = taskManager.addTask(task);
        final Task savedTask = taskManager.getTask(taskId);

        assertNotNull(savedTask, "Задача не найдена.");
        assertEquals(task, savedTask, "Задачи не совпадают.");

        savedTask.setTitle("NewTitle");
        savedTask.setDescriptions("NewDescription");
        savedTask.setStatus(Status.IN_PROGRESS);

        taskManager.updateTask(savedTask);

        final Task updatedTask = taskManager.getTask(taskId);

        assertEquals("NewTitle", updatedTask.getTitle(), "Название не изменилось");
        assertEquals("NewDescription", updatedTask.getDescriptions(), "Описание не изменилось");
        assertEquals(Status.IN_PROGRESS, updatedTask.getStatus(), "Статус не изменилося");
    }

    @Test
    public void deleteTask() {
        TaskManager taskManager = createInstance();
        Task task = new Task("Task", "Description");
        final int taskId = taskManager.addTask(task);
        final Task savedTask = taskManager.getTask(taskId);

        assertNotNull(savedTask, "Задача не найдена.");
        assertEquals(task, savedTask, "Задачи не совпадают.");

        taskManager.deleteTask(savedTask.getId());

        assertNull(taskManager.getTask(savedTask.getId()), "task не был удален");
    }

    @Test
    public void deleteAllTasks() {
        TaskManager taskManager = createInstance();
        Task task1 = new Task("Task", "Description");
        Task task2 = new Task("Task2", "Description2");

        final int taskId1 = taskManager.addTask(task1);
        final int taskId2 = taskManager.addTask(task2);

        final List<Task> tasks1 = taskManager.getAllTasks();
        assertEquals(2, tasks1.size(), "не верное количество тасков");

        taskManager.deleteAllTasks();
        final List<Task> tasks2 = taskManager.getAllTasks();
        assertEquals(0, tasks2.size(), "таски не удалены");
    }

    // Проверка изменения статуса заказа
    @Test
    public void checkTaskStatusesChange() {
        TaskManager taskManager = createInstance();
        Task task = new Task("Task", "Description");
        final int taskId = taskManager.addTask(task);

        final Task savedTask = taskManager.getTask(taskId);
        assertEquals(Status.NEW, savedTask.getStatus(), "Статус не NEW");

        savedTask.setStatus(Status.IN_PROGRESS);
        assertEquals(Status.IN_PROGRESS, savedTask.getStatus(), "Статус не IN_PROGRESS");

        savedTask.setStatus(Status.DONE);
        assertEquals(Status.DONE, savedTask.getStatus(), "Статус не DONE");
    }

    // проверка не существующего таска
    @Test
    public void getANonExistentTask() {
        TaskManager taskManager = createInstance();

        Task taskNull = taskManager.getTask(10);
        assertNull(taskNull, "Таск с id 10 не должен существовать");
    }

    @Test
    public void getAllNonExistentTasks() {
        TaskManager taskManager = createInstance();

        List<Task> expected = new ArrayList<>();
        List<Task> actual = taskManager.getAllTasks();

        assertEquals(expected, actual, "Таски не пусты");
    }

    @Test
    public void updateANonExistentTask() {
        TaskManager taskManager = createInstance();
        Task task1 = new Task("Task1", "Description1");
        final int taskId1 = taskManager.addTask(task1);
        Task task2 = new Task("Task2", "Description2");
        final int taskId2 = taskManager.addTask(task2);

        Task taskForUpdate = new Task("NewTask", "NewTask");
        int id = taskForUpdate.getId();
        assertEquals(0, id, "id не должно быть отличным от 0");

        taskManager.updateTask(taskForUpdate);
        Task taskUpdated = taskManager.getTask(id);
        assertNull(taskUpdated, "Таск не должен существовать");
    }
    //endregion

    //region subtask

    // endregion
}