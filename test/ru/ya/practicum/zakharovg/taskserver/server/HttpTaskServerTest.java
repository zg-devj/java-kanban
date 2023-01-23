package ru.ya.practicum.zakharovg.taskserver.server;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.ya.practicum.zakharovg.javakanban.model.BaseTask;
import ru.ya.practicum.zakharovg.javakanban.model.Epic;
import ru.ya.practicum.zakharovg.javakanban.model.Subtask;
import ru.ya.practicum.zakharovg.javakanban.model.Task;
import ru.ya.practicum.zakharovg.javakanban.service.TaskManager;
import ru.ya.practicum.zakharovg.javakanban.util.Managers;
import ru.ya.practicum.zakharovg.taskserver.model.MessageResp;
import ru.ya.practicum.zakharovg.taskserver.util.BaseTaskDeserializer;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class HttpTaskServerTest {
    private BaseTaskDeserializer deserializer;
    private HttpTaskServer taskServer;
    private Gson gson;
    private TaskManager taskManager;
    private Task task;
    private Task task2;
    private Epic epic;
    private Subtask subtask;
    private Subtask subtask2;

    @BeforeEach
    void setUp() throws IOException {
        System.out.println();
        deserializer = new BaseTaskDeserializer("type");
        deserializer.registerBarnType("Task", Task.class);
        deserializer.registerBarnType("Subtask", Subtask.class);
        deserializer.registerBarnType("Epic", Epic.class);

        gson = new GsonBuilder()
                .registerTypeAdapter(BaseTask.class, deserializer)
                .serializeNulls().create();

        taskManager = Managers.getDefault();
        taskServer = new HttpTaskServer(taskManager);

        task = new Task("Task", "Desc", "11.01.2023 11:00", 10);
        taskManager.addTask(task); // 1

        task2 = new Task("Task", "Desc", "12.01.2023 11:00", 10);
        taskManager.addTask(task2); // 2

        epic = new Epic("Epic", "Desc");
        taskManager.addEpic(epic); // 3

        subtask = new Subtask("Subtask", "Desc", "13.01.2023 11:00", 10);
        taskManager.addSubtask(3, subtask); //4

        subtask2 = new Subtask("Subtask2", "Desc", "14.01.2023 11:00", 10);
        taskManager.addSubtask(3, subtask2); //5

        taskServer.start();
    }

    @AfterEach
    void tearDown() {
        taskServer.stop();
    }

    //region tasks
    @Test
    public void tasks_ReturnCode405_WrongMethod() throws IOException, InterruptedException {
        HttpRequest.BodyPublisher bodyPublisher
                = HttpRequest.BodyPublishers.ofString(gson.toJson("{\"a\":\"b\"}"));

        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:8080/api/tasks");
        HttpRequest request = HttpRequest.newBuilder().uri(uri).method("OPTION", bodyPublisher).build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(405, response.statusCode(), "Не верный код ответа");

        Type type = new TypeToken<MessageResp>() {
        }.getType();
        MessageResp actual = gson.fromJson(response.body(), type);

        assertNotNull(actual, "Сообщение не возвращается.");
        assertEquals("Метод запроса не поддерживается.", actual.getMessage(), "Сообщения не совпадают.");
    }

    @Test
    public void tasks_ReturnAllTasks_GET() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:8080/api/tasks");
        HttpRequest request = HttpRequest.newBuilder().GET().uri(uri).build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode(), "Не верный код ответа");

        Type type = new TypeToken<ArrayList<Task>>() {
        }.getType();
        List<Task> actual = gson.fromJson(response.body(), type);

        assertNotNull(actual, "Задача не возвращается");
        assertEquals(4, actual.size(), "Не верное количество задач");
        assertEquals(task, actual.get(0), "Задачи не совпадают");
    }

    @Test
    public void tasks_ReturnAllTasks_GETEmptyTasks() throws IOException, InterruptedException {
        taskManager.deleteAllTasks();
        taskManager.deleteAllSubtasks();
        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:8080/api/tasks");
        HttpRequest request = HttpRequest.newBuilder().GET().uri(uri).build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode(), "Не верный код ответа");

        Type type = new TypeToken<ArrayList<Task>>() {
        }.getType();
        List<Task> actual = gson.fromJson(response.body(), type);

        assertNotNull(actual, "Задача не возвращается");
        assertEquals(0, actual.size(), "Не верное количество задач");
    }
    //endregion

    //region task
    @Test
    public void task_ReturnCode405_WrongMethod() throws IOException, InterruptedException {
        HttpRequest.BodyPublisher bodyPublisher
                = HttpRequest.BodyPublishers.ofString(gson.toJson("{\"a\":\"b\"}"));

        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:8080/api/tasks/task");
        HttpRequest request = HttpRequest.newBuilder().uri(uri).method("OPTION", bodyPublisher).build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(405, response.statusCode(), "Не верный код ответа");

        Type type = new TypeToken<MessageResp>() {
        }.getType();
        MessageResp actual = gson.fromJson(response.body(), type);

        assertNotNull(actual, "Сообщение не возвращается.");
        assertEquals("Метод запроса не поддерживается.", actual.getMessage(), "Сообщения не совпадают.");
    }

    @Test
    public void taskGET_ReturnTaskById() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:8080/api/tasks/task?id=1");
        HttpRequest request = HttpRequest.newBuilder().GET().uri(uri).build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode(), "Не верный код ответа");

        Type type = new TypeToken<Task>() {
        }.getType();
        Task actual = gson.fromJson(response.body(), type);

        assertNotNull(actual, "Задача не возвращается");
        assertEquals(task, actual, "Задачи не совпадают");

    }

    @Test
    public void taskGET_ReturnTaskById_WithWrongId() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:8080/api/tasks/task?id=55");
        HttpRequest request = HttpRequest.newBuilder().GET().uri(uri).build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(404, response.statusCode(), "Не верный код ответа");

        Type type = new TypeToken<MessageResp>() {
        }.getType();
        MessageResp actual = gson.fromJson(response.body(), type);

        assertNotNull(actual, "Сообщение не возвращается.");
        assertEquals("Задачи с id 55 не найдено.", actual.getMessage(), "Сообщения не совпадают.");

    }

    @Test
    public void taskGET_ReturnCode400_WithNotCorrectId() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:8080/api/tasks/task?id=wrong");
        HttpRequest request = HttpRequest.newBuilder().GET().uri(uri).build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(400, response.statusCode(), "Не верный код ответа");

        Type type = new TypeToken<MessageResp>() {
        }.getType();
        MessageResp actual = gson.fromJson(response.body(), type);

        assertNotNull(actual, "Сообщение не возвращается.");
        assertEquals("Вы использовали неверный запрос.", actual.getMessage(), "Сообщения не совпадают.");
    }

    @Test
    public void taskGET_ReturnAllTask_Normal() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:8080/api/tasks/task");
        HttpRequest request = HttpRequest.newBuilder().GET().uri(uri).build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode(), "Не верный код ответа");

        Type type = new TypeToken<ArrayList<Task>>() {
        }.getType();
        List<Task> actual = gson.fromJson(response.body(), type);

        assertNotNull(actual, "Задача не возвращается");
        assertEquals(2, actual.size(), "Не верное количество задач");
        assertEquals(task, actual.get(0), "Задачи не совпадают");
    }

    @Test
    public void taskPOST_ReturnCode201_AddNewTask_Normal() throws IOException, InterruptedException {
        Task newTask = new Task("NewTask", "Desc");
        HttpRequest.BodyPublisher bodyPublisher
                = HttpRequest.BodyPublishers.ofString(gson.toJson(newTask));

        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:8080/api/tasks/task");
        HttpRequest request = HttpRequest.newBuilder().POST(bodyPublisher).uri(uri).build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response.statusCode(), "Не верный код ответа");
    }

    @Test
    public void taskPOST_ReturnCode422_AddNewTask_WrongValidate() throws IOException, InterruptedException {
        Task newTask = new Task("NewTask", "Desc", "11.01.2023 11:00", 10);
        HttpRequest.BodyPublisher bodyPublisher
                = HttpRequest.BodyPublishers.ofString(gson.toJson(newTask));

        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:8080/api/tasks/task");
        HttpRequest request = HttpRequest.newBuilder().POST(bodyPublisher).uri(uri).build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(422, response.statusCode(), "Не верный код ответа");

        Type type = new TypeToken<MessageResp>() {
        }.getType();
        MessageResp actual = gson.fromJson(response.body(), type);

        assertNotNull(actual, "Сообщение не возвращается.");
        assertEquals("Добавляемая задача пересекается с существующими.", actual.getMessage(), "Сообщения не совпадают.");
    }

    @Test
    public void taskPUT_ReturnCode204_UpdateTask_Normal() throws IOException, InterruptedException {
        Task updateTask =
                new Task("Updated Task", "Updated Desc"
                        , "11.01.2023 11:00", 20);
        updateTask.setId(task.getId());

        HttpRequest.BodyPublisher bodyPublisher
                = HttpRequest.BodyPublishers.ofString(gson.toJson(updateTask));

        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:8080/api/tasks/task");
        HttpRequest request = HttpRequest.newBuilder().PUT(bodyPublisher).uri(uri).build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(204, response.statusCode(), "Не верный код ответа");
    }

    @Test
    public void taskPUT_ReturnCode422_UpdateTask_WrongValidate() throws IOException, InterruptedException {
        Task updateTask =
                new Task("Updated Task", "Updated Desc"
                        , "12.01.2023 11:00", 20);
        updateTask.setId(task.getId());

        HttpRequest.BodyPublisher bodyPublisher
                = HttpRequest.BodyPublishers.ofString(gson.toJson(updateTask));

        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:8080/api/tasks/task");
        HttpRequest request = HttpRequest.newBuilder().PUT(bodyPublisher).uri(uri).build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(422, response.statusCode(), "Не верный код ответа");

        Type type = new TypeToken<MessageResp>() {
        }.getType();
        MessageResp actual = gson.fromJson(response.body(), type);

        assertNotNull(actual, "Сообщение не возвращается.");
        assertEquals("Добавляемая задача пересекается с существующими.", actual.getMessage(), "Сообщения не совпадают.");
    }

    @Test
    public void taskPUT_ReturnCode404_UpdateTask_WithWrongId() throws IOException, InterruptedException {
        Task updateTask =
                new Task("Updated Task", "Updated Desc"
                        , "11.01.2023 11:00", 20);
        updateTask.setId(55);

        HttpRequest.BodyPublisher bodyPublisher
                = HttpRequest.BodyPublishers.ofString(gson.toJson(updateTask));

        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:8080/api/tasks/task");
        HttpRequest request = HttpRequest.newBuilder().PUT(bodyPublisher).uri(uri).build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(404, response.statusCode(), "Не верный код ответа");

        Type type = new TypeToken<MessageResp>() {
        }.getType();
        MessageResp actual = gson.fromJson(response.body(), type);

        assertNotNull(actual, "Сообщение не возвращается.");
        assertEquals("Задачи с id 55 не найдено.", actual.getMessage(), "Сообщения не совпадают.");
    }

    @Test
    public void taskDELETE_ReturnCode204_DeleteTask_Normal() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:8080/api/tasks/task?id=1");
        HttpRequest request = HttpRequest.newBuilder().DELETE().uri(uri).build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(204, response.statusCode(), "Не верный код ответа");

        assertEquals(1, taskManager.getAllTasks().size(), "Не верное количество задач");
    }

    @Test
    public void taskDELETE_ReturnCode404_DeleteTask_WithWrongId() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:8080/api/tasks/task?id=55");
        HttpRequest request = HttpRequest.newBuilder().DELETE().uri(uri).build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(404, response.statusCode(), "Не верный код ответа");

        assertEquals(2, taskManager.getAllTasks().size(), "Не верное количество задач");

        Type type = new TypeToken<MessageResp>() {
        }.getType();
        MessageResp actual = gson.fromJson(response.body(), type);

        assertNotNull(actual, "Сообщение не возвращается.");
        assertEquals("Задачи с id 55 не найдено.", actual.getMessage(), "Сообщения не совпадают.");
    }

    @Test
    public void taskDELETE_ReturnCode400_DeleteTask_WithNotCorrectId() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:8080/api/tasks/task?id=wrong");
        HttpRequest request = HttpRequest.newBuilder().DELETE().uri(uri).build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(400, response.statusCode(), "Не верный код ответа");

        Type type = new TypeToken<MessageResp>() {
        }.getType();
        MessageResp actual = gson.fromJson(response.body(), type);

        assertNotNull(actual, "Сообщение не возвращается.");
        assertEquals("Вы использовали неверный запрос.", actual.getMessage(), "Сообщения не совпадают.");
    }

    @Test
    public void taskDELETE_ReturnCode204_DeleteAllTasks_Normal() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:8080/api/tasks/task");
        HttpRequest request = HttpRequest.newBuilder().DELETE().uri(uri).build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(204, response.statusCode(), "Не верный код ответа");

        assertEquals(0, taskManager.getAllTasks().size(), "Не верное количество задач");
    }

    @Test
    public void taskDELETE_ReturnCode404_DeleteAllTasks_WithEmptyTasks() throws IOException, InterruptedException {
        taskManager.deleteAllTasks();

        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:8080/api/tasks/task");
        HttpRequest request = HttpRequest.newBuilder().DELETE().uri(uri).build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(404, response.statusCode(), "Не верный код ответа");

        assertEquals(0, taskManager.getAllTasks().size(), "Не верное количество задач");

        Type type = new TypeToken<MessageResp>() {
        }.getType();
        MessageResp actual = gson.fromJson(response.body(), type);

        assertNotNull(actual, "Сообщение не возвращается.");
        assertEquals("Задач не найдено.", actual.getMessage(), "Сообщения не совпадают.");
    }
    //endregion

    //region epic
    @Test
    public void epic_ReturnCode405_WrongMethod() throws IOException, InterruptedException {
        HttpRequest.BodyPublisher bodyPublisher
                = HttpRequest.BodyPublishers.ofString(gson.toJson("{\"a\":\"b\"}"));

        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:8080/api/tasks/epic");
        HttpRequest request = HttpRequest.newBuilder().uri(uri).method("OPTION", bodyPublisher).build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(405, response.statusCode(), "Не верный код ответа");

        Type type = new TypeToken<MessageResp>() {
        }.getType();
        MessageResp actual = gson.fromJson(response.body(), type);

        assertNotNull(actual, "Сообщение не возвращается.");
        assertEquals("Метод запроса не поддерживается.", actual.getMessage(), "Сообщения не совпадают.");
    }

    @Test
    public void epicGET_ReturnEpicById() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:8080/api/tasks/epic?id=3");
        HttpRequest request = HttpRequest.newBuilder().GET().uri(uri).build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode(), "Не верный код ответа");

        Type type = new TypeToken<Epic>() {
        }.getType();
        Epic actual = gson.fromJson(response.body(), type);

        assertNotNull(actual, "Задача не возвращается");
        assertEquals(epic, actual, "Задачи не совпадают");

    }

    @Test
    public void epicGET_ReturnEpicById_WithWrongId() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:8080/api/tasks/epic?id=55");
        HttpRequest request = HttpRequest.newBuilder().GET().uri(uri).build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(404, response.statusCode(), "Не верный код ответа");

        Type type = new TypeToken<MessageResp>() {
        }.getType();
        MessageResp actual = gson.fromJson(response.body(), type);

        assertNotNull(actual, "Сообщение не возвращается.");
        assertEquals("Задачи с id 55 не найдено.", actual.getMessage(), "Сообщения не совпадают.");

    }

    @Test
    public void epicGET_ReturnCode400_WithNotCorrectId() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:8080/api/tasks/epic?id=wrong");
        HttpRequest request = HttpRequest.newBuilder().GET().uri(uri).build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(400, response.statusCode(), "Не верный код ответа");

        Type type = new TypeToken<MessageResp>() {
        }.getType();
        MessageResp actual = gson.fromJson(response.body(), type);

        assertNotNull(actual, "Сообщение не возвращается.");
        assertEquals("Вы использовали неверный запрос.", actual.getMessage(), "Сообщения не совпадают.");
    }

    @Test
    public void epicGET_ReturnAllEpic_Normal() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:8080/api/tasks/epic");
        HttpRequest request = HttpRequest.newBuilder().GET().uri(uri).build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode(), "Не верный код ответа");

        Type type = new TypeToken<ArrayList<Epic>>() {
        }.getType();
        List<Epic> actual = gson.fromJson(response.body(), type);

        assertNotNull(actual, "Задача не возвращается");
        assertEquals(1, actual.size(), "Не верное количество задач");
        assertEquals(epic, actual.get(0), "Задачи не совпадают");
    }

    @Test
    public void epicPOST_ReturnCode201_AddNewEpic_Normal() throws IOException, InterruptedException {
        Epic newEpic = new Epic("NewEpic", "Desc");
        HttpRequest.BodyPublisher bodyPublisher
                = HttpRequest.BodyPublishers.ofString(gson.toJson(newEpic));

        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:8080/api/tasks/epic");
        HttpRequest request = HttpRequest.newBuilder().POST(bodyPublisher).uri(uri).build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response.statusCode(), "Не верный код ответа");
    }

    @Test
    public void epicPUT_ReturnCode204_UpdateEpic_Normal() throws IOException, InterruptedException {
        Epic updateEpic = new Epic("Updated Epic", "Updated Desc");
        updateEpic.setId(epic.getId());

        HttpRequest.BodyPublisher bodyPublisher
                = HttpRequest.BodyPublishers.ofString(gson.toJson(updateEpic));

        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:8080/api/tasks/epic");
        HttpRequest request = HttpRequest.newBuilder().PUT(bodyPublisher).uri(uri).build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(204, response.statusCode(), "Не верный код ответа");
    }

    @Test
    public void epicPUT_ReturnCode404_UpdateEpic_WithWrongId() throws IOException, InterruptedException {
        Epic updateEpic = new Epic("Updated Epic", "Updated Desc");
        updateEpic.setId(55);

        HttpRequest.BodyPublisher bodyPublisher
                = HttpRequest.BodyPublishers.ofString(gson.toJson(updateEpic));

        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:8080/api/tasks/epic");
        HttpRequest request = HttpRequest.newBuilder().PUT(bodyPublisher).uri(uri).build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(404, response.statusCode(), "Не верный код ответа");

        Type type = new TypeToken<MessageResp>() {
        }.getType();
        MessageResp actual = gson.fromJson(response.body(), type);

        assertNotNull(actual, "Сообщение не возвращается.");
        assertEquals("Задачи с id 55 не найдено.", actual.getMessage(), "Сообщения не совпадают.");
    }

    @Test
    public void epicDELETE_ReturnCode204_DeleteEpic_Normal() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:8080/api/tasks/epic?id=3");
        HttpRequest request = HttpRequest.newBuilder().DELETE().uri(uri).build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(204, response.statusCode(), "Не верный код ответа");

        assertEquals(0, taskManager.getAllEpics().size(), "Не верное количество задач");
    }

    @Test
    public void epicDELETE_ReturnCode404_DeleteEpic_WithWrongId() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:8080/api/tasks/epic?id=55");
        HttpRequest request = HttpRequest.newBuilder().DELETE().uri(uri).build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(404, response.statusCode(), "Не верный код ответа");

        assertEquals(1, taskManager.getAllEpics().size(), "Не верное количество задач");

        Type type = new TypeToken<MessageResp>() {
        }.getType();
        MessageResp actual = gson.fromJson(response.body(), type);

        assertNotNull(actual, "Сообщение не возвращается.");
        assertEquals("Задачи с id 55 не найдено.", actual.getMessage(), "Сообщения не совпадают.");
    }

    @Test
    public void epicDELETE_ReturnCode400_DeleteEpic_WithNotCorrectId() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:8080/api/tasks/epic?id=wrong");
        HttpRequest request = HttpRequest.newBuilder().DELETE().uri(uri).build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(400, response.statusCode(), "Не верный код ответа");

        Type type = new TypeToken<MessageResp>() {
        }.getType();
        MessageResp actual = gson.fromJson(response.body(), type);

        assertNotNull(actual, "Сообщение не возвращается.");
        assertEquals("Вы использовали неверный запрос.", actual.getMessage(), "Сообщения не совпадают.");
    }

    @Test
    public void epicDELETE_ReturnCode204_DeleteAllEpics_Normal() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:8080/api/tasks/epic");
        HttpRequest request = HttpRequest.newBuilder().DELETE().uri(uri).build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(204, response.statusCode(), "Не верный код ответа");

        assertEquals(0, taskManager.getAllEpics().size(), "Не верное количество задач");
    }

    @Test
    public void epicDELETE_ReturnCode404_DeleteAllEpics_WithEmptyTasks() throws IOException, InterruptedException {
        taskManager.deleteAllEpics();

        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:8080/api/tasks/epic");
        HttpRequest request = HttpRequest.newBuilder().DELETE().uri(uri).build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(404, response.statusCode(), "Не верный код ответа");

        assertEquals(0, taskManager.getAllEpics().size(), "Не верное количество задач");

        Type type = new TypeToken<MessageResp>() {
        }.getType();
        MessageResp actual = gson.fromJson(response.body(), type);

        assertNotNull(actual, "Сообщение не возвращается.");
        assertEquals("Задач не найдено.", actual.getMessage(), "Сообщения не совпадают.");
    }
    //endregion

    //region subtask
    @Test
    public void subtask_ReturnCode405_WrongMethod() throws IOException, InterruptedException {
        HttpRequest.BodyPublisher bodyPublisher
                = HttpRequest.BodyPublishers.ofString(gson.toJson("{\"a\":\"b\"}"));

        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:8080/api/tasks/subtask");
        HttpRequest request = HttpRequest.newBuilder().uri(uri).method("OPTION", bodyPublisher).build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(405, response.statusCode(), "Не верный код ответа");

        Type type = new TypeToken<MessageResp>() {
        }.getType();
        MessageResp actual = gson.fromJson(response.body(), type);

        assertNotNull(actual, "Сообщение не возвращается.");
        assertEquals("Метод запроса не поддерживается.", actual.getMessage(), "Сообщения не совпадают.");
    }

    @Test
    public void subtaskGET_ReturnSubtaskById() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:8080/api/tasks/subtask?id=4");
        HttpRequest request = HttpRequest.newBuilder().GET().uri(uri).build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode(), "Не верный код ответа");

        Type type = new TypeToken<Subtask>() {
        }.getType();
        Subtask actual = gson.fromJson(response.body(), type);

        System.out.println(subtask);

        assertNotNull(actual, "Задача не возвращается");
        assertEquals(subtask, actual, "Задачи не совпадают");

    }

    @Test
    public void subtaskGET_ReturnSubtaskById_WithWrongId() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:8080/api/tasks/subtask?id=55");
        HttpRequest request = HttpRequest.newBuilder().GET().uri(uri).build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(404, response.statusCode(), "Не верный код ответа");

        Type type = new TypeToken<MessageResp>() {
        }.getType();
        MessageResp actual = gson.fromJson(response.body(), type);

        assertNotNull(actual, "Сообщение не возвращается.");
        assertEquals("Задачи с id 55 не найдено.", actual.getMessage(), "Сообщения не совпадают.");

    }

    @Test
    public void subtaskGET_ReturnCode400_WithNotCorrectId() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:8080/api/tasks/subtask?id=wrong");
        HttpRequest request = HttpRequest.newBuilder().GET().uri(uri).build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(400, response.statusCode(), "Не верный код ответа");

        Type type = new TypeToken<MessageResp>() {
        }.getType();
        MessageResp actual = gson.fromJson(response.body(), type);

        assertNotNull(actual, "Сообщение не возвращается.");
        assertEquals("Вы использовали неверный запрос.", actual.getMessage(), "Сообщения не совпадают.");
    }

    @Test
    public void subtaskGET_ReturnAllSubtask_Normal() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:8080/api/tasks/subtask");
        HttpRequest request = HttpRequest.newBuilder().GET().uri(uri).build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode(), "Не верный код ответа");

        Type type = new TypeToken<ArrayList<Subtask>>() {
        }.getType();
        List<Subtask> actual = gson.fromJson(response.body(), type);

        assertNotNull(actual, "Задача не возвращается");
        assertEquals(2, actual.size(), "Не верное количество задач");
        assertEquals(subtask, actual.get(0), "Задачи не совпадают");
    }

    @Test
    public void subtaskPOST_ReturnCode201_AddNewSubtask_Normal() throws IOException, InterruptedException {
        Subtask newSubtask = new Subtask("New Subtask", "New Desc");
        newSubtask.setEpicId(epic.getId());
        HttpRequest.BodyPublisher bodyPublisher
                = HttpRequest.BodyPublishers.ofString(gson.toJson(newSubtask));

        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:8080/api/tasks/subtask");
        HttpRequest request = HttpRequest.newBuilder().POST(bodyPublisher).uri(uri).build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response.statusCode(), "Не верный код ответа");
    }

    @Test
    public void subtaskPOST_ReturnCode422_AddNewSubtask_WrongValidate() throws IOException, InterruptedException {
        Subtask newSubtask = new Subtask("New Subtask", "Desc", "14.01.2023 11:00", 10);
        newSubtask.setEpicId(epic.getId());
        HttpRequest.BodyPublisher bodyPublisher
                = HttpRequest.BodyPublishers.ofString(gson.toJson(newSubtask));

        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:8080/api/tasks/subtask");
        HttpRequest request = HttpRequest.newBuilder().POST(bodyPublisher).uri(uri).build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(422, response.statusCode(), "Не верный код ответа");

        Type type = new TypeToken<MessageResp>() {
        }.getType();
        MessageResp actual = gson.fromJson(response.body(), type);

        assertNotNull(actual, "Сообщение не возвращается.");
        assertEquals("Добавляемая задача пересекается с существующими.", actual.getMessage(), "Сообщения не совпадают.");
    }

    @Test
    public void subtaskPUT_ReturnCode204_UpdateSubtask_Normal() throws IOException, InterruptedException {
        Subtask updateSubtask =
                new Subtask("Updated Task", "Updated Desc"
                        , "13.01.2023 11:00", 20);
        updateSubtask.setId(subtask.getId());

        HttpRequest.BodyPublisher bodyPublisher
                = HttpRequest.BodyPublishers.ofString(gson.toJson(updateSubtask));

        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:8080/api/tasks/subtask");
        HttpRequest request = HttpRequest.newBuilder().PUT(bodyPublisher).uri(uri).build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(204, response.statusCode(), "Не верный код ответа");
    }

    @Test
    public void subtaskPUT_ReturnCode422_UpdateSubtask_WrongValidate() throws IOException, InterruptedException {
        Subtask updateSubtask =
                new Subtask("Updated Task", "Updated Desc"
                        , "14.01.2023 11:00", 20);
        updateSubtask.setEpicId(subtask.getEpicId());
        updateSubtask.setId(subtask.getId());

        HttpRequest.BodyPublisher bodyPublisher
                = HttpRequest.BodyPublishers.ofString(gson.toJson(updateSubtask));

        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:8080/api/tasks/subtask");
        HttpRequest request = HttpRequest.newBuilder().PUT(bodyPublisher).uri(uri).build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(422, response.statusCode(), "Не верный код ответа");

        Type type = new TypeToken<MessageResp>() {
        }.getType();
        MessageResp actual = gson.fromJson(response.body(), type);

        assertNotNull(actual, "Сообщение не возвращается.");
        assertEquals("Добавляемая задача пересекается с существующими.", actual.getMessage(), "Сообщения не совпадают.");
    }

    @Test
    public void subtaskPUT_ReturnCode404_UpdateSubtask_WithWrongId() throws IOException, InterruptedException {
        Subtask updateSubtask =
                new Subtask("Updated Task", "Updated Desc"
                        , "20.01.2023 11:00", 20);
        updateSubtask.setEpicId(epic.getId());
        updateSubtask.setId(55);

        HttpRequest.BodyPublisher bodyPublisher
                = HttpRequest.BodyPublishers.ofString(gson.toJson(updateSubtask));

        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:8080/api/tasks/subtask");
        HttpRequest request = HttpRequest.newBuilder().PUT(bodyPublisher).uri(uri).build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(404, response.statusCode(), "Не верный код ответа");

        Type type = new TypeToken<MessageResp>() {
        }.getType();
        MessageResp actual = gson.fromJson(response.body(), type);

        assertNotNull(actual, "Сообщение не возвращается.");
        assertEquals("Задачи с id 55 не найдено.", actual.getMessage(), "Сообщения не совпадают.");
    }

    @Test
    public void subtaskDELETE_ReturnCode204_DeleteSubtask_Normal() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:8080/api/tasks/subtask?id=4");
        HttpRequest request = HttpRequest.newBuilder().DELETE().uri(uri).build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(204, response.statusCode(), "Не верный код ответа");

        assertEquals(1, taskManager.getAllSubtasks().size(), "Не верное количество задач");
    }

    @Test
    public void subtaskDELETE_ReturnCode404_DeleteSubtask_WithWrongId() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:8080/api/tasks/subtask?id=55");
        HttpRequest request = HttpRequest.newBuilder().DELETE().uri(uri).build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(404, response.statusCode(), "Не верный код ответа");

        assertEquals(2, taskManager.getAllSubtasks().size(), "Не верное количество задач");

        Type type = new TypeToken<MessageResp>() {
        }.getType();
        MessageResp actual = gson.fromJson(response.body(), type);

        assertNotNull(actual, "Сообщение не возвращается.");
        assertEquals("Задачи с id 55 не найдено.", actual.getMessage(), "Сообщения не совпадают.");
    }

    @Test
    public void subtaskDELETE_ReturnCode400_DeleteSubtask_WithNotCorrectId() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:8080/api/tasks/subtask?id=wrong");
        HttpRequest request = HttpRequest.newBuilder().DELETE().uri(uri).build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(400, response.statusCode(), "Не верный код ответа");

        Type type = new TypeToken<MessageResp>() {
        }.getType();
        MessageResp actual = gson.fromJson(response.body(), type);

        assertNotNull(actual, "Сообщение не возвращается.");
        assertEquals("Вы использовали неверный запрос.", actual.getMessage(), "Сообщения не совпадают.");
    }

    @Test
    public void subtaskDELETE_ReturnCode204_DeleteAllSubtasks_Normal() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:8080/api/tasks/subtask");
        HttpRequest request = HttpRequest.newBuilder().DELETE().uri(uri).build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(204, response.statusCode(), "Не верный код ответа");

        assertEquals(0, taskManager.getAllSubtasks().size(), "Не верное количество задач");
    }

    @Test
    public void subtaskDELETE_ReturnCode404_DeleteAllSubtasks_WithEmptySubtasks() throws IOException, InterruptedException {
        taskManager.deleteAllSubtasks();

        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:8080/api/tasks/subtask");
        HttpRequest request = HttpRequest.newBuilder().DELETE().uri(uri).build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(404, response.statusCode(), "Не верный код ответа");

        assertEquals(0, taskManager.getAllSubtasks().size(), "Не верное количество задач");

        Type type = new TypeToken<MessageResp>() {
        }.getType();
        MessageResp actual = gson.fromJson(response.body(), type);

        assertNotNull(actual, "Сообщение не возвращается.");
        assertEquals("Задач не найдено.", actual.getMessage(), "Сообщения не совпадают.");
    }
    //endregion

    //region subtasks by epic
    @Test
    public void subtasksEpic_ReturnCode405_WrongMethod() throws IOException, InterruptedException {
        HttpRequest.BodyPublisher bodyPublisher
                = HttpRequest.BodyPublishers.ofString(gson.toJson("{\"a\":\"b\"}"));

        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:8080/api/tasks/subtask/epic?id=" + epic.getId());
        HttpRequest request = HttpRequest.newBuilder().uri(uri).method("OPTION", bodyPublisher).build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(405, response.statusCode(), "Не верный код ответа");

        Type type = new TypeToken<MessageResp>() {
        }.getType();
        MessageResp actual = gson.fromJson(response.body(), type);

        assertNotNull(actual, "Сообщение не возвращается.");
        assertEquals("Метод запроса не поддерживается.", actual.getMessage(), "Сообщения не совпадают.");
    }

    @Test
    public void subtasksEpicGET_ReturnAllSubtaskByEpic_Normal() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:8080/api/tasks/subtask/epic?id=" + epic.getId());
        HttpRequest request = HttpRequest.newBuilder().GET().uri(uri).build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode(), "Не верный код ответа");

        Type type = new TypeToken<ArrayList<Subtask>>() {
        }.getType();
        List<Subtask> actual = gson.fromJson(response.body(), type);

        assertNotNull(actual, "Задача не возвращается");
        assertEquals(2, actual.size(), "Не верное количество задач");
        assertEquals(subtask, actual.get(0), "Задачи не совпадают");
    }

    @Test
    public void subtasksEpicGET_ReturnCode400_WithNoQueryString() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:8080/api/tasks/subtask/epic");
        HttpRequest request = HttpRequest.newBuilder().GET().uri(uri).build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(400, response.statusCode(), "Не верный код ответа");

        Type type = new TypeToken<MessageResp>() {
        }.getType();
        MessageResp actual = gson.fromJson(response.body(), type);

        assertNotNull(actual, "Сообщение не возвращается.");
        assertEquals("Вы использовали неверный запрос.", actual.getMessage(), "Сообщения не совпадают.");
    }

    @Test
    public void subtasksEpicGET_ReturnCode400_WithNotCorrectId() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:8080/api/tasks/subtask/epic?id=wrong");
        HttpRequest request = HttpRequest.newBuilder().GET().uri(uri).build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(400, response.statusCode(), "Не верный код ответа");

        Type type = new TypeToken<MessageResp>() {
        }.getType();
        MessageResp actual = gson.fromJson(response.body(), type);

        assertNotNull(actual, "Сообщение не возвращается.");
        assertEquals("Вы использовали неверный запрос.", actual.getMessage(), "Сообщения не совпадают.");
    }

    @Test
    public void subtaskEpicGET_ReturnCode404_WithWrongId() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:8080/api/tasks/subtask/epic?id=55");
        HttpRequest request = HttpRequest.newBuilder().GET().uri(uri).build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(404, response.statusCode(), "Не верный код ответа");

        Type type = new TypeToken<MessageResp>() {
        }.getType();
        MessageResp actual = gson.fromJson(response.body(), type);

        assertNotNull(actual, "Сообщение не возвращается.");
        assertEquals("Задачи с id 55 не найдено.", actual.getMessage(), "Сообщения не совпадают.");

    }
    //endregion

    //region history
    @Test
    public void history_ReturnCode405_WrongMethod() throws IOException, InterruptedException {
        HttpRequest.BodyPublisher bodyPublisher
                = HttpRequest.BodyPublishers.ofString(gson.toJson("{\"a\":\"b\"}"));

        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:8080/api/tasks/history");
        HttpRequest request = HttpRequest.newBuilder().uri(uri).method("OPTION", bodyPublisher).build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(405, response.statusCode(), "Не верный код ответа");

        Type type = new TypeToken<MessageResp>() {
        }.getType();
        MessageResp actual = gson.fromJson(response.body(), type);

        assertNotNull(actual, "Сообщение не возвращается.");
        assertEquals("Метод запроса не поддерживается.", actual.getMessage(), "Сообщения не совпадают.");
    }

    @Test
    public void history_ReturnHistory_GETHistoryList() throws IOException, InterruptedException {
        taskManager.getTask(task.getId());
        taskManager.getEpic(epic.getId());
        taskManager.getSubtask(subtask.getId());

        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:8080/api/tasks/history");
        HttpRequest request = HttpRequest.newBuilder().GET().uri(uri).build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode(), "Не верный код ответа");

        Type type = new TypeToken<ArrayList<BaseTask>>() {
        }.getType();

        List<BaseTask> actual = gson.fromJson(response.body(), type);

        assertNotNull(actual, "Истории не возвращаются");
        assertEquals(3, actual.size(), "Не верное количество задач");
        assertTrue(actual.get(0) instanceof Task);
        assertTrue(actual.get(2) instanceof Subtask);
    }

    @Test
    public void history_ReturnHistory_GETEmptyHistory() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:8080/api/tasks/history");
        HttpRequest request = HttpRequest.newBuilder().GET().uri(uri).build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode(), "Не верный код ответа");

        Type type = new TypeToken<ArrayList<BaseTask>>() {
        }.getType();
        List<BaseTask> actual = gson.fromJson(response.body(), type);

        assertNotNull(actual, "Истории не возвращаются");
        assertEquals(0, actual.size(), "Не верное количество задач");
    }
    //endregion
}