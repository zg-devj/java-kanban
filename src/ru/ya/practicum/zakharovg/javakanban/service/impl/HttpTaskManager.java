package ru.ya.practicum.zakharovg.javakanban.service.impl;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import ru.ya.practicum.zakharovg.javakanban.model.*;
import ru.ya.practicum.zakharovg.javakanban.util.TaskType;
import ru.ya.practicum.zakharovg.kvclient.KVTaskClient;
import ru.ya.practicum.zakharovg.taskserver.util.BaseTaskDeserializer;

import java.io.IOException;
import java.net.URI;

public class HttpTaskManager extends FileBackedTasksManager {

    private KVTaskClient client;
    private final Gson gson;
    private final BaseTaskDeserializer deserializer;

    public HttpTaskManager(URI uri) throws IOException, InterruptedException {
        super();
        deserializer = new BaseTaskDeserializer("type");
        deserializer.registerBarnType("Task", Task.class);
        deserializer.registerBarnType("Subtask", Subtask.class);
        deserializer.registerBarnType("Epic", Epic.class);
        gson = new GsonBuilder()
                .registerTypeAdapter(BaseTask.class, deserializer)
                .create();

        client = new KVTaskClient(uri);

        load();
    }

    private void load() {
        String json;
        try {
            json = client.load("data");
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        if (!json.isBlank()) {
            StorageUnit storageUnit = gson.fromJson(json, StorageUnit.class);

            for (Task task : storageUnit.getTasks().values()) {
                addItemToLists(this, task, TaskType.valueOf(task.type.toUpperCase()));
            }

            for (Epic epic : storageUnit.getEpics().values()) {
                addItemToLists(this, epic, TaskType.valueOf(epic.type.toUpperCase()));
            }

            for (Subtask subtask : storageUnit.getSubtasks().values()) {
                addItemToLists(this, subtask, TaskType.valueOf(subtask.type.toUpperCase()));
            }

            if (storageUnit.getHistory().size() > 0) {
                for (BaseTask bt : storageUnit.getHistory()) {
                    if (tasks.containsKey(bt.getId())) {
                        historyManager.add(tasks.get(bt.getId()));
                    } else if (epics.containsKey(bt.getId())) {
                        historyManager.add(epics.get(bt.getId()));
                    } else if (subtasks.containsKey(bt.getId())) {
                        historyManager.add(subtasks.get(bt.getId()));
                    }
                }
            }
        }
    }

    @Override
    protected void save() {
        System.out.println("save to server");

        StorageUnit storageUnit = new StorageUnit(tasks, epics, subtasks, getHistory());


        String json = gson.toJson(storageUnit);

        try {
            client.put("data", json);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }


}
