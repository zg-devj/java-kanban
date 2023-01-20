package ru.ya.practicum.zakharovg.taskserver.server.handlers;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import ru.ya.practicum.zakharovg.javakanban.service.TaskManager;
import ru.ya.practicum.zakharovg.taskserver.util.HelperServer;

import java.io.IOException;

public class SubtaskEpicHandler implements HttpHandler {

    TaskManager manager;
    Gson gson;

    public SubtaskEpicHandler(TaskManager manager, Gson gson) {
        this.manager = manager;
        this.gson = gson;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        System.out.println("Запрос подзадач по эпику");
        HelperServer.responseCode200(exchange, "Запрос подзадач по эпику");
    }
}
