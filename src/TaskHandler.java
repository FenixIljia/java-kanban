import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.stream.Collectors;

public class TaskHandler extends BaseHttpHandler implements HttpHandler {
    TaskManager taskManager = Managers.getDefault();

    public TaskHandler(TaskManager taskManager) {
        this.taskManager = taskManager;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        String method = exchange.getRequestMethod();
        URI url = exchange.getRequestURI();
        String body = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);

        switch (method) {
            case "GET":
                if (checkLength(url)) {
                    sendText(exchange, createGson().toJson(taskManager.getAllTasks()));
                } else {
                    try {
                        sendText(exchange, createGson().toJson(taskManager.getTask(getId(url))));
                    } catch (NullPointerException e) {
                        sendNotFound(exchange);
                    }
                }
                break;
            case "POST":
                Task task = createGson().fromJson(body, Task.class);

                try {
                    if (task.getIdentifier() == 0) {
                        taskManager.addTask(createGson().fromJson(body, Task.class));
                        send(exchange);
                    } else {
                       taskManager.upDateTask(task);
                        send(exchange);
                    }
                } catch (ManagerSaveException e) {
                    sendHasInteractions(exchange);
                }
                break;
            case "DELETE":
                taskManager.removeTask(getId(url));
                exchange.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
                exchange.sendResponseHeaders(200, 0);
                exchange.close();
        }
    }
}
