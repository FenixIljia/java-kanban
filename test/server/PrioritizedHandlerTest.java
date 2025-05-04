package server;

import data.*;
import manager.Managers;
import manager.TaskManager;
import server.adapter.*;
import exception.ManagerIntersectionException;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
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

public class PrioritizedHandlerTest {
    Type taskListType = new TypeToken<List<Task>>() {
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
    public void getPrioritizedList() throws IOException, InterruptedException, ManagerIntersectionException {
        Task task1 = new Task(
                "Test1",
                "Task1",
                Status.NEW,
                Variety.TASK,
                Duration.ofMinutes(10),
                LocalDateTime.of(2000, 1, 1, 1, 1, 0)
        );

        Task task2 = new Task(
                "Test2",
                "Task2",
                Status.NEW,
                Variety.TASK,
                Duration.ofMinutes(10),
                LocalDateTime.of(2001, 1, 1, 1, 1, 0)
        );

        Epic epic1 = new Epic("Test3", "Epic1", Variety.EPIC);
        Epic epic2 = new Epic("Test4", "Epic2", Variety.EPIC);

        taskManager.addEpic(epic1);

        SubTask subTask1 = new SubTask(
                "Test5",
                "In Data.Epic 1",
                epic1.getIdentifier(),
                Status.NEW,
                Variety.SUBTASK,
                Duration.ofMinutes(10),
                LocalDateTime.of(2002, 1, 1, 1, 1, 0)
        );

        SubTask subTask2 = new SubTask(
                "Test6",
                "In Data.Epic 1",
                epic1.getIdentifier(),
                Status.NEW,
                Variety.SUBTASK,
                Duration.ofMinutes(10),
                LocalDateTime.of(2003, 1, 1, 1, 1, 0)
        );

        SubTask subTask3 = new SubTask(
                "Test7",
                "In Data.Epic 1",
                epic1.getIdentifier(),
                Status.NEW,
                Variety.SUBTASK,
                Duration.ofMinutes(10),
                LocalDateTime.of(2004, 1, 1, 1, 1, 0)
        );

        taskManager.addTask(task1);
        taskManager.addTask(task2);
        taskManager.addEpic(epic2);
        taskManager.addSubTask(subTask1);
        taskManager.addSubTask(subTask2);
        taskManager.addSubTask(subTask3);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/prioritized");

        HttpRequest request = HttpRequest
                .newBuilder()
                .uri(url)
                .GET()
                .version(HttpClient.Version.HTTP_1_1)
                .headers("Accept", "application/json")
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        List<Task> treeSet = gson.fromJson(response.body(), taskListType);
        ArrayList<Task> taskList = new ArrayList<>(taskManager.getPrioritizedTasks());
        Assertions.assertEquals(treeSet.getFirst(), taskList.getFirst());
    }
}
