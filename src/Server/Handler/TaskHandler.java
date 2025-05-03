package Server.Handler;

import Data.Task;
import Exception.ManagerIntersectionException;
import Manager.TaskManager;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.net.URI;
import java.nio.charset.StandardCharsets;

public class TaskHandler extends BaseHttpHandler implements HttpHandler {

    public TaskHandler(TaskManager taskManager) {
        super(taskManager);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        String method = exchange.getRequestMethod();
        URI url = exchange.getRequestURI();
        String body = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);

        switch (method) {
            case "GET":
                if (checkLength(url)) {
                    sendText(exchange, createGson().toJson(getTaskManager().getAllTasks()));
                } else {
                    try {
                        sendText(exchange, createGson().toJson(getTaskManager().getTask(getId(url))));
                    } catch (NullPointerException e) {
                        sendNotFound(exchange);
                    }
                }
                break;
            case "POST":
                Task task = createGson().fromJson(body, Task.class);

                try {
                    if (task.getIdentifier() == 0) {
                        getTaskManager().addTask(createGson().fromJson(body, Task.class));
                        send(exchange);
                    } else {
                        getTaskManager().upDateTask(task);
                        send(exchange);
                    }
                } catch (ManagerIntersectionException e) {
                    sendHasInteractions(exchange);
                }
                break;
            case "DELETE":
                getTaskManager().removeTask(getId(url));
                exchange.sendResponseHeaders(200, 0);
                exchange.close();
        }
    }
}
