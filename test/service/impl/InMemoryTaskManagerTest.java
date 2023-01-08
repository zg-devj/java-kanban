package service.impl;

import service.TaskManagerTest;

public class InMemoryTaskManagerTest extends TaskManagerTest<InMemoryTaskManager> {

    @Override
    public InMemoryTaskManager createInstance() {
        return new InMemoryTaskManager();
    }
}