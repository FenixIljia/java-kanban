import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;
import java.util.stream.Collectors;

public class SubTaskHandler extends BaseHttpHandler implements HttpHandler {
    TaskManager taskManager;

    public SubTaskHandler(TaskManager taskManager) {
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
                    sendText(exchange, createGson().toJson(taskManager.getAllSubTasks()));
                } else {
                    try {
                        sendText(exchange, createGson().toJson(taskManager.getSubTask(getId(url))));
                    } catch (NullPointerException e) {
                        sendNotFound(exchange);
                    }
                }
                break;
            case "POST":
                SubTask subTask = createGson().fromJson(body, SubTask.class);
                try {
                    if (subTask.getIdentifier() == 0) {
                        taskManager.addSubTask(subTask);
                        send(exchange);
                    } else {
                        taskManager.upDateSubTask(subTask);
                        send(exchange);
                    }
                } catch (ManagerSaveException e) {
                    sendHasInteractions(exchange);
                }
                break;
            case "DELETE":
                taskManager.removeSubtask(getId(url));
                exchange.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
                exchange.sendResponseHeaders(200, 0);
                exchange.close();
        }
    }
}
