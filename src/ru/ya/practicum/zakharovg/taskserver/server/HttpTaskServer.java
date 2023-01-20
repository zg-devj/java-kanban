package ru.ya.practicum.zakharovg.taskserver.server;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpServer;
import ru.ya.practicum.zakharovg.javakanban.service.TaskManager;
import ru.ya.practicum.zakharovg.javakanban.util.Managers;
import ru.ya.practicum.zakharovg.taskserver.server.handlers.*;

import java.io.IOException;
import java.net.InetSocketAddress;

public class HttpTaskServer {
    private static final int PORT = 8080;

    private TaskManager manager;
    private Gson gson;
    private HttpServer httpServer;

    public HttpTaskServer() {
        this(Managers.getFileStorage("tasks.csv"));
    }

    public HttpTaskServer(TaskManager taskManager) {
        this.manager = taskManager;
        this.gson = new GsonBuilder().serializeNulls().create();
        try {
            httpServer = HttpServer.create();
            httpServer.bind(new InetSocketAddress(PORT), 0);

            httpServer.createContext("/api/tasks", new TasksHandler(manager, gson));
            httpServer.createContext("/api/tasks/task", new TaskHandler(manager, gson));
            httpServer.createContext("/api/tasks/subtask", new SubtaskHandler(manager, gson));
            httpServer.createContext("/api/tasks/epic", new EpicHandler(manager, gson));
            httpServer.createContext("/api/tasks/history", new HistoryHandler(manager, gson));
            httpServer.createContext("/api/tasks/subtask/epic", new SubtaskEpicHandler(manager, gson));
        } catch (IOException e) {
            System.out.println("Ошибка при создании сервера: " + e.getMessage());
        }
    }

    public void start() {
        httpServer.start();
        System.out.println("HTTP-сервер запущен на " + PORT + " порту!");
    }
}
