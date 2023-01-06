package service.impl;

import service.TaskManagerTest;

class InMemoryTaskManagerTest extends TaskManagerTest<InMemoryTaskManager> {
    @Override
    public InMemoryTaskManager createInstance() {
        return new InMemoryTaskManager();
    }
}