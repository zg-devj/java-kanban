package service.impl;

import service.TaskManagerTest;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileBackedTasksManagerTest extends TaskManagerTest<FileBackedTasksManager> {
    public static final String DATA_FILE_PATH = "testresources/tasks-test.csv";

    @Override
    public FileBackedTasksManager createInstance() {
        Path path = Paths.get(DATA_FILE_PATH);
        clearTestCsvFile(path);
        return FileBackedTasksManager.loadFromFile(path);
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