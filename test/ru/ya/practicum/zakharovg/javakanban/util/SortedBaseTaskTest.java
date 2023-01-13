package ru.ya.practicum.zakharovg.javakanban.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.ya.practicum.zakharovg.javakanban.model.Subtask;
import ru.ya.practicum.zakharovg.javakanban.model.Task;

import static org.junit.jupiter.api.Assertions.*;

class SortedBaseTaskTest {
    SortedBaseTask sortedBaseTask;

    @BeforeEach
    void beforeEach() {
        sortedBaseTask = new SortedBaseTask();
    }

    @Test
    void add_ShouldBe2Element() {
        Task task1 = new Task("Task", "Desc");
        task1.setId(1);

        Task task2 = new Task("Task", "Desc", "10.01.2023 09:00", 10);
        task2.setId(2);

        sortedBaseTask.add(task1);
        sortedBaseTask.add(task2);

        assertEquals(2, sortedBaseTask.getList().size(), "Не 2 задачи в списке");
    }

    @Test
    void remove_ShouldBe1ElementAfterDelete1_Added2Element() {
        Task task1 = new Task("Task", "Desc");
        task1.setId(1);

        Task task2 = new Task("Task", "Desc", "10.01.2023 09:00", 10);
        task2.setId(2);

        sortedBaseTask.add(task1);
        sortedBaseTask.add(task2);

        assertEquals(2, sortedBaseTask.getList().size(), "Не 2 задачи в списке");

        sortedBaseTask.remove(task1);

        assertEquals(1, sortedBaseTask.getList().size()
                , "Количество задач не соответствует ожиданию");
    }

    @Test
    void getList() {
        Task task1 = new Task("Task", "Desc", "10.01.2023 09:00", 10);
        task1.setId(1);

        Subtask subtask2 = new Subtask("Subtask", "Desc", "10.01.2023 12:00", 60);
        subtask2.setId(2);
        subtask2.setEpicId(1);

        sortedBaseTask.add(task1);
        sortedBaseTask.add(subtask2);

        assertEquals(2, sortedBaseTask.getList().size(), "Не 2 задачи в списке");
        assertEquals(task1, sortedBaseTask.getList().first(), "Задачи не совпадают");
    }

    //region Тесты валидации
    @Test
    void validate_ReturnTrue_WhenAddIn08_00_Dur_30() {
        Task task1 = new Task("Task", "Desc", "10.01.2023 09:00", 30);
        task1.setId(1);

        Task task2 = new Task("Task", "Desc", "10.01.2023 12:00", 30);
        task2.setId(2);

        sortedBaseTask.add(task1);
        sortedBaseTask.add(task2);

        Task taskAdded = new Task("TaskAdded", "Desc", "10.01.2023 08:00", 30);
        taskAdded.setId(3);

        assertTrue(sortedBaseTask.validate(taskAdded));
    }

    @Test
    void validate_ReturnFalse_WhenAddIn08_45_Dur_30() {
        Task task1 = new Task("Task", "Desc", "10.01.2023 09:00", 30);
        task1.setId(1);

        Task task2 = new Task("Task", "Desc", "10.01.2023 12:00", 30);
        task2.setId(2);

        sortedBaseTask.add(task1);
        sortedBaseTask.add(task2);

        Task taskAdded = new Task("TaskAdded", "Desc", "10.01.2023 08:45", 30);
        taskAdded.setId(3);

        assertFalse(sortedBaseTask.validate(taskAdded));
    }

    @Test
    void validate_ReturnFalse_WhenAddIn09_15_Dur_30() {
        Task task1 = new Task("Task", "Desc", "10.01.2023 09:00", 30);
        task1.setId(1);

        Task task2 = new Task("Task", "Desc", "10.01.2023 12:00", 30);
        task2.setId(2);

        sortedBaseTask.add(task1);
        sortedBaseTask.add(task2);

        Task taskAdded = new Task("TaskAdded", "Desc", "10.01.2023 09:15", 30);
        taskAdded.setId(3);

        assertFalse(sortedBaseTask.validate(taskAdded));
    }

    @Test
    void validate_ReturnTrue_WhenAddIn10_00_Dur_30() {
        Task task1 = new Task("Task", "Desc", "10.01.2023 09:00", 30);
        task1.setId(1);

        Task task2 = new Task("Task", "Desc", "10.01.2023 12:00", 30);
        task2.setId(2);

        sortedBaseTask.add(task1);
        sortedBaseTask.add(task2);

        Task taskAdded = new Task("TaskAdded", "Desc", "10.01.2023 10:00", 30);
        taskAdded.setId(3);

        assertTrue(sortedBaseTask.validate(taskAdded));
    }

    @Test
    void validate_ReturnFalse_WhenAddIn11_45_Dur_30() {
        Task task1 = new Task("Task", "Desc", "10.01.2023 09:00", 30);
        task1.setId(1);

        Task task2 = new Task("Task", "Desc", "10.01.2023 12:00", 30);
        task2.setId(2);

        sortedBaseTask.add(task1);
        sortedBaseTask.add(task2);

        Task taskAdded = new Task("TaskAdded", "Desc", "10.01.2023 11:45", 30);
        taskAdded.setId(3);

        assertFalse(sortedBaseTask.validate(taskAdded));
    }

    @Test
    void validate_ReturnFalse_WhenAddIn12_15_Dur_30() {
        Task task1 = new Task("Task", "Desc", "10.01.2023 09:00", 30);
        task1.setId(1);

        Task task2 = new Task("Task", "Desc", "10.01.2023 12:00", 30);
        task2.setId(2);

        sortedBaseTask.add(task1);
        sortedBaseTask.add(task2);

        Task taskAdded = new Task("TaskAdded", "Desc", "10.01.2023 12:15", 30);
        taskAdded.setId(3);

        assertFalse(sortedBaseTask.validate(taskAdded));
    }

    @Test
    void validate_ReturnTrue_WhenAddIn12_45_Dur_30() {
        Task task1 = new Task("Task", "Desc", "10.01.2023 09:00", 30);
        task1.setId(1);

        Task task2 = new Task("Task", "Desc", "10.01.2023 12:00", 30);
        task2.setId(2);

        sortedBaseTask.add(task1);
        sortedBaseTask.add(task2);

        Task taskAdded = new Task("TaskAdded", "Desc", "10.01.2023 12:45", 30);
        taskAdded.setId(3);

        assertTrue(sortedBaseTask.validate(taskAdded));
    }

    @Test
    void validate_ReturnTrue_WhenAddedStartTimeIsNull() {
        Task task1 = new Task("Task", "Desc", "10.01.2023 09:00", 30);
        task1.setId(1);

        Task task2 = new Task("Task", "Desc", "10.01.2023 12:00", 30);
        task2.setId(2);

        sortedBaseTask.add(task1);
        sortedBaseTask.add(task2);

        Task taskAdded = new Task("TaskAdded", "Desc");
        taskAdded.setId(3);

        assertTrue(sortedBaseTask.validate(taskAdded));
    }

    @Test
    void validate_ReturnFalse_WhenAddedEqualsExist() {
        Task task1 = new Task("Task", "Desc", "10.01.2023 09:00", 30);
        task1.setId(1);

        sortedBaseTask.add(task1);

        Task taskAdded = new Task("TaskAdded", "Desc","10.01.2023 09:00", 30);
        taskAdded.setId(3);

        assertFalse(sortedBaseTask.validate(taskAdded));
    }

    @Test
    void validate_ReturnTrue_WhenStartTimeExistIsNull(){
        Task task1 = new Task("Task", "Desc");
        task1.setId(1);

        sortedBaseTask.add(task1);

        Task taskAdded = new Task("TaskAdded", "Desc","10.01.2023 09:00", 30);
        taskAdded.setId(3);

        assertTrue(sortedBaseTask.validate(taskAdded));
    }
    //endregion
}