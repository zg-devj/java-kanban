package ru.ya.practicum.zakharovg.taskserver.server.handler;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import ru.ya.practicum.zakharovg.javakanban.service.TaskManager;
import ru.ya.practicum.zakharovg.taskserver.util.HelperServer;

import java.io.IOException;

public class TasksHandler implements HttpHandler {

    TaskManager manager;
    Gson gson;

    public TasksHandler(TaskManager manager, Gson gson) {
        this.manager = manager;
        this.gson = gson;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        System.out.println("Запрос отсортированных задач и подзадач");
        String method = exchange.getRequestMethod();

        if (method.equals("GET")) {
            // возвращаем отсортированный список или пустой список
            String response = gson.toJson(manager.getPrioritizedTasks());
            HelperServer.responseCode200(exchange, response);
        } else {
            // если запрос не GET
            HelperServer.responseCode405(exchange, gson);
        }
        exchange.close();
    }
}
