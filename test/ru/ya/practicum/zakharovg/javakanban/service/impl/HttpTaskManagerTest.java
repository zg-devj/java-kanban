package ru.ya.practicum.zakharovg.javakanban.service.impl;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.ya.practicum.zakharovg.javakanban.model.Epic;
import ru.ya.practicum.zakharovg.javakanban.model.Subtask;
import ru.ya.practicum.zakharovg.javakanban.model.Task;
import ru.ya.practicum.zakharovg.javakanban.service.TaskManager;
import ru.ya.practicum.zakharovg.javakanban.service.TaskManagerTest;
import ru.ya.practicum.zakharovg.javakanban.util.Managers;
import ru.ya.practicum.zakharovg.kvserver.KVServer;

import java.io.IOException;
import java.net.URI;

import static org.junit.jupiter.api.Assertions.assertEquals;

class HttpTaskManagerTest extends TaskManagerTest<TaskManager> {

    private KVServer kvServer;

    @BeforeEach
    void setUp() {
        try {
            kvServer = new KVServer();
            kvServer.start();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @AfterEach
    void tearDown() {
        kvServer.stop();
    }

    @Override
    public TaskManager createInstance() {
        try {
            return new HttpTaskManager(URI.create("http://localhost:8078"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void test_Load() throws IOException, InterruptedException {
        TaskManager manager = Managers.getServerStorage();

        manager.addTask(new Task("Task", "Desc")); //1
        manager.addEpic(new Epic("Epic", "Desc"));  //2
        manager.addSubtask(2,new Subtask("Subtask","Desc")); //3


        TaskManager manager2 = Managers.getServerStorage();

        assertEquals(1,manager2.getAllTasks().size());
        assertEquals(1,manager2.getAllEpics().size());
        assertEquals(1,manager2.getAllSubtasks().size());

    }
}