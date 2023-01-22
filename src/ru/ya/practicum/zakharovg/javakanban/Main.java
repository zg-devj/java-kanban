package ru.ya.practicum.zakharovg.javakanban;

import ru.ya.practicum.zakharovg.javakanban.model.Epic;
import ru.ya.practicum.zakharovg.javakanban.model.Status;
import ru.ya.practicum.zakharovg.javakanban.model.Subtask;
import ru.ya.practicum.zakharovg.javakanban.model.Task;
import ru.ya.practicum.zakharovg.javakanban.service.TaskManager;
import ru.ya.practicum.zakharovg.javakanban.util.Managers;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException, InterruptedException {
        TaskManager manager = Managers.getServerStorage();

//        manager.addTask(new Task("Task", "Desc")); //1
//        manager.addEpic(new Epic("Epic", "Desc"));  //2
//        manager.addSubtask(2,new Subtask("Subtask","Desc")); //3
//
//        manager.getTask(1);
//        manager.getSubtask(3);

//        manager.getSubtask(3);
//        manager.getEpic(2);
//        manager.getTask(1);

        manager.addTask(new Task("Task3","Desc","23.01.2023 12:00"));

        System.out.println(manager.getAllTasks());
        System.out.println(manager.getAllEpics());
        System.out.println(manager.getAllSubtasks());
        System.out.println();
        System.out.println(manager.getHistory());

    }
}
