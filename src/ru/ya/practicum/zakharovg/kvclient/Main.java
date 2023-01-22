package ru.ya.practicum.zakharovg.kvclient;

import com.google.gson.Gson;
import ru.ya.practicum.zakharovg.javakanban.model.Status;
import ru.ya.practicum.zakharovg.javakanban.model.Task;

import java.io.IOException;
import java.net.URI;

public class Main {
    public static void main(String[] args) throws IOException, InterruptedException {
        URI url = URI.create("http://localhost:8078");
        KVTaskClient taskClient = new KVTaskClient(url);
        Task task = new Task("Task1", "Desc", 20);
        task.setId(1);

        // сохраняем значение с key1
        taskClient.put("key1", new Gson().toJson(task));

        // возвращаем значение с key1
        String result = taskClient.load("key1");
        System.out.println(result);

        // преобразуем полученные значения в объект
        Task retTask = new Gson().fromJson(result, Task.class);
        System.out.println(task);

        // изменяем статус объекта
        retTask.setStatus(Status.IN_PROGRESS);

        // загружаем по ключу key1 измененный обект
        taskClient.put("key1", new Gson().toJson(retTask));

        // возвращаем значение с key1
        String result2 = taskClient.load("key1");
        System.out.println(result2);

    }
}
