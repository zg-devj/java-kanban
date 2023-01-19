package ru.ya.practicum.zakharovg.taskserver.server;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import ru.ya.practicum.zakharovg.javakanban.model.Task;
import ru.ya.practicum.zakharovg.javakanban.service.TaskManager;
import ru.ya.practicum.zakharovg.javakanban.util.Managers;
import ru.ya.practicum.zakharovg.taskserver.model.MessageResp;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URI;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class HttpTaskServer {
    private static final String RESPONSE_CODE_400_MSG = "Вы использовали неверный запрос";
    private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;
    private static final int PORT = 8080;

    private TaskManager manager;
    private Gson gson;

    private HttpTaskServer() {
        manager = Managers.getFileStorage("tasks.csv");
        gson = new GsonBuilder().serializeNulls().create();
    }

    public void start() {
        try {
            HttpServer httpServer = HttpServer.create();
            httpServer.bind(new InetSocketAddress(PORT), 0);

            httpServer.createContext("/tasks", this::tasksHandler);
            httpServer.createContext("/tasks/task", this::taskHandler);
            httpServer.createContext("/tasks/subtask", this::subtaskHandler);
            httpServer.createContext("/tasks/epic", this::epicHandler);
            httpServer.createContext("/tasks/history", this::historyHandler);
            httpServer.createContext("/tasks/subtask/epic", this::subtaskEpicHandler);

            httpServer.start();
            System.out.println("HTTP-сервер запущен на " + PORT + " порту!");
        } catch (IOException e) {
            System.out.println("Ошибка при создании сервера: " + e.getMessage());
        }
    }

    // /tasks
    private void tasksHandler(HttpExchange exchange) throws IOException {
        System.out.println("Запрос отсортированных задач и подзадач");
        String method = exchange.getRequestMethod();
        String response;
        if (method.equals("GET")) {
            // возвращаем отсортированный список или пустой список
            response = gson.toJson(manager.getPrioritizedTasks());
            sendResponse(exchange, response);
        } else {
            // если запрос не GET
            responseCode400(exchange);
        }
    }

    // /tasks/task
    private void taskHandler(HttpExchange exchange) throws IOException {
        String response = "";
        System.out.println("Запрос задач");
        String method = exchange.getRequestMethod();
        URI uri = exchange.getRequestURI();
        String queryString = uri.getQuery();
        switch (method) {
            case "GET":
                if (queryString != null) {
                    //вернуть одну задачу
                    String[] queries = queryString.split("&");
                    Integer id = getIdFromQueries(queries);
                    if (id != null && id >= 0) {
                        Task task = manager.getTask(id);
                        if (task != null) {
                            response = gson.toJson(manager.getTask(id));
                            sendResponse(exchange, response);
                        } else {
                            responseCode404(exchange, "Задачи с id=" + id + " не найдено.");
                        }
                    } else {
                        responseCode400(exchange);
                    }
                } else {
                    //вернуть все задачи
                    response = gson.toJson(manager.getAllTasks());
                    sendResponse(exchange, response);
                }
                break;
            case "POST":
                InputStream inputStreamPost = exchange.getRequestBody();
                String bodyPost = new String(inputStreamPost.readAllBytes(), DEFAULT_CHARSET);
                Task taskPost = gson.fromJson(bodyPost, Task.class);
                if (taskPost != null) {
                    manager.addTask(taskPost);
                    responseCode201(exchange, "Задача добавлена");
                }
                break;
            case "PUT":
                InputStream inputStreamPut = exchange.getRequestBody();
                String bodyPut = new String(inputStreamPut.readAllBytes(), DEFAULT_CHARSET);
                Task taskPut = gson.fromJson(bodyPut, Task.class);
                if (manager.getTask(taskPut.getId()) != null) {
                    manager.updateTask(taskPut);
                    sendResponse(exchange, null, 204);
                } else {
                    responseCode404(exchange, "Задачи с id=" + taskPut.getId() + " нет, нельзя обновить задачу.");
                }
                break;
            case "DELETE":
                if (queryString != null) {
                    // удаляем задачу по id
                    String[] queries = queryString.split("&");
                    Integer id = getIdFromQueries(queries);
                    if (id != null && id >= 0) {
                        Task taskDelete = manager.getTask(id);
                        if (taskDelete != null) {
                            manager.deleteTask(id);
                            sendResponse(exchange, null, 204);
                        } else {
                            responseCode404(exchange, "Задачи с id=" + id + " не найдено.");
                        }
                    } else {
                        responseCode400(exchange);
                    }
                } else {
                    // все задачи
                    if (manager.getAllTasks().size() > 0) {
                        manager.deleteAllTasks();
                        sendResponse(exchange, null, 204);
                    } else {
                        responseCode404(exchange, "Задач не найдено.");
                    }
                }
                break;
            default:
                responseCode400(exchange);
        }
        exchange.close();
    }

    private void subtaskHandler(HttpExchange exchange) throws IOException {
        System.out.println("Запрос подзадач");
        sendResponse(exchange, "Запрос подзадач");
    }

    private void epicHandler(HttpExchange exchange) throws IOException {
        System.out.println("Запрос эпиков");
        sendResponse(exchange, "Запрос эпиков");
    }

    private void historyHandler(HttpExchange exchange) throws IOException {
        System.out.println("Запрос истории");
        sendResponse(exchange, "Запрос истории");
    }

    private void subtaskEpicHandler(HttpExchange exchange) throws IOException {
        System.out.println("Запрос подзадач по эпику");
        sendResponse(exchange, "Запрос подзадач по эпику");
    }

    private void sendResponse(HttpExchange exchange, String response, int statusCode) throws IOException {
        exchange.getResponseHeaders().add("Content-Type", "application/json");
        switch (statusCode) {
            case 204:
                exchange.sendResponseHeaders(statusCode, -1);
            default:
                exchange.sendResponseHeaders(statusCode, 0);
        }

        try (OutputStream os = exchange.getResponseBody()) {
            if (response != null || !response.isBlank()) {
                os.write(response.getBytes());
            }
        }
        exchange.close();
    }

    private void sendResponse(HttpExchange exchange, String response) throws IOException {
        sendResponse(exchange, response, 200);
    }

    public static void main(String[] args) {
        new HttpTaskServer().start();
    }

    private Integer getIdFromQueries(String[] queries) {
        String[] pair = queries[0].split("=");
        if (pair[0].toLowerCase().equals("id")) {
            try {
                return Integer.valueOf(pair[1]);
            } catch (NumberFormatException e) {
                return null;
            }
        }
        return null;
    }


    private void responseCode201(HttpExchange exchange, String msg) throws IOException {
        MessageResp message = new MessageResp(msg);
        sendResponse(exchange, gson.toJson(message), 201);
    }

    private void responseCode400(HttpExchange exchange) throws IOException {
        MessageResp message = new MessageResp(RESPONSE_CODE_400_MSG);
        sendResponse(exchange, gson.toJson(message), 400);
    }

    private void responseCode404(HttpExchange exchange, String msg) throws IOException {
        MessageResp message = new MessageResp(msg);
        sendResponse(exchange, gson.toJson(message), 404);
    }
}
