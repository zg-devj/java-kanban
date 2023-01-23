package ru.ya.practicum.zakharovg.taskserver.server.handler;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import ru.ya.practicum.zakharovg.javakanban.service.TaskManager;
import ru.ya.practicum.zakharovg.taskserver.util.HelperServer;

import java.io.IOException;
import java.net.URI;

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
        String method = exchange.getRequestMethod();
        URI uri = exchange.getRequestURI();
        String queryString = uri.getQuery();
        if (method.equals("GET")) {
            if (queryString != null) {
                // возвращаем подзадачи по эпику
                actionToGetSubtasksByEpic(exchange, queryString);
            } else {
                // если отсутствует строка запроса, запрос не корректен
                HelperServer.responseCode400(exchange, gson);
            }
        } else {
            // если запрос не GET
            HelperServer.responseCode405(exchange, gson);
        }
        exchange.close();
    }

    private void actionToGetSubtasksByEpic(HttpExchange exchange, String queryString) throws IOException {
        String[] queries = queryString.split("&");
        Integer id = HelperServer.getIdFromQueries(queries);
        if (id >= 0) {
            if (manager.getEpic(id) != null) {
                // ответ 200
                String response = gson.toJson(manager.getSubtasksByEpicId(id));
                HelperServer.responseCode200(exchange, response);
            } else {
                // если эпика с запрашиваемым id не существует
                HelperServer.responseCode404(exchange, gson, "Задачи с id " + id + " не найдено.");
            }
        } else {
            // если id не корректен
            HelperServer.responseCode400(exchange, gson);
        }
    }
}
