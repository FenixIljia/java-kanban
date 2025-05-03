package Server;

import Data.Epic;
import Data.Status;
import Data.SubTask;
import Data.Variety;
import Manager.Managers;
import Manager.TaskManager;
import Server.Adapter.*;
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

public class EpicHandlerTask {
    Type taskListType = new TypeToken<List<Epic>>() {
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
    public void addEpic() throws IOException, InterruptedException {
        Epic task = new Epic("Test", "Description", Variety.EPIC);
        task.setIdentifier(0);

        String gsonTask = gson.toJson(task);
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/epics");
        HttpRequest request = HttpRequest
                .newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(gsonTask))
                .version(HttpClient.Version.HTTP_1_1)
                .headers("Accept", "application/json")
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response.statusCode());
        List<Epic> tasksFromManager = taskManager.getAllEpics();

        assertNotNull(tasksFromManager, "Задачи не возвращаются");
        assertEquals(1, tasksFromManager.size(), "Некорректное количество задач");
        assertEquals("Test", tasksFromManager.get(0).getName(), "Некорректное имя задачи");
    }

    @Test
    public void getAllEpics() throws IOException, InterruptedException {
        Epic task = new Epic("Test", "Description", Variety.EPIC);
        task.setIdentifier(0);

        taskManager.addEpic(task);
        SubTask subTask = new SubTask(
                "Subtask",
                "Description",
                task.getIdentifier(),
                Status.DONE,
                Variety.SUBTASK,
                Duration.ofMinutes(40),
                LocalDateTime.of(2000, 2, 2, 2, 2)
        );

        HttpClient client = HttpClient.newHttpClient();
        URI urlSubtask = URI.create("http://localhost:8080/subtasks");
        HttpRequest requestSubtask = HttpRequest
                .newBuilder()
                .uri(urlSubtask)
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(subTask)))
                .version(HttpClient.Version.HTTP_1_1)
                .headers("Accept", "application/json")
                .build();
        HttpResponse<String> responseSubtasks = client.send(requestSubtask, HttpResponse.BodyHandlers.ofString());

        URI url = URI.create("http://localhost:8080/epics");
        HttpRequest httpRequestGet = HttpRequest
                .newBuilder()
                .uri(url)
                .GET()
                .version(HttpClient.Version.HTTP_1_1)
                .headers("Accept", "application/json")
                .build();
        HttpResponse<String> responseGet = client.send(httpRequestGet, HttpResponse.BodyHandlers.ofString());
        ArrayList<Epic> taskGet = gson.fromJson(responseGet.body(), taskListType);

        assertEquals(taskManager.getAllEpics(), taskGet);
        assertEquals(200, responseGet.statusCode());


    }

    @Test
    public void getEpic() throws IOException, InterruptedException {
        Epic task = new Epic("Test", "Description", Variety.EPIC);
        taskManager.addEpic(task);

        HttpClient client = HttpClient.newHttpClient();
        URI urlGet = URI.create("http://localhost:8080/epics/" + task.getIdentifier());
        HttpRequest httpRequestGet = HttpRequest
                .newBuilder()
                .uri(urlGet)
                .GET()
                .version(HttpClient.Version.HTTP_1_1)
                .headers("Accept", "application/json")
                .build();
        HttpResponse<String> responseGet = client.send(httpRequestGet, HttpResponse.BodyHandlers.ofString());
        Epic taskGet = gson.fromJson(responseGet.body(), Epic.class);
        assertEquals(200, responseGet.statusCode());
        assertEquals(task, taskGet);
    }

    @Test
    public void updateEpic() throws IOException, InterruptedException {
        Epic task = new Epic("Test", "Description", Variety.EPIC);


        taskManager.addEpic(task);

        Epic taskUpdate = new Epic("Update", "Description", Variety.EPIC);

        taskUpdate.setIdentifier(task.getIdentifier());
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/epics/");

        HttpRequest request = HttpRequest
                .newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(taskUpdate)))
                .version(HttpClient.Version.HTTP_1_1)
                .headers("Accept", "application/json")
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response.statusCode());
        assertEquals(taskManager.getEpic(task.getIdentifier()), taskUpdate);
    }

    @Test
    public void getCode404AtNullEpic() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI urlGet = URI.create("http://localhost:8080/epics/2");
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
    public void getCode200AtDelete() throws IOException, InterruptedException {
        Epic task = new Epic("Test", "Description", Variety.EPIC);


        taskManager.addEpic(task);

        int identifier = task.getIdentifier();

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/epics/" + task.getIdentifier());

        HttpRequest request = HttpRequest
                .newBuilder()
                .uri(url)
                .DELETE()
                .version(HttpClient.Version.HTTP_1_1)
                .headers("Accept", "application/json")
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        assertThrows(NullPointerException.class, () -> taskManager.getEpic(identifier));
    }

}
