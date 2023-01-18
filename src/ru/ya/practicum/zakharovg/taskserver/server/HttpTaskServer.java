package ru.ya.practicum.zakharovg.taskserver.server;

import com.sun.net.httpserver.HttpServer;
import ru.ya.practicum.zakharovg.javakanban.service.TaskManager;
import ru.ya.practicum.zakharovg.javakanban.util.Managers;

import java.io.IOException;
import java.net.InetSocketAddress;

public class HttpTaskServer {
    private static final int PORT = 8081;
    private TaskManager manager;

    private HttpTaskServer() {
        manager = Managers.getFileStorage("tasks.csv");
    }

    public void create() {
        try {
            HttpServer httpServer = HttpServer.create();
            httpServer.bind(new InetSocketAddress(PORT), 0);
            httpServer.start();
            System.out.println("HTTP-сервер запущен на " + PORT + " порту!");
        } catch (IOException e) {
            System.out.println("Ошибка при создании сервера: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        new HttpTaskServer().create();
    }
}
