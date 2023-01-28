package ru.ya.practicum.zakharovg.javakanban.service.impl;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import ru.ya.practicum.zakharovg.javakanban.model.*;
import ru.ya.practicum.zakharovg.javakanban.util.TaskConverter;
import ru.ya.practicum.zakharovg.javakanban.util.TaskType;
import ru.ya.practicum.zakharovg.kvclient.KVTaskClient;
import ru.ya.practicum.zakharovg.taskserver.util.BaseTaskDeserializer;

import java.io.IOException;
import java.net.URI;
import java.util.List;

public class HttpTaskManager extends FileBackedTasksManager {

    private KVTaskClient client;
    private final Gson gson;
    private final BaseTaskDeserializer deserializer;

    public HttpTaskManager(URI url) throws IOException, InterruptedException {
        super();
        deserializer = new BaseTaskDeserializer("type");
        deserializer.registerBarnType("Task", Task.class);
        deserializer.registerBarnType("Subtask", Subtask.class);
        deserializer.registerBarnType("Epic", Epic.class);
        gson = new GsonBuilder()
                .registerTypeAdapter(BaseTask.class, deserializer)
                .create();

        client = new KVTaskClient(url);

        load();
    }

    private void load() {
        String json;
        try {
            json = client.load("data");
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }

        if (!json.isBlank()) {
            StorageUnit storageUnit = gson.fromJson(json, StorageUnit.class);

            for (Task task : storageUnit.getTasks()) {
                addItemToLists(this, task, TaskType.valueOf(task.getType().toUpperCase()));
            }

            for (Epic epic : storageUnit.getEpics()) {
                addItemToLists(this, epic, TaskType.valueOf(epic.getType().toUpperCase()));
            }

            for (Subtask subtask : storageUnit.getSubtasks()) {
                addItemToLists(this, subtask, TaskType.valueOf(subtask.getType().toUpperCase()));
            }

            List<Integer> history = TaskConverter.historyFromString(storageUnit.getHistory());
            if (history.size() > 0) {
                for (Integer id : history) {
                    if (tasks.containsKey(id)) {
                        historyManager.add(tasks.get(id));
                    } else if (epics.containsKey(id)) {
                        historyManager.add(epics.get(id));
                    } else if (subtasks.containsKey(id)) {
                        historyManager.add(subtasks.get(id));
                    }
                }
            }
        }
    }

    @Override
    protected void save() {
        // При перезапуске KVServer токен меняется.
        // Т.к. KVServer не авторизует, а только аутентифицирует клиента
        // и не хранит его токен,
        // а данные на TaskManager сохраняются раньше, чем попадают на KVServer,
        // в результате данные в TaskManager будут утеряны.
        // Решение: обновляем токен.
        if (!isSaveServerAccess()) {
            try {
                client.initToken();
            } catch (IOException | InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        System.out.println("Сохраняем данные на KVServer.");

        StorageUnit storageUnit = new StorageUnit(tasks.values(),
                epics.values(), subtasks.values(),
                TaskConverter.historyToString(getHistory()));

        String json = gson.toJson(storageUnit);

        try {
            client.put("data", json);
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private boolean isSaveServerAccess() {
        try {
            return client.ping();
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
