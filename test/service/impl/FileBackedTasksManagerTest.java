package service.impl;

import model.Epic;
import model.Subtask;
import model.Task;
import org.junit.jupiter.api.Test;
import service.TaskManagerTest;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FileBackedTasksManagerTest extends TaskManagerTest<FileBackedTasksManager> {
    public static final String DATA_FILE_PATH = "testresources/tasks-test.csv";

    @Override
    public FileBackedTasksManager createInstance() {
        Path path = Paths.get(DATA_FILE_PATH);
        clearTestCsvFile(path);
        return FileBackedTasksManager.loadFromFile(path);
    }

    // если указан пустой файл
    @Test
    public void loadEmptyFile() {
        Path path = Paths.get("testresources/tasks-empty.csv");
        if(!Files.exists(path)) {
            try {
                Files.createFile(path);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        FileBackedTasksManager manager = FileBackedTasksManager.loadFromFile(path);

        assertEquals(0, manager.getAllTasks().size(), "Список должен быть пуст");
        assertEquals(0, manager.getAllEpics().size(), "Список должен быть пуст");
        assertEquals(0, manager.getAllSubtasks().size(), "Список должен быть пуст");
    }

    @Test
    public void loadAndSaveEpicFromFile() {
        Path path = Paths.get("testresources/tasks-epic.csv");
        clearTestCsvFile(path);

        // сохраняем данные
        FileBackedTasksManager manager1 = FileBackedTasksManager.loadFromFile(path);
        Epic epic1 = new Epic("Epic", "Epic Description");
        final int epicId1 = manager1.addEpic(epic1);

        // восстанавливаем данные
        FileBackedTasksManager manager2 = FileBackedTasksManager.loadFromFile(path);
        final Epic epic2 = manager2.getEpic(epicId1);

        assertEquals(epic1, epic2);
    }

    @Test
    public void checkEmptyHistory() {
        Path path = Paths.get("testresources/tasks-history.csv");
        clearTestCsvFile(path);

        // сохраняем данные
        FileBackedTasksManager manager1 = FileBackedTasksManager.loadFromFile(path);
        final Task task = new Task("Task", "Task Description");
        final int taskId =  manager1.addTask(task);
        final Epic epic = new Epic("Epic", "Epic Description");
        final int epicId = manager1.addEpic(epic);
        final Subtask subtask = new Subtask(epicId, "Subtask", "Subtask Description");
        final int subtaskId = manager1.addSubtask(subtask);

        // восстанавливаем данные
        FileBackedTasksManager manager2 = FileBackedTasksManager.loadFromFile(path);
        assertEquals(0, manager2.getHistory().size());
    }

    @Test
    public void loadAndSaveFullDataWithHistory() {
        Path path = Paths.get("testresources/tasks-full.csv");
        clearTestCsvFile(path);

        // сохраняем данные
        FileBackedTasksManager manager1 = FileBackedTasksManager.loadFromFile(path);
        final Task task = new Task("Task", "Task Description");
        final int taskId =  manager1.addTask(task);
        final Epic epic = new Epic("Epic", "Epic Description");
        final int epicId = manager1.addEpic(epic);
        final Subtask subtask = new Subtask(epicId, "Subtask", "Subtask Description");
        final int subtaskId = manager1.addSubtask(subtask);

        manager1.getTask(taskId);
        manager1.getEpic(epicId);
        manager1.getSubtask(subtaskId);

        // восстанавливаем данные
        FileBackedTasksManager manager2 = FileBackedTasksManager.loadFromFile(path);
        assertEquals(3, manager2.getHistory().size());
    }

    // Удаляем и создаем файл заново
    private void clearTestCsvFile(Path path) {
        try {
            Files.deleteIfExists(path);
            Files.createFile(path);
        } catch (IOException ex) {
            throw new RuntimeException();
        }
    }
}