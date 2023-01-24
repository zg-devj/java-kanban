package ru.ya.practicum.zakharovg.javakanban.util;

import ru.ya.practicum.zakharovg.javakanban.service.HistoryManager;
import ru.ya.practicum.zakharovg.javakanban.service.TaskManager;
import ru.ya.practicum.zakharovg.javakanban.service.impl.FileBackedTasksManager;
import ru.ya.practicum.zakharovg.javakanban.service.impl.HttpTaskManager;
import ru.ya.practicum.zakharovg.javakanban.service.impl.InMemoryHistoryManager;
import ru.ya.practicum.zakharovg.javakanban.service.impl.InMemoryTaskManager;

import java.io.IOException;
import java.net.URI;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Managers {

    // путь к файлу
    public static final String FILE_FOR_EXAMPLE = "tasks.csv";
    public static final String DIR_FILE_RESOURCES = "resources";

    public static TaskManager getDefault() throws IOException, InterruptedException {
        return getServerStorage();
    }

    // для хранения в памяти
    public static TaskManager getInMemoryStorage() {
        return new InMemoryTaskManager();
    }

    // для хранения в файле
    public static TaskManager getFileStorage(String fileName) {
        Path path = Paths.get(DIR_FILE_RESOURCES, fileName);
        return FileBackedTasksManager.loadFromFile(path);
    }

    // для хранения на сервере
    public static TaskManager getServerStorage() throws IOException, InterruptedException {
        return new HttpTaskManager(URI.create("http://localhost:8078"));
    }

    // для хранения истории
    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }
}
