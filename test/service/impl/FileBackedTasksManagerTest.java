package service.impl;

import service.TaskManagerTest;

import java.nio.file.Path;
import java.nio.file.Paths;

class FileBackedTasksManagerTest extends TaskManagerTest<FileBackedTasksManager> {
    public static final String DATA_FILE_PATH = "resources/tasks.csv";

    @Override
    public FileBackedTasksManager createInstance() {
        Path path = Paths.get(DATA_FILE_PATH);
        return FileBackedTasksManager.loadFromFile(path);
    }
}