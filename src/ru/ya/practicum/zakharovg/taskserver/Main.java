package ru.ya.practicum.zakharovg.taskserver;

import ru.ya.practicum.zakharovg.taskserver.server.HttpTaskServer;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException, InterruptedException {
        new HttpTaskServer().start();
    }
}
