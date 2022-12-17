package util;

import service.HistoryManager;
import service.TaskManager;
import service.impl.FileBackedTasksManager;
import service.impl.InMemoryHistoryManager;
import service.impl.InMemoryTaskManager;

import java.nio.file.Path;
import java.nio.file.Paths;

public class Managers {

    // путь к файлу
    public static final String DATA_FILE_PATH = "resources/tasks.csv";

    public static TaskManager getDefault() {
        return new InMemoryTaskManager();
    }

    //для хранения в файле
    public static TaskManager getFileStorage() {
        Path path = Paths.get(DATA_FILE_PATH);
        return FileBackedTasksManager.loadFromFile(path.toFile());
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }
}
