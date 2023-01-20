package ru.ya.practicum.zakharovg.taskserver.server.handlers;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import ru.ya.practicum.zakharovg.javakanban.model.Task;
import ru.ya.practicum.zakharovg.javakanban.service.TaskManager;
import ru.ya.practicum.zakharovg.taskserver.util.HelperServer;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;

public class TaskHandler implements HttpHandler {
    TaskManager manager;
    Gson gson;

    public TaskHandler(TaskManager manager, Gson gson) {
        this.manager = manager;
        this.gson = gson;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        System.out.println("Запрос задач");
        String method = exchange.getRequestMethod();
        URI uri = exchange.getRequestURI();
        String queryString = uri.getQuery();
        switch (method) {
            case "GET":
                if (queryString != null) {
                    // вернуть одну задачу
                    actionToGetTask(exchange, queryString);
                } else {
                    // вернуть все задачи
                    HelperServer.sendResponse(exchange, gson.toJson(manager.getAllTasks()));
                }
                break;
            case "POST":
                actionToPostTask(exchange);
                break;
            case "PUT":
                actionToPutTask(exchange);
                break;
            case "DELETE":
                if (queryString != null) {
                    // удаляем задачу по id
                    actionToDeleteTaskOne(exchange, queryString);
                } else {
                    // все задачи
                    actionToDeleteTaskAll(exchange);
                }
                break;
            default:
                HelperServer.responseCode400(exchange, gson);
        }
        exchange.close();
    }

    private void actionToGetTask(HttpExchange exchange, String queryString) throws IOException {
        String[] queries = queryString.split("&");
        Integer id = HelperServer.getIdFromQueries(queries);
        if (id != null && id >= 0) {
            Task task = manager.getTask(id);
            if (task != null) {
                HelperServer.sendResponse(exchange, gson.toJson(manager.getTask(id)));
            } else {
                HelperServer.responseCode404(exchange, gson, "Задачи с id=" + id + " не найдено.");
            }
        } else {
            HelperServer.responseCode400(exchange, gson);
        }
    }

    private void actionToPostTask(HttpExchange exchange) throws IOException {
        InputStream inputStreamPost = exchange.getRequestBody();
        String bodyPost = new String(inputStreamPost.readAllBytes(), HelperServer.DEFAULT_CHARSET);
        Task taskPost = gson.fromJson(bodyPost, Task.class);
        if (taskPost != null) {
            manager.addTask(taskPost);
            HelperServer.responseCode201(exchange, gson, "Задача добавлена");
        }
    }

    private void actionToPutTask(HttpExchange exchange) throws IOException {
        InputStream inputStreamPut = exchange.getRequestBody();
        String bodyPut = new String(inputStreamPut.readAllBytes(), HelperServer.DEFAULT_CHARSET);
        Task taskPut = gson.fromJson(bodyPut, Task.class);
        if (manager.getTask(taskPut.getId()) != null) {
            manager.updateTask(taskPut);
            HelperServer.sendResponse(exchange, null, 204);
        } else {
            HelperServer.responseCode404(exchange, gson, "Задачи с id=" + taskPut.getId() + " нет, нельзя обновить задачу.");
        }
    }

    private void actionToDeleteTaskOne(HttpExchange exchange, String queryString) throws IOException {
        String[] queries = queryString.split("&");
        Integer id = HelperServer.getIdFromQueries(queries);
        if (id != null && id >= 0) {
            Task taskDelete = manager.getTask(id);
            if (taskDelete != null) {
                manager.deleteTask(id);
                HelperServer.responseCode204(exchange, gson);
            } else {
                HelperServer.responseCode404(exchange, gson, "Задачи с id=" + id + " не найдено.");
            }
        } else {
            HelperServer.responseCode400(exchange, gson);
        }
    }

    private void actionToDeleteTaskAll(HttpExchange exchange) throws IOException {
        if (manager.getAllTasks().size() > 0) {
            manager.deleteAllTasks();
            HelperServer.sendResponse(exchange, null, 204);
        } else {
            HelperServer.responseCode404(exchange, gson, "Задач не найдено.");
        }
    }
}