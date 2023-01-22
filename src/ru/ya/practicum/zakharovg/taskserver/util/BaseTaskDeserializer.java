package ru.ya.practicum.zakharovg.taskserver.util;

import com.google.gson.*;
import ru.ya.practicum.zakharovg.javakanban.model.BaseTask;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class BaseTaskDeserializer implements JsonDeserializer<BaseTask> {
    private String baseTaskTypeElementName;
    private Gson gson;
    private Map<String, Class<? extends BaseTask>> baseTaskTypeRegistry;

    public BaseTaskDeserializer(String baseTaskTypeElementName) {
        this.baseTaskTypeElementName = baseTaskTypeElementName;
        this.gson = new Gson();
        this.baseTaskTypeRegistry = new HashMap<>();
    }

    public void registerBarnType(String baseTaskTypeName, Class<? extends BaseTask> baseTaskType) {
        baseTaskTypeRegistry.put(baseTaskTypeName, baseTaskType);
    }

    @Override
    public BaseTask deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        JsonObject baseTaskObject = jsonElement.getAsJsonObject();
        JsonElement baseTaskTypeElement = baseTaskObject.get(baseTaskTypeElementName);

        Class<? extends BaseTask> baseTaskType = baseTaskTypeRegistry
                .get(baseTaskTypeElement.getAsString());
        return gson.fromJson(baseTaskObject, baseTaskType);
    }
}
