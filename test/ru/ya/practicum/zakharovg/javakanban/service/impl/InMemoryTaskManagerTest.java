package ru.ya.practicum.zakharovg.javakanban.service.impl;

import ru.ya.practicum.zakharovg.javakanban.service.TaskManagerTest;

public class InMemoryTaskManagerTest extends TaskManagerTest<InMemoryTaskManager> {

    @Override
    public InMemoryTaskManager createInstance() {
        return new InMemoryTaskManager();
    }
}