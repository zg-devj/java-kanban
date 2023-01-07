package service;

import model.Epic;
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
        final Task task = new Task("Task", "Description");
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

    @Test
    public void deleteTask() {
        TaskManager taskManager = createInstance();
        final Task task = new Task("Task", "Description");
        final int taskId = taskManager.addTask(task);
        final Task savedTask = taskManager.getTask(taskId);

        taskManager.deleteTask(savedTask.getId());

        assertNull(taskManager.getTask(taskId), "Задача не была удалена");
    }

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

        final Task taskNull = taskManager.getTask(1);
        assertNull(taskNull, "Задача с id 1 не должна существовать");
    }

    @Test
    public void getAllNonExistentTasks() {
        TaskManager taskManager = createInstance();

        final List<Task> expected = new ArrayList<>();
        final List<Task> actual = taskManager.getAllTasks();

        assertEquals(expected, actual, "Задача не пуста");
    }

    @Test
    public void updateANonExistentTask() {
        TaskManager taskManager = createInstance();
        final Task task = new Task("Task", "Description");
        taskManager.addTask(task);

        final Task taskForUpdate = new Task("NewTask", "NewDescription");
        int id = taskForUpdate.getId();
        assertEquals(0, id, "id не должно быть отличным от 0");

        taskManager.updateTask(taskForUpdate);
        final Task taskUpdated = taskManager.getTask(id);
        assertNull(taskUpdated, "Задача не должна существовать");
    }
    //endregion

    //region Epic
    @Test
    public void addEpicAndGetEpic() {
        TaskManager taskManager = createInstance();
        Epic epic = new Epic("Epic", "Description");
        final int epicId = taskManager.addEpic(epic);

        final Epic savedEpic = taskManager.getEpic(epicId);

        assertNotNull(savedEpic, "Эпик не найдена.");
        assertEquals(epic, savedEpic, "Эпики не совпадают.");

        final List<Epic> epics = taskManager.getAllEpics();

        assertNotNull(epics, "Эпики на возвращаются.");
        assertEquals(1, epics.size(), "Неверное количество эпиков'.");
        assertEquals(epic, epics.get(0), "Эпики не совпадают.");
    }

    @Test
    public void updateEpic() {
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

    @Test
    public void deleteEpic() {
        TaskManager taskManager = createInstance();
        final Epic epic = new Epic("Epic", "Description");
        final int epicId = taskManager.addEpic(epic);
        final Epic savedEpic = taskManager.getEpic(epicId);

        taskManager.deleteEpic(savedEpic.getId());

        assertNull(taskManager.getEpic(epicId), "Эпик не был удален");
    }

    @Test
    public void deleteAllEpics() {
        TaskManager taskManager = createInstance();
        final Epic epic = new Epic("Epic", "Description");
        taskManager.addEpic(epic);

        final List<Epic> epics1 = taskManager.getAllEpics();
        assertEquals(1, epics1.size(), "Неверное количество эпиков");

        taskManager.deleteAllEpics();

        final List<Epic> epics2 = taskManager.getAllEpics();
        assertEquals(0, epics2.size(), "Эпики не удалены");
    }

    @Test
    public void getANonExistentEpic() {
        TaskManager taskManager = createInstance();

        final Epic epicNull = taskManager.getEpic(1);
        assertNull(epicNull, "Эпик с id 1 не должен существовать");
    }

    @Test
    public void getAllNonExistentEpics() {
        TaskManager taskManager = createInstance();

        final List<Epic> expected = new ArrayList<>();
        final List<Epic> actual = taskManager.getAllEpics();

        assertEquals(expected, actual, "Эпики не пусты");
    }

    @Test
    public void updateANonExistentEpic() {
        TaskManager taskManager = createInstance();
        final Epic epic = new Epic("Epic", "Description");
        taskManager.addEpic(epic);

        final Epic epicForUpdate = new Epic("NewEpic", "NewDescription");
        int id = epicForUpdate.getId();
        assertEquals(0, id, "id не должно быть отличным от 0");

        taskManager.updateEpic(epicForUpdate);
        final Epic epicUpdated = taskManager.getEpic(id);
        assertNull(epicUpdated, "Эпик не должен существовать");
    }
    // endregion

    //region Subtask
    @Test
    public void addSubtaskAndGetSubtask() {
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
        final List<Subtask> expected = new ArrayList<>();

        assertNotNull(subtasks, "Список не должен быть null");
        assertEquals(expected, subtasks, "Подзадачи не должны существовать");
    }

    @Test
    public void updateSubtask() {
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

    @Test
    public void deleteSubtask() {
        TaskManager taskManager = createInstance();

        final Epic epic = new Epic("Epic", "Description");
        int epicId = taskManager.addEpic(epic);

        final Subtask subtask = new Subtask(epicId, "Subtask", "Description");
        final int subtaskId = taskManager.addSubtask(subtask);

        final Subtask savedSubtask = taskManager.getSubtask(subtaskId);

        taskManager.deleteSubtask(subtaskId);

        assertNull(taskManager.getSubtask(subtaskId), "Подзадача не была удалена");
    }

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

    @Test
    public void getANonExistentSubtask() {
        TaskManager taskManager = createInstance();

        final Subtask subtaskNull = taskManager.getSubtask(1);
        assertNull(subtaskNull, "Подзадача с id 1 не должна существовать");
    }

    @Test
    public void getAllNonExistentSubtasks() {
        TaskManager taskManager = createInstance();

        final List<Subtask> expected = new ArrayList<>();
        final List<Subtask> actual = taskManager.getAllSubtasks();

        assertEquals(expected, actual, "Подзадача не пуста");
    }

    @Test
    public void updateANonExistentSubtask() {
        TaskManager taskManager = createInstance();
        final Epic epic = new Epic("Epic", "Description");
        int epicId = taskManager.addEpic(epic);

        final Subtask subtask = new Subtask(epicId, "Subtask", "Description");
        taskManager.addSubtask(subtask);

        final Subtask subtaskForUpdate = new Subtask(epicId, "NewSubtask", "NewDescription");
        int id = subtaskForUpdate.getId();
        assertEquals(0, id, "id не должно быть отличным от 0");

        taskManager.updateSubtask(subtaskForUpdate);
        final Subtask subtaskUpdated = taskManager.getSubtask(id);
        assertNull(subtaskUpdated, "Задача не должна существовать");
    }
    // endregion

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

        final  Subtask subtaskDone2 = taskManager.getSubtask(subtaskId2);
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
}