package ru.ya.practicum.zakharovg.taskserver.server.handler;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import ru.ya.practicum.zakharovg.javakanban.exceptions.OutOfTimeIntervalException;
import ru.ya.practicum.zakharovg.javakanban.model.Subtask;
import ru.ya.practicum.zakharovg.javakanban.service.TaskManager;
import ru.ya.practicum.zakharovg.taskserver.util.HelperServer;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;

public class SubtaskHandler implements HttpHandler {

    TaskManager manager;
    Gson gson;

    public SubtaskHandler(TaskManager manager, Gson gson) {
        this.manager = manager;
        this.gson = gson;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        System.out.println("Запрос подзадач");
        String method = exchange.getRequestMethod();
        URI uri = exchange.getRequestURI();
        String queryString = uri.getQuery();
        switch (method) {
            case "GET":
                if (queryString != null) {
                    // вернуть одну подзадачу
                    actionToGetSubtask(exchange, queryString);
                } else {
                    // вернуть все подзадачи
                    HelperServer.responseCode200(exchange, gson.toJson(manager.getAllSubtasks()));
                }
                break;
            case "POST":
                // добавляем подзадачу
                actionToPostSubtask(exchange);
                break;
            case "PUT":
                // обновляем подзадачу
                actionToPutSubtask(exchange);
                break;
            case "DELETE":
                if (queryString != null) {
                    // удаляем задачу по id
                    actionToDeleteSubtaskOne(exchange, queryString);
                } else {
                    // все задачи
                    actionToDeleteSubtaskAll(exchange);
                }
                break;
            default:
                HelperServer.responseCode405(exchange, gson);
        }
        exchange.close();
    }

    private void actionToGetSubtask(HttpExchange exchange, String queryString) throws IOException {
        String[] queries = queryString.split("&");
        Integer id = HelperServer.getIdFromQueries(queries);
        if (id >= 0) {
            Subtask subtask = manager.getSubtask(id);
            if (subtask != null) {
                HelperServer.responseCode200(exchange, gson.toJson(subtask));
            } else {
                HelperServer.responseCode404(exchange, gson, "Задачи с id " + id + " не найдено.");
            }
        } else {
            HelperServer.responseCode400(exchange, gson);
        }
    }

    private void actionToPostSubtask(HttpExchange exchange) throws IOException {
        InputStream inputStreamPost = exchange.getRequestBody();
        String bodyPost = new String(inputStreamPost.readAllBytes(), HelperServer.DEFAULT_CHARSET);
        Subtask subtaskPost = gson.fromJson(bodyPost, Subtask.class);
        if (subtaskPost != null && subtaskPost.getEpicId() > 0) {
            try {
                manager.addSubtask(subtaskPost.getEpicId(), subtaskPost);
            } catch (OutOfTimeIntervalException e) {
                HelperServer.responseCode422(exchange, gson, e.getMessage());
            }
            HelperServer.responseCode201(exchange, gson, "Задача добавлена.");
        }
    }

    private void actionToPutSubtask(HttpExchange exchange) throws IOException {
        InputStream inputStreamPut = exchange.getRequestBody();
        String bodyPut = new String(inputStreamPut.readAllBytes(), HelperServer.DEFAULT_CHARSET);
        Subtask subtaskPut = gson.fromJson(bodyPut, Subtask.class);
        if (manager.containsSubtask(subtaskPut.getId())) {
            try {
                manager.updateSubtask(subtaskPut);
            } catch (OutOfTimeIntervalException e) {
                HelperServer.responseCode422(exchange, gson, e.getMessage());
            }
            HelperServer.responseCode204(exchange);
        } else {
            HelperServer.responseCode404(exchange, gson, "Задачи с id " + subtaskPut.getId() + " не найдено.");
        }
    }

    private void actionToDeleteSubtaskOne(HttpExchange exchange, String queryString) throws IOException {
        String[] queries = queryString.split("&");
        int id = HelperServer.getIdFromQueries(queries);
        if (id >= 0) {
            if (manager.containsSubtask(id)) {
                manager.deleteSubtask(id);
                HelperServer.responseCode204(exchange);
            } else {
                HelperServer.responseCode404(exchange, gson, "Задачи с id " + id + " не найдено.");
            }
        } else {
            HelperServer.responseCode400(exchange, gson);
        }
    }

    private void actionToDeleteSubtaskAll(HttpExchange exchange) throws IOException {
        if (manager.getAllSubtasks().size() > 0) {
            manager.deleteAllSubtasks();
            HelperServer.responseCode204(exchange);
        } else {
            HelperServer.responseCode404(exchange, gson, "Задач не найдено.");
        }
    }
}
