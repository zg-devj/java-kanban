package ru.ya.practicum.zakharovg.taskserver.server.handler;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import ru.ya.practicum.zakharovg.javakanban.service.TaskManager;
import ru.ya.practicum.zakharovg.taskserver.util.HelperServer;

import java.io.IOException;
import java.net.URI;

public class HistoryHandler implements HttpHandler {

    TaskManager manager;
    Gson gson;

    public HistoryHandler(TaskManager manager, Gson gson) {
        this.manager = manager;
        this.gson = gson;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        System.out.println("Запрос истории");
        String method = exchange.getRequestMethod();
        if (method.equals("GET"))   {
            String response = gson.toJson(manager.getHistory());
            HelperServer.responseCode200(exchange, response);
        } else {
            // если запрос не GET
            HelperServer.responseCode405(exchange, gson);
        }
        exchange.close();
    }
}
