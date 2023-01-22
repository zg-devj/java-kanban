package ru.ya.practicum.zakharovg.taskserver.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.junit.jupiter.api.Test;
import ru.ya.practicum.zakharovg.javakanban.model.BaseTask;
import ru.ya.practicum.zakharovg.javakanban.model.Epic;
import ru.ya.practicum.zakharovg.javakanban.model.Subtask;
import ru.ya.practicum.zakharovg.javakanban.model.Task;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class BaseTaskDeserializerTest {
    @Test
    public void test_BaseTaskDeserializer() {
        List<BaseTask> list = new ArrayList<>();
        Task t1 = new Task("Task", "Desc", "10.01.2023 11:00", 10);
        t1.setId(1);
        list.add(t1);
        Epic e1 = new Epic("Epic", "Desc");
        e1.setId(2);
        list.add(e1);

        BaseTaskDeserializer deserializer = new BaseTaskDeserializer("type");
        deserializer.registerBarnType("Task", Task.class);
        deserializer.registerBarnType("Subtask", Subtask.class);
        deserializer.registerBarnType("Epic", Epic.class);

        Gson gson1 = new GsonBuilder()
                .registerTypeAdapter(BaseTask.class, deserializer)
                .create();

        String jsonA = gson1.toJson(list);

        Type type = new TypeToken<ArrayList<BaseTask>>() {
        }.getType();

        List<BaseTask> outList = gson1.fromJson(jsonA, type);

        assertEquals(2, outList.size());
        assertTrue(outList.get(0) instanceof Task);
        assertTrue(outList.get(1) instanceof Epic);

    }
}