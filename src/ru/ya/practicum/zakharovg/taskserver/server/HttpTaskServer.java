package ru.ya.practicum.zakharovg.taskserver.server;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpServer;
import ru.ya.practicum.zakharovg.javakanban.model.BaseTask;
import ru.ya.practicum.zakharovg.javakanban.model.Epic;
import ru.ya.practicum.zakharovg.javakanban.model.Subtask;
import ru.ya.practicum.zakharovg.javakanban.model.Task;
import ru.ya.practicum.zakharovg.javakanban.service.TaskManager;
import ru.ya.practicum.zakharovg.javakanban.util.Managers;
import ru.ya.practicum.zakharovg.taskserver.server.handler.*;
import ru.ya.practicum.zakharovg.taskserver.util.BaseTaskDeserializer;

import java.io.IOException;
import java.net.InetSocketAddress;

public class HttpTaskServer {
    private static final int PORT = 8080;

    private TaskManager manager;
    private Gson gson;
    private HttpServer httpServer;

    public HttpTaskServer() throws IOException, InterruptedException {
        this(Managers.getDefault());
    }

    public HttpTaskServer(TaskManager taskManager) throws IOException {
        this.manager = taskManager;

        BaseTaskDeserializer deserializer = new BaseTaskDeserializer("type");
        deserializer.registerBarnType("Task", Task.class);
        deserializer.registerBarnType("Subtask", Subtask.class);
        deserializer.registerBarnType("Epic", Epic.class);

        this.gson = new GsonBuilder()
                .registerTypeAdapter(BaseTask.class, deserializer)
                .create();

        httpServer = HttpServer.create();
        httpServer.bind(new InetSocketAddress(PORT), 0);

        httpServer.createContext("/api/tasks", new TasksHandler(manager, gson));
        httpServer.createContext("/api/tasks/task", new TaskHandler(manager, gson));
        httpServer.createContext("/api/tasks/subtask", new SubtaskHandler(manager, gson));
        httpServer.createContext("/api/tasks/epic", new EpicHandler(manager, gson));
        httpServer.createContext("/api/tasks/history", new HistoryHandler(manager, gson));
        httpServer.createContext("/api/tasks/subtask/epic", new SubtaskEpicHandler(manager, gson));

    }

    public void start() {
        httpServer.start();
        System.out.println("HTTP-сервер запущен на " + PORT + " порту!");
    }

    public void stop() {
        httpServer.stop(0);
        System.out.println("HTTP-сервер остановлен.");
    }
}
