package ru.ya.practicum.zakharovg.taskserver;

import ru.ya.practicum.zakharovg.taskserver.server.HttpTaskServer;

public class Main {
    public static void main(String[] args) {
        new HttpTaskServer().start();
    }
}
