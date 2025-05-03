package Server;

import Data.*;
import Manager.Managers;
import Manager.TaskManager;
import Server.Adapter.*;
import Exception.ManagerIntersectionException;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class SubTaskHandlerTest {
    Type taskListType = new TypeToken<List<SubTask>>() {
    }.getType();
    TaskManager taskManager = Managers.getDefault();
    HttpTaskServer httpTaskServer;
    Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .serializeNulls()
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
            .registerTypeAdapter(Duration.class, new DurationAdapter())
            .registerTypeAdapter(Status.class, new StatusAdapter())
            .registerTypeAdapter(VarietyAdapter.class, new VarietyAdapter())
            .registerTypeAdapter(SubTask.class, new SubTaskAdapter())
            .registerTypeAdapter(new TypeToken<ArrayList<SubTask>>() {
            }
                    .getType(), new ArrayListSubtaskAdapter())
            .create();

    @BeforeEach
    public void startServerInTest() throws InterruptedException {
        httpTaskServer = new HttpTaskServer(taskManager);
        taskManager.clearAllEpic();
        taskManager.clearAllSubTasks();
        taskManager.clearAllTasks();
        httpTaskServer.startServer();
    }

    @AfterEach
    public void stopServerInTest() {
        httpTaskServer.stopServer();
    }

    @Test
    public void addSubTask() throws IOException, InterruptedException {
        Epic epic = new Epic("Data.Epic", "Description", Variety.EPIC);

        taskManager.addEpic(epic);

        SubTask task = new SubTask(
                "Test",
                "Description",
                epic.getIdentifier(),
                Status.NEW,
                Variety.SUBTASK,
                Duration.ofMinutes(50),
                LocalDateTime.of(2000, 1, 2, 2, 2)
        );
        task.setIdentifier(0);

        String gsonTask = gson.toJson(task);
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/subtasks");
        HttpRequest request = HttpRequest
                .newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(gsonTask))
                .version(HttpClient.Version.HTTP_1_1)
                .headers("Accept", "application/json")
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response.statusCode());
        List<SubTask> tasksFromManager = taskManager.getAllSubTasks();

        assertNotNull(tasksFromManager, "Задачи не возвращаются");
        assertEquals(1, tasksFromManager.size(), "Некорректное количество задач");
        assertEquals("Test", tasksFromManager.get(0).getName(), "Некорректное имя задачи");
    }

    @Test
    public void getAllSubTask() throws IOException, InterruptedException {
        Epic epic = new Epic("Data.Epic", "Description", Variety.EPIC);

        taskManager.addEpic(epic);

        SubTask task = new SubTask(
                "Test",
                "Description",
                epic.getIdentifier(),
                Status.NEW,
                Variety.SUBTASK,
                Duration.ofMinutes(50),
                LocalDateTime.of(2000, 1, 2, 2, 2)
        );
        task.setIdentifier(0);

        String gsonTask = gson.toJson(task);
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/subtasks");
        HttpRequest request = HttpRequest
                .newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(gsonTask))
                .version(HttpClient.Version.HTTP_1_1)
                .headers("Accept", "application/json")
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        HttpRequest httpRequestGet = HttpRequest
                .newBuilder()
                .uri(url)
                .GET()
                .version(HttpClient.Version.HTTP_1_1)
                .headers("Accept", "application/json")
                .build();
        HttpResponse<String> responseGet = client.send(httpRequestGet, HttpResponse.BodyHandlers.ofString());
        ArrayList<SubTask> taskGet = gson.fromJson(responseGet.body(), taskListType);

        assertEquals(taskManager.getAllSubTasks(), taskGet);
        assertEquals(200, responseGet.statusCode());


    }

    @Test
    public void getSubTask() throws IOException, InterruptedException, ManagerIntersectionException {
        Epic epic = new Epic("Data.Epic", "Description", Variety.EPIC);

        taskManager.addEpic(epic);

        SubTask task = new SubTask(
                "Test",
                "Description",
                epic.getIdentifier(),
                Status.NEW,
                Variety.SUBTASK,
                Duration.ofMinutes(50),
                LocalDateTime.of(2000, 1, 2, 2, 2)
        );
        taskManager.addSubTask(task);

        HttpClient client = HttpClient.newHttpClient();
        URI urlGet = URI.create("http://localhost:8080/subtasks/" + task.getIdentifier());
        HttpRequest httpRequestGet = HttpRequest
                .newBuilder()
                .uri(urlGet)
                .GET()
                .version(HttpClient.Version.HTTP_1_1)
                .headers("Accept", "application/json")
                .build();
        HttpResponse<String> responseGet = client.send(httpRequestGet, HttpResponse.BodyHandlers.ofString());
        Task taskGet = gson.fromJson(responseGet.body(), SubTask.class);
        assertEquals(task, taskGet);
        assertEquals(200, responseGet.statusCode());
    }

    @Test
    public void updateSubTask() throws IOException, InterruptedException, ManagerIntersectionException {
        Epic epic = new Epic("Data.Epic", "Description", Variety.EPIC);

        taskManager.addEpic(epic);

        SubTask task = new SubTask(
                "Test",
                "Description",
                epic.getIdentifier(),
                Status.NEW,
                Variety.SUBTASK,
                Duration.ofMinutes(50),
                LocalDateTime.of(2000, 1, 2, 2, 2)
        );

        taskManager.addSubTask(task);

        SubTask taskUpdate = new SubTask(
                "Update",
                "Description",
                epic.getIdentifier(),
                Status.DONE,
                Variety.SUBTASK,
                Duration.ofMinutes(30),
                LocalDateTime.of(1000, 1, 1, 1, 1)
        );

        taskUpdate.setIdentifier(task.getIdentifier());
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/subtasks/");

        HttpRequest request = HttpRequest
                .newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(taskUpdate)))
                .version(HttpClient.Version.HTTP_1_1)
                .headers("Accept", "application/json")
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response.statusCode());
        assertEquals(taskManager.getSubTask(task.getIdentifier()), taskUpdate);
    }

    @Test
    public void getCode404AtNullSubTask() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI urlGet = URI.create("http://localhost:8080/subtasks/2");
        HttpRequest httpRequestGet = HttpRequest
                .newBuilder()
                .uri(urlGet)
                .GET()
                .version(HttpClient.Version.HTTP_1_1)
                .headers("Accept", "application/json")
                .build();
        HttpResponse<String> responseGet = client.send(httpRequestGet, HttpResponse.BodyHandlers.ofString());
        assertEquals(404, responseGet.statusCode());
    }

    @Test
    public void getCode406AtIntersectionSubTaskAtAddSubTask() throws IOException, InterruptedException, ManagerIntersectionException {
        Epic epic = new Epic("Data.Epic", "Description", Variety.EPIC);

        taskManager.addEpic(epic);

        SubTask task = new SubTask(
                "Test",
                "Description",
                epic.getIdentifier(),
                Status.NEW,
                Variety.SUBTASK,
                Duration.ofMinutes(50),
                LocalDateTime.of(2000, 1, 2, 2, 2)
        );

        taskManager.addSubTask(task);

        SubTask taskUpdate = new SubTask(
                "Update",
                "Description",
                epic.getIdentifier(),
                Status.DONE,
                Variety.SUBTASK,
                Duration.ofMinutes(30),
                LocalDateTime.of(2000, 1, 2, 2, 10)
        );

        taskUpdate.setIdentifier(0);
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/subtasks");

        HttpRequest request = HttpRequest
                .newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(taskUpdate)))
                .version(HttpClient.Version.HTTP_1_1)
                .headers("Accept", "application/json")
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(406, response.statusCode());
    }

    @Test
    public void getCode200AtDeleteSubTask() throws IOException, InterruptedException, ManagerIntersectionException {
        Task task = new Task(
                "Test",
                "Description",
                Status.NEW,
                Variety.TASK,
                Duration.ofMinutes(50),
                LocalDateTime.of(2000, 1, 2, 2, 2)
        );

        taskManager.addTask(task);
        int identifier = task.getIdentifier();
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/" + task.getIdentifier());

        HttpRequest request = HttpRequest
                .newBuilder()
                .uri(url)
                .DELETE()
                .version(HttpClient.Version.HTTP_1_1)
                .headers("Accept", "application/json")
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
        assertThrows(NullPointerException.class, () -> taskManager.getSubTask(identifier));
    }
}
