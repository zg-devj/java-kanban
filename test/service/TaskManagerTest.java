package service;

import exceptions.OutOfTimeIntervalException;
import model.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public abstract class TaskManagerTest<T extends TaskManager> {
    public static final String ERROR_MESSAGE = "Добавляемая задача пересекается с существующими";

    public abstract T createInstance();

    //region Task
    // С нормальном функционированием
    @Test
    public void getTaskNormal() {
        TaskManager taskManager = createInstance();
        final Task task = new Task("Task", "Description");
        final int taskId = taskManager.addTask(task);

        final Task savedTask = taskManager.getTask(taskId);

        assertNotNull(savedTask, "Задача не найдена.");
        assertEquals(task, savedTask, "Задачи не совпадают.");

        final List<Task> tasks = taskManager.getAllTasks();

        assertNotNull(tasks, "Задачи не возвращаются.");
        assertEquals(1, tasks.size(), "Неверное количество задач.");
        assertEquals(task, tasks.get(0), "Задачи не совпадают.");
    }

    // С пустым списком задач
    @Test
    public void getTaskWithEmptyList() {
        TaskManager taskManager = createInstance();
        final List<Task> tasks = taskManager.getAllTasks();
        assertNotNull(tasks, "Пустой список задач не должен быть null");
        assertEquals(0, tasks.size(), "Неверное количество задач");
        final Task task = taskManager.getTask(1);
        assertNull(task, "Задача не должна существовать.");
    }

    // С неверным id
    @Test
    public void getTaskWithWrongId() {
        TaskManager taskManager = createInstance();
        final Task task = new Task("Task", "Description");
        taskManager.addTask(task);

        final Task taskWrong = taskManager.getTask(2);
        assertNull(taskWrong, "Задача не должна существовать");
    }

    // С заполненным списком задач
    @Test
    public void getAllTasksWithTasks() {
        TaskManager taskManager = createInstance();
        final Task task1 = new Task("Task1", "Description");
        final int taskId1 = taskManager.addTask(task1);
        final Task task2 = new Task("Task2", "Description");
        final int taskId2 = taskManager.addTask(task2);

        final List<Task> tasks = taskManager.getAllTasks();
        assertEquals(2, tasks.size(), "Задач в списке должно быть 2");
    }

    // С пустым списком задач
    @Test
    public void getAllTasksWithEmptyTasks() {
        TaskManager taskManager = createInstance();
        final List<Task> tasks = taskManager.getAllTasks();
        assertNotNull(tasks, "Пустой список задач не должен быть null");
        assertEquals(0, tasks.size(), "Неверное количество задач");
    }

    // Нормальное добавление задачи
    @Test
    public void addTaskNormal() {
        TaskManager taskManager = createInstance();
        final Task task = new Task("Task", "Description");
        final int taskId = taskManager.addTask(task);

        final Task savedTask = taskManager.getTask(taskId);

        assertNotNull(savedTask, "Задача не найдена.");
        assertEquals(task, savedTask, "Задачи не совпадают.");
    }

    // С неверным (не существующем id)
    @Test
    public void addTaskWithWrongId() {
        TaskManager taskManager = createInstance();
        final Task task = new Task("Task", "Description");
        final int taskId = taskManager.addTask(task);


        final Task taskWrong = new Task("TaskWrong", "Description");
        taskWrong.setId(10);
        // при использования addTask, id изменится на 2
        final int taskIdWrong = taskManager.addTask(taskWrong);

        assertEquals(2, taskIdWrong, "Id должен быть 2");

        final Task taskCheck = taskManager.getTask(taskId);

        assertEquals("Task", taskCheck.getTitle());

        final List<Task> tasks = taskManager.getAllTasks();

        assertEquals(2, tasks.size());
    }

    // проверка задачи на пересечение
    @Test
    public void checkingTheIntersectionTaskAddAndUpdate() {
        TaskManager taskManager = createInstance();
        final Task task1 = new Task("Task1", "Description");
        task1.setStartTime("11.01.2023 12:00");
        task1.setDurationMinute(30);
        final int taskId1 = taskManager.addTask(task1);

        final Task task2 = new Task("Task2", "Description");
        task2.setStartTime("11.01.2023 11:00");
        task2.setDurationMinute(30);
        final int taskId2 = taskManager.addTask(task2);

        final Task newTask = new Task("NewTask", "Description");
        newTask.setStartTime("11.01.2023 12:15");
        newTask.setDurationMinute(30);

        // проверка на добавления
        final OutOfTimeIntervalException ex1 = assertThrows(
                OutOfTimeIntervalException.class,
                new Executable() {
                    @Override
                    public void execute() throws Throwable {
                        taskManager.addTask(newTask);
                    }
                }
        );
        assertEquals(ERROR_MESSAGE, ex1.getMessage());

        // проверка на обновления
        final Task savedTask = taskManager.getTask(taskId2);
        savedTask.setDurationMinute(120);

        final OutOfTimeIntervalException ex2 = assertThrows(
                OutOfTimeIntervalException.class,
                () -> taskManager.updateTask(savedTask)
        );
        assertEquals(ERROR_MESSAGE, ex2.getMessage());
    }

    // Нормальное поведение
    @Test
    public void updateTaskNormal() {
        TaskManager taskManager = createInstance();
        final Task task = new Task("Task", "Description");
        final int taskId = taskManager.addTask(task);
        final Task savedTask = taskManager.getTask(taskId);

        savedTask.setTitle("NewTitle");
        savedTask.setDescriptions("NewDescription");
        savedTask.setStatus(Status.IN_PROGRESS);

        taskManager.updateTask(savedTask);

        final Task updatedTask = taskManager.getTask(savedTask.getId());

        assertEquals("NewTitle", updatedTask.getTitle(), "Название не изменилось");
        assertEquals("NewDescription", updatedTask.getDescriptions(), "Описание не изменилось");
        assertEquals(Status.IN_PROGRESS, updatedTask.getStatus(), "Статус не изменился");
    }

    // Обновление с несуществующим id
    @Test
    public void updateTaskWithWrongId() {
        TaskManager taskManager = createInstance();
        final Task task = new Task("Task", "Description");
        final int taskId = taskManager.addTask(task);

        final Task taskWithWrongId = new Task("WrongTask", "WrongDescription");
        taskWithWrongId.setId(2);
        taskWithWrongId.setStatus(Status.IN_PROGRESS);

        // Задача не должна обновится, ничего не произойдет
        taskManager.updateTask(taskWithWrongId);

        assertNull(taskManager.getTask(2), "Не должно существовать задачи с id 2");
        assertEquals(1, taskManager.getAllTasks().size(), "Задача должна быть одна");
    }

    // Нормальное удаление
    @Test
    public void deleteTaskNormal() {
        TaskManager taskManager = createInstance();

        final Task task = new Task("Task", "Description");
        final int taskId = taskManager.addTask(task);

        final Task savedTask = taskManager.getTask(taskId);

        taskManager.deleteTask(savedTask.getId());

        assertNull(taskManager.getTask(taskId), "Задача не была удалена");
    }

    // Удаляем не существующую задачу
    @Test
    public void deleteTaskWithWrongId() {
        TaskManager taskManager = createInstance();
        final Task task = new Task("Task", "Description");
        final int taskId = taskManager.addTask(task);

        assertEquals(1, taskId, "id должен быть 1");

        taskManager.deleteTask(2); // Ничего не произойдет

        assertEquals(1, taskManager.getAllTasks().size(),
                "Неверное количество задач");
    }

    // Удаление всех задач
    @Test
    public void deleteAllTasks() {
        TaskManager taskManager = createInstance();
        final Task task = new Task("Task", "Description");
        taskManager.addTask(task);

        final List<Task> tasks1 = taskManager.getAllTasks();
        assertEquals(1, tasks1.size(), "Неверное количество задач");

        taskManager.deleteAllTasks();

        final List<Task> tasks2 = taskManager.getAllTasks();
        assertEquals(0, tasks2.size(), "Задачи не удалены");
    }

    // Удаление всех задач из пустого списка
    @Test
    public void deleteAllTasksWithEmptyList() {
        TaskManager taskManager = createInstance();
        final List<Task> tasks1 = taskManager.getAllTasks();
        assertEquals(0, tasks1.size(), "Неверное количество задач");

        // ничего не должно произойти
        taskManager.deleteAllTasks();

        final List<Task> tasks2 = taskManager.getAllTasks();
        assertEquals(0, tasks2.size(), "Неверное количество задач");
    }
    //endregion

    //region Subtask
    // С нормальном функционированием
    @Test
    public void getSubtaskNormal() {
        TaskManager taskManager = createInstance();
        final Epic epic = new Epic("Epic", "Description");
        int epicId = taskManager.addEpic(epic);

        final Subtask subtask = new Subtask(epicId, "Subtask", "Description");
        final int subtaskId = taskManager.addSubtask(subtask);

        final Subtask savedSubtask = taskManager.getSubtask(subtaskId);

        assertNotNull(savedSubtask, "Подзадача не найдена.");
        assertEquals(subtask, savedSubtask, "Подзадачи не совпадают.");

        final List<Subtask> subtasks = taskManager.getAllSubtasks();

        assertEquals(epicId, savedSubtask.getEpicId(), "У подзадачи неверный идентификатор эпика");
        assertNotNull(subtasks, "Подзадачи на возвращаются.");
        assertEquals(1, subtasks.size(), "Неверное количество подзадач.");
        assertEquals(subtask, subtasks.get(0), "Подзадачи не совпадают.");
    }

    // С пустым списком подзадач
    @Test
    public void getSubtaskWithEmptyList() {
        TaskManager taskManager = createInstance();
        final List<Subtask> subtasks = taskManager.getAllSubtasks();
        assertNotNull(subtasks, "Пустой список подзадач не должен быть null");
        assertEquals(0, subtasks.size(), "Неверное количество задач");
        final Subtask subtask = taskManager.getSubtask(1);
        assertNull(subtask, "Задача не должна существовать.");
    }

    // С неверным id
    @Test
    public void getSubtaskWithWrongId() {
        TaskManager taskManager = createInstance();
        final Epic epic = new Epic("Epic", "Description");
        final int epicId = taskManager.addEpic(epic);
        assertEquals(1, epicId);
        final Subtask subtask = new Subtask(epicId, "Subtask", "Description");
        final int subtaskId = taskManager.addSubtask(subtask);
        assertEquals(2, subtaskId);

        final Subtask subtaskWrong = taskManager.getSubtask(3);
        assertNull(subtaskWrong, "Задача не должна существовать");
    }

    // С заполненным списком подзадач
    @Test
    public void getAllSubtasksWithSubtasks() {
        TaskManager taskManager = createInstance();

        final Epic epic1 = new Epic("Epic", "Description");
        final int epicId1 = taskManager.addEpic(epic1);
        final Subtask subtask1 = new Subtask(epicId1, "Subtask1", "Description");
        taskManager.addSubtask(subtask1);

        final Epic epic2 = new Epic("Epic2", "Description");
        final int epicId2 = taskManager.addEpic(epic2);
        final Subtask subtask2 = new Subtask(epicId2, "Subtask2", "Description");
        taskManager.addSubtask(subtask2);

        final List<Subtask> subtasks = taskManager.getAllSubtasks();
        assertEquals(2, subtasks.size(), "Подзадач в списке должно быть 2");
    }

    // С пустым списком подзадач
    @Test
    public void getAllSubtasksWithEmptySubtasks() {
        TaskManager taskManager = createInstance();
        final List<Subtask> subtasks = taskManager.getAllSubtasks();
        assertNotNull(subtasks, "Пустой список задач не должен быть null");
        assertEquals(0, subtasks.size(), "Неверное количество задач");
    }

    @Test
    public void getSubtasksByEpicId() {
        TaskManager taskManager = createInstance();
        final Epic epic = new Epic("Epic", "Description");
        int epicId = taskManager.addEpic(epic);

        final Subtask subtask1 = new Subtask(epicId, "Subtask1", "Description");
        taskManager.addSubtask(subtask1);

        final Subtask subtask2 = new Subtask(epicId, "Subtask2", "Description");
        taskManager.addSubtask(subtask2);

        final List<Subtask> subtasks = taskManager.getSubtasksByEpicId(epicId);

        assertEquals(2, subtasks.size(), "Неверное количество подзадач.");
    }

    @Test
    public void getSubtasksNonExistentByEpicId() {
        TaskManager taskManager = createInstance();

        final List<Subtask> subtasks = taskManager.getSubtasksByEpicId(1);

        assertNotNull(subtasks, "Список не должен быть null");
        assertEquals(0, subtasks.size(), "Подзадачи не должны существовать");
    }

    // Нормальное добавление подзадачи
    @Test
    public void addSubtaskNormal() {
        TaskManager taskManager = createInstance();
        final Epic epic = new Epic("Epic", "Description");
        final int epicId = taskManager.addEpic(epic);
        final Subtask subtask = new Subtask(epicId, "Subtask1", "Description");
        final int subtaskId = taskManager.addSubtask(subtask);

        final Subtask savedSubtask = taskManager.getSubtask(subtaskId);

        assertNotNull(savedSubtask, "Задача не найдена.");
        assertEquals(subtask, savedSubtask, "Задачи не совпадают.");
        assertEquals(2, savedSubtask.getId());
    }

    // С неверным (не существующем id)
    @Test
    public void addSubtaskWithWrongId() {
        TaskManager taskManager = createInstance();
        final Epic epic = new Epic("Epic", "Description");
        final int epicId = taskManager.addEpic(epic);

        final Subtask subtask = new Subtask(epicId, "Subtask1", "Description");
        final int subtaskId = taskManager.addSubtask(subtask);

        final Subtask subtaskWrong = new Subtask(epicId, "SubtaskWrong", "Description");
        subtaskWrong.setId(10);
        // при использования addSubtask, id изменится на 3
        final int subtaskIdWrong = taskManager.addSubtask(subtaskWrong);

        assertEquals(3, subtaskIdWrong, "Id должен быть 3");

        final Subtask subtaskCheck = taskManager.getSubtask(subtaskId);

        assertEquals("Subtask1", subtaskCheck.getTitle());

        final List<Subtask> subtasks = taskManager.getAllSubtasks();

        assertEquals(2, subtasks.size());
    }

    // проверка подзадачи на пересечение
    @Test
    public void checkingTheIntersectionSubtaskAddAndUpdate() {
        TaskManager taskManager = createInstance();

        final Epic epic = new Epic("Epic", "Desc");
        final int epicId = taskManager.addEpic(epic);

        final Subtask subtask1 = new Subtask(epicId, "Subtask1", "Description");
        subtask1.setStartTime("11.01.2023 12:00");
        subtask1.setDurationMinute(30);
        final int subtaskId1 = taskManager.addSubtask(subtask1);

        final Subtask subtask2 = new Subtask(epicId, "Subtask1", "Description");
        subtask2.setStartTime("11.01.2023 11:00");
        subtask2.setDurationMinute(30);
        final int subtaskId2 = taskManager.addSubtask(subtask2);

        final Subtask newSubtask = new Subtask(epicId, "NewSubtask", "Description");
        newSubtask.setStartTime("11.01.2023 12:15");
        newSubtask.setDurationMinute(30);

        // проверка на добавления
        final OutOfTimeIntervalException ex1 = assertThrows(
                OutOfTimeIntervalException.class,
                new Executable() {
                    @Override
                    public void execute() throws Throwable {
                        taskManager.addSubtask(newSubtask);
                    }
                }
        );
        assertEquals(ERROR_MESSAGE, ex1.getMessage());

        // проверка на обновления
        final Subtask savedSubtask = taskManager.getSubtask(subtaskId2);
        savedSubtask.setDurationMinute(120);

        final OutOfTimeIntervalException ex2 = assertThrows(
                OutOfTimeIntervalException.class,
                new Executable() {
                    @Override
                    public void execute() throws Throwable {
                        taskManager.updateSubtask(savedSubtask);
                    }
                }
        );
        assertEquals(ERROR_MESSAGE, ex2.getMessage());
    }

    // Нормальное поведение
    @Test
    public void updateSubtaskNormal() {
        TaskManager taskManager = createInstance();
        final Epic epic = new Epic("Epic", "Description");
        int epicId = taskManager.addEpic(epic);

        final Subtask subtask = new Subtask(epicId, "Subtask", "Description");
        final int subtaskId = taskManager.addSubtask(subtask);

        final Subtask savedSubtask = taskManager.getSubtask(subtaskId);

        savedSubtask.setTitle("NewSubtask");
        savedSubtask.setDescriptions("NewDescription");
        savedSubtask.setStatus(Status.IN_PROGRESS);

        taskManager.updateSubtask(savedSubtask);

        final Subtask updatedTask = taskManager.getSubtask(savedSubtask.getId());

        assertEquals("NewSubtask", updatedTask.getTitle(), "Название не изменилось");
        assertEquals("NewDescription", updatedTask.getDescriptions(), "Описание не изменилось");
        assertEquals(Status.IN_PROGRESS, updatedTask.getStatus(), "Статус не изменился");
    }

    // Обновление с несуществующим id
    @Test
    public void updateSubtaskWithWrongId() {
        TaskManager taskManager = createInstance();
        final Epic epic = new Epic("Epic", "Description");
        int epicId = taskManager.addEpic(epic);

        final Subtask subtask = new Subtask(epicId, "Subtask", "Description");
        taskManager.addSubtask(subtask);

        final Subtask subtaskWithWrongId = new Subtask(epicId, "WrongSubtask",
                "WrongDescription");
        subtaskWithWrongId.setId(3);
        subtaskWithWrongId.setStatus(Status.IN_PROGRESS);

        // Подзадача не должна обновится, ничего не произойдет
        taskManager.updateSubtask(subtaskWithWrongId);

        assertNull(taskManager.getSubtask(3), "Не должно существовать задачи с id 3");
        assertEquals(1, taskManager.getAllSubtasks().size(), "Подзадача должна быть одна");
    }

    // Нормальное удаление
    @Test
    public void deleteSubtaskNormal() {
        TaskManager taskManager = createInstance();

        final Epic epic = new Epic("Epic", "Description");
        int epicId = taskManager.addEpic(epic);

        final Subtask subtask = new Subtask(epicId, "Subtask", "Description");
        final int subtaskId = taskManager.addSubtask(subtask);

        final Subtask savedSubtask = taskManager.getSubtask(subtaskId);
        int idForDelete = savedSubtask.getId();

        taskManager.deleteSubtask(idForDelete);

        assertNull(taskManager.getSubtask(idForDelete), "Подзадача не была удалена");
    }

    // Удаляем не существующую подзадачу
    @Test
    public void deleteSubtaskWithWrongId() {
        TaskManager taskManager = createInstance();
        final Epic epic = new Epic("Epic", "Description");
        int epicId = taskManager.addEpic(epic);

        final Subtask subtask = new Subtask(epicId, "Subtask", "Description");
        final int subtaskId = taskManager.addSubtask(subtask);

        assertEquals(2, subtaskId, "id должен быть 2");

        taskManager.deleteSubtask(3); // Ничего не произойдет

        assertEquals(1, taskManager.getAllSubtasks().size(),
                "Неверное количество задач");
    }

    // Удаление всех подзадач
    @Test
    public void deleteAllSubtasks() {
        TaskManager taskManager = createInstance();
        final Epic epic = new Epic("Epic", "Description");
        int epicId = taskManager.addEpic(epic);

        final Subtask subtask = new Subtask(epicId, "Subtask", "Description");
        final int subtaskId = taskManager.addSubtask(subtask);

        final List<Subtask> subtasks1 = taskManager.getAllSubtasks();
        assertEquals(1, subtasks1.size(), "Неверное количество подзадач");

        taskManager.deleteAllSubtasks();

        final List<Subtask> subtasks2 = taskManager.getAllSubtasks();
        assertEquals(0, subtasks2.size(), "Подзадачи не удалены");
    }

    // Удаление всех задач из пустого списка
    @Test
    public void deleteAllSubtasksWithEmptyList() {
        TaskManager taskManager = createInstance();
        final List<Subtask> subtasks1 = taskManager.getAllSubtasks();
        assertEquals(0, subtasks1.size(), "Неверное количество задач");

        // ничего не должно произойти
        taskManager.deleteAllSubtasks();

        final List<Subtask> subtasks2 = taskManager.getAllSubtasks();
        assertEquals(0, subtasks2.size(), "Неверное количество задач");
    }

    @Test
    public void checkSubtaskStatusesChange() {
        TaskManager taskManager = createInstance();
        final Epic epic = new Epic("Epic", "Description");
        int epicId = taskManager.addEpic(epic);

        final Subtask subtask = new Subtask(epicId, "Subtask", "Description");
        final int subtaskId = taskManager.addSubtask(subtask);

        final Subtask savedSubtask = taskManager.getSubtask(subtaskId);
        assertEquals(Status.NEW, savedSubtask.getStatus(), "Статус не NEW");

        savedSubtask.setStatus(Status.IN_PROGRESS);
        assertEquals(Status.IN_PROGRESS, savedSubtask.getStatus(), "Статус не IN_PROGRESS");

        savedSubtask.setStatus(Status.DONE);
        assertEquals(Status.DONE, savedSubtask.getStatus(), "Статус не DONE");
    }
    //endregion

    //region Epic
    // С нормальном функционированием
    @Test
    public void getEpicNormal() {
        TaskManager taskManager = createInstance();
        final Epic epic = new Epic("Epic", "Description");
        final int epicId = taskManager.addEpic(epic);

        final Epic savedEpic = taskManager.getEpic(epicId);

        assertNotNull(savedEpic, "Задача не найдена.");
        assertEquals(epic, savedEpic, "Задачи не совпадают.");

        final List<Epic> epics = taskManager.getAllEpics();

        assertNotNull(epics, "Задачи не возвращаются.");
        assertEquals(1, epics.size(), "Неверное количество задач.");
        assertEquals(epic, epics.get(0), "Задачи не совпадают.");
    }

    // С пустым списком эпиков
    @Test
    public void getEpicWithEmptyList() {
        TaskManager taskManager = createInstance();
        final List<Epic> epics = taskManager.getAllEpics();
        assertNotNull(epics, "Пустой список эпиков не должен быть null");
        assertEquals(0, epics.size(), "Неверное количество эпиков");
        final Epic epic = taskManager.getEpic(1);
        assertNull(epic, "Эпик не должен существовать.");
    }

    // С неверным id
    @Test
    public void getEpicWithWrongId() {
        TaskManager taskManager = createInstance();
        final Epic epic = new Epic("Epic", "Description");
        taskManager.addEpic(epic);

        final Epic epicWrong = taskManager.getEpic(2);
        assertNull(epicWrong, "Эпика не должно существовать");
    }

    // Нормальное добавление эпика
    @Test
    public void addEpicNormal() {
        TaskManager taskManager = createInstance();
        final Epic epic = new Epic("Epic", "Description");
        final int epicId = taskManager.addEpic(epic);

        final Epic savedEpic = taskManager.getEpic(epicId);

        assertNotNull(savedEpic, "Эпик не найден.");
        assertEquals(epic, savedEpic, "Эпики не совпадают.");
    }

    // С неверным (не существующем id)
    @Test
    public void addEpicWithWrongId() {
        TaskManager taskManager = createInstance();
        final Epic epic = new Epic("Epic", "Description");
        final int epicId = taskManager.addEpic(epic);

        final Epic epicWrong = new Epic("EpicWrong", "Description");
        epicWrong.setId(10);
        // при использования addEpic, id изменится на 2
        final int epicIdWrong = taskManager.addEpic(epicWrong);

        assertEquals(2, epicIdWrong, "Id должен быть 2");

        final Epic epicCheck = taskManager.getEpic(epicId);

        assertEquals("Epic", epicCheck.getTitle());

        final List<Epic> epics = taskManager.getAllEpics();

        assertEquals(2, epics.size());
    }

    // Нормальное поведение
    @Test
    public void updateEpicNormal() {
        TaskManager taskManager = createInstance();
        final Epic epic = new Epic("Epic", "Description");
        final int epicId = taskManager.addEpic(epic);

        final Epic savedEpic = taskManager.getEpic(epicId);

        savedEpic.setTitle("NewEpic");
        savedEpic.setDescriptions("NewDescription");

        taskManager.updateEpic(savedEpic);

        final Epic updatedEpic = taskManager.getEpic(savedEpic.getId());

        assertEquals("NewEpic", updatedEpic.getTitle(), "Название не изменилось");
        assertEquals("NewDescription", updatedEpic.getDescriptions(), "Описание не изменилось");
    }

    // Обновление с несуществующим id
    @Test
    public void updateEpicWithWrongId() {
        TaskManager taskManager = createInstance();
        final Epic epic = new Epic("Epic", "Description");
        final int epicId = taskManager.addEpic(epic);

        final Epic epicWithWrongId = new Epic("WrongEpic", "WrongDescription");
        epicWithWrongId.setId(2);

        // Задача не должна обновится, ничего не произойдет
        taskManager.updateEpic(epicWithWrongId);

        assertNull(taskManager.getEpic(2), "Не должно существовать эпика с id 2");
        assertEquals(1, taskManager.getAllEpics().size(), "Эпик должен быть один");
    }

    // Нормальное удаление
    @Test
    public void deleteEpicNormal() {
        TaskManager taskManager = createInstance();
        final Epic epic = new Epic("Epic", "Description");
        final int epicId = taskManager.addEpic(epic);

        final Subtask subtask = new Subtask(epicId, "Subtask", "Description");
        taskManager.addSubtask(subtask);

        final Epic savedEpic = taskManager.getEpic(epicId);

        taskManager.deleteEpic(savedEpic.getId());

        assertNull(taskManager.getEpic(epicId), "Эпик не был удален");
        assertEquals(0, taskManager.getAllEpics().size(), "Эпиков не должно быть");
        assertEquals(0, taskManager.getAllSubtasks().size(), "Подзадач не должно быть");
    }

    // Удаляем не существующий эпик
    @Test
    public void deleteEpicWithWrongId() {
        TaskManager taskManager = createInstance();
        final Epic epic = new Epic("Epic", "Description");
        final int epicId = taskManager.addEpic(epic);

        assertEquals(1, epicId, "id должен быть 1");

        taskManager.deleteEpic(2); // Ничего не произойдет

        assertEquals(1, taskManager.getAllEpics().size(),
                "Неверное количество эпиков");
    }

    // Удаление всех эпиков
    @Test
    public void deleteAllEpics() {
        TaskManager taskManager = createInstance();
        final Epic epic = new Epic("Epic", "Description");
        final int epicId = taskManager.addEpic(epic);
        final Subtask subtask = new Subtask(epicId, "Subtask", "Description");
        taskManager.addSubtask(subtask);

        final List<Epic> epics1 = taskManager.getAllEpics();
        assertEquals(1, epics1.size(), "Неверное количество эпиков");
        final List<Subtask> subtasks1 = taskManager.getAllSubtasks();
        assertEquals(1, subtasks1.size(), "Неверное количество подзадач");

        taskManager.deleteAllEpics();

        final List<Epic> epics2 = taskManager.getAllEpics();
        assertEquals(0, epics2.size(), "Эпики не удалены");
        final List<Subtask> subtasks2 = taskManager.getAllSubtasks();
        assertEquals(0, subtasks2.size(), "Подзадачи не удалены");
    }

    // Удаление всех эпиков из пустого списка
    @Test
    public void deleteAllEpicsWithEmptyList() {
        TaskManager taskManager = createInstance();
        final List<Epic> epics1 = taskManager.getAllEpics();
        assertEquals(0, epics1.size(), "Неверное количество эпиков");

        // ничего не должно произойти
        taskManager.deleteAllEpics();

        final List<Epic> epics2 = taskManager.getAllEpics();
        assertEquals(0, epics2.size(), "Неверное количество эпиков");
    }


    @Test
    public void checkEpicInterval() {
        TaskManager taskManager = createInstance();

        final Epic epic = new Epic("Epic", "EpicDesc");
        final int epicId = taskManager.addEpic(epic);

        final Subtask subtask1 = new Subtask(epicId, "Subtask1", "Description");
        final int subtaskId1 = taskManager.addSubtask(subtask1);

        final Epic savedEpic1 = taskManager.getEpic(epicId);

        assertNull(savedEpic1.getStartTime());
        assertNull(savedEpic1.getEndTime());
        assertEquals(0, savedEpic1.getDurationMinute());

        final Subtask subtask2 = new Subtask(epicId, "Subtask2", "Description");
        subtask2.setStartTime("11.01.2023 12:00");
        subtask2.setDurationMinute(30);
        final int subtaskId2 = taskManager.addSubtask(subtask2);

        final Epic savedEpic2 = taskManager.getEpic(epicId);

        assertEquals(1673427600000L, savedEpic2.getStartTime().toEpochMilli());
        assertEquals(30, savedEpic2.getDurationMinute());
        assertEquals(1673429400000L, savedEpic2.getEndTime().toEpochMilli());


        final Subtask subtask3 = new Subtask(epicId, "Subtask3", "Description");
        subtask3.setStartTime("11.01.2023 15:00");
        subtask3.setDurationMinute(6 * 60); // 6ч
        final int subtaskId3 = taskManager.addSubtask(subtask3);

        final Epic savedEpic3 = taskManager.getEpic(epicId);

        assertEquals(1673427600000L, savedEpic3.getStartTime().toEpochMilli());
        assertEquals(390L, savedEpic3.getDurationMinute()); // 360 + 30
        assertEquals(1673460000000L, savedEpic3.getEndTime().toEpochMilli());
    }
    //endregion

    //region Epic Status Test
    // Пустой список подзадач.
    @Test
    public void epicStatusIfEmptySubtasks() {
        TaskManager taskManager = createInstance();
        final Epic epic = new Epic("Epic", "Description");
        int epicId = taskManager.addEpic(epic);

        Epic savedEpic = taskManager.getEpic(epicId);
        assertEquals(Status.NEW, savedEpic.getStatus(), "Статус эпика без подзадач должен быть NEW");
    }

    // Все подзадачи со статусом NEW.
    @Test
    public void epicStatusAllSubtasksIsNew() {
        TaskManager taskManager = createInstance();
        final Epic epic = new Epic("Epic", "Description");
        int epicId = taskManager.addEpic(epic);

        final Subtask subtask1 = new Subtask(epicId, "Subtask1", "Description");
        taskManager.addSubtask(subtask1);

        final Epic savedEpic = taskManager.getEpic(epicId);
        assertEquals(Status.NEW, savedEpic.getStatus(), "Статус эпика c подзадачи NEW, должен быть NEW");
    }

    // Все подзадачи со статусом DONE.
    @Test
    public void epicStatusAllSubtasksIsDone() {
        TaskManager taskManager = createInstance();
        final Epic epic = new Epic("Epic", "Description");
        int epicId = taskManager.addEpic(epic);

        final Subtask subtask1 = new Subtask(epicId, "Subtask1", "Description");
        final int subtaskId1 = taskManager.addSubtask(subtask1);

        final Subtask subtaskDone1 = taskManager.getSubtask(subtaskId1);
        subtaskDone1.setStatus(Status.DONE);
        taskManager.updateSubtask(subtaskDone1);

        final Epic savedEpic = taskManager.getEpic(epicId);
        assertEquals(Status.DONE, savedEpic.getStatus(), "Статус эпика c подзадачи DONE, должен быть DONE");
    }

    // Подзадачи со статусами NEW и DONE.
    @Test
    public void epicStatusSubtaskIsNewAndDone() {
        TaskManager taskManager = createInstance();
        final Epic epic = new Epic("Epic", "Description");
        int epicId = taskManager.addEpic(epic);

        final Subtask subtask1 = new Subtask(epicId, "Subtask1", "Description");
        final int subtaskId1 = taskManager.addSubtask(subtask1);
        final Subtask subtask2 = new Subtask(epicId, "Subtask2", "Description");
        final int subtaskId2 = taskManager.addSubtask(subtask2);

        final Subtask subtaskDone1 = taskManager.getSubtask(subtaskId1);
        subtaskDone1.setStatus(Status.NEW);
        taskManager.updateSubtask(subtaskDone1);

        final Subtask subtaskDone2 = taskManager.getSubtask(subtaskId2);
        subtaskDone2.setStatus(Status.DONE);
        taskManager.updateSubtask(subtaskDone2);

        final Epic savedEpic = taskManager.getEpic(epicId);
        assertEquals(Status.IN_PROGRESS, savedEpic.getStatus(),
                "Статус эпика c подзадачами NEW и DONE, должен быть IN_PROGRESS");
    }

    // Подзадачи со статусом IN_PROGRESS.
    @Test
    public void epicStatusSubtaskIsInProgress() {
        TaskManager taskManager = createInstance();
        final Epic epic = new Epic("Epic", "Description");
        int epicId = taskManager.addEpic(epic);

        final Subtask subtask1 = new Subtask(epicId, "Subtask1", "Description");
        final int subtaskId1 = taskManager.addSubtask(subtask1);

        final Subtask subtaskDone1 = taskManager.getSubtask(subtaskId1);
        subtaskDone1.setStatus(Status.IN_PROGRESS);
        taskManager.updateSubtask(subtaskDone1);

        final Epic savedEpic = taskManager.getEpic(epicId);
        assertEquals(Status.IN_PROGRESS, savedEpic.getStatus(),
                "Статус эпика c подзадачами IN_PROGRESS , должен быть IN_PROGRESS");
    }
    //endregion

    //region Зависимости эпика и подзадачи
    @Test
    public void checkEpicForSubtask() {
        TaskManager taskManager = createInstance();
        final Epic epic = new Epic("Epic", "Description");
        final int epicId = taskManager.addEpic(epic);
        final Subtask subtask1 = new Subtask(epicId, "Subtask1", "Description");
        int subtaskId1 = taskManager.addSubtask(subtask1);

        final Subtask subtask = taskManager.getSubtask(subtaskId1);

        assertEquals(epicId, subtask.getEpicId(), "Эпики не соответствуют");
    }

    @Test
    public void checkSubtasksForEpic() {
        TaskManager taskManager = createInstance();
        final Epic epic = new Epic("Epic", "Description");
        final int epicId = taskManager.addEpic(epic);
        final Subtask subtask1 = new Subtask(epicId, "Subtask1", "Description");
        final int subtaskId1 = taskManager.addSubtask(subtask1);
        final Subtask subtask2 = new Subtask(epicId, "Subtask2", "Description");
        final int subtaskId2 = taskManager.addSubtask(subtask2);

        final Epic epicCheck = taskManager.getEpic(epicId);
        Set<Integer> subtaskIds = epicCheck.getSubtaskIds();
        assertEquals(2, subtaskIds.size(), "Не соответствие кол-ву подзадач");
        assertTrue(subtaskIds.contains(subtaskId1));
        assertTrue(subtaskIds.contains(subtaskId2));
    }
    //endregion

    //region History
    @Test
    public void checkGetEmptyHistory() {
        TaskManager taskManager = createInstance();
        List<BaseTask> historyEmpty = taskManager.getHistory();
        assertEquals(0, historyEmpty.size(), "История не пуста");
    }

    @Test
    public void checkGetNotEmptyHistory() {
        TaskManager taskManager = createInstance();

        final Task task1 = new Task("Task1", "Description");
        final int taskId1 = taskManager.addTask(task1);

        final Task task2 = new Task("Task2", "Description");
        final int taskId2 = taskManager.addTask(task2);

        taskManager.getTask(taskId1);
        taskManager.getTask(taskId2);

        List<BaseTask> historyList = taskManager.getHistory();
        assertEquals(2, historyList.size(), "в списке должно быть 2 истории");
    }
    //endregion
}