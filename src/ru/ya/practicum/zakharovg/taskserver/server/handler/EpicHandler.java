package ru.ya.practicum.zakharovg.taskserver.server.handler;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import ru.ya.practicum.zakharovg.javakanban.model.Epic;
import ru.ya.practicum.zakharovg.javakanban.service.TaskManager;
import ru.ya.practicum.zakharovg.taskserver.util.HelperServer;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;

public class EpicHandler implements HttpHandler {

    TaskManager manager;
    Gson gson;

    public EpicHandler(TaskManager manager, Gson gson) {
        this.manager = manager;
        this.gson = gson;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        System.out.println("Запрос эпиков");
        String method = exchange.getRequestMethod();
        URI uri = exchange.getRequestURI();
        String queryString = uri.getQuery();
        switch (method) {
            case "GET":
                if (queryString != null) {
                    // вернуть одну задачу
                    actionToGetEpic(exchange, queryString);
                } else {
                    // вернуть все задачи
                    HelperServer.responseCode200(exchange, gson.toJson(manager.getAllEpics()));
                }
                break;
            case "POST":
                // добавляем эпик
                actionToPostEpic(exchange);
                break;
            case "PUT":
                // обновляем эпик
                actionToPutEpic(exchange);
                break;
            case "DELETE":
                if (queryString != null) {
                    // удаляем эпик по id
                    actionToDeleteEpicOne(exchange, queryString);
                } else {
                    // все эпики
                    actionToDeleteEpicAll(exchange);
                }
                break;
            default:
                HelperServer.responseCode405(exchange, gson);
        }
        exchange.close();
    }

    private void actionToGetEpic(HttpExchange exchange, String queryString) throws IOException {
        String[] queries = queryString.split("&");
        Integer id = HelperServer.getIdFromQueries(queries);
        if (id >= 0) {
            Epic epic = manager.getEpic(id);
            if (epic != null) {
                HelperServer.responseCode200(exchange, gson.toJson(epic));
            } else {
                HelperServer.responseCode404(exchange, gson, "Задачи с id " + id + " не найдено.");
            }
        } else {
            HelperServer.responseCode400(exchange, gson);
        }
    }

    private void actionToPostEpic(HttpExchange exchange) throws IOException {
        InputStream inputStreamPost = exchange.getRequestBody();
        String bodyPost = new String(inputStreamPost.readAllBytes(), HelperServer.DEFAULT_CHARSET);
        Epic epicPost = gson.fromJson(bodyPost, Epic.class);
        if (epicPost != null) {
            manager.addEpic(epicPost);
            HelperServer.responseCode201(exchange, gson, "Задача добавлена.");
        }
    }

    private void actionToPutEpic(HttpExchange exchange) throws IOException {
        InputStream inputStreamPut = exchange.getRequestBody();
        String bodyPut = new String(inputStreamPut.readAllBytes(), HelperServer.DEFAULT_CHARSET);
        Epic epicPut = gson.fromJson(bodyPut, Epic.class);
        if (manager.containsEpic(epicPut.getId())) {
            manager.updateEpic(epicPut);
            HelperServer.responseCode204(exchange);
        } else {
            HelperServer.responseCode404(exchange, gson, "Задачи с id " + epicPut.getId() + " не найдено.");
        }
    }

    private void actionToDeleteEpicOne(HttpExchange exchange, String queryString) throws IOException {
        String[] queries = queryString.split("&");
        int id = HelperServer.getIdFromQueries(queries);
        if (id >= 0) {
            if(manager.containsEpic(id)) {
                manager.deleteEpic(id);
                HelperServer.responseCode204(exchange);
            } else {
                HelperServer.responseCode404(exchange, gson, "Задачи с id " + id + " не найдено.");
            }
        } else {
            HelperServer.responseCode400(exchange, gson);
        }
    }

    private void actionToDeleteEpicAll(HttpExchange exchange) throws IOException {
        if (manager.getAllEpics().size() > 0) {
            manager.deleteAllEpics();
            HelperServer.responseCode204(exchange);
        } else {
            HelperServer.responseCode404(exchange, gson, "Задач не найдено.");
        }
    }
}
