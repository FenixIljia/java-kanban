package Server.Handler;

import Data.SubTask;
import Exception.ManagerIntersectionException;
import Manager.TaskManager;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.net.URI;
import java.nio.charset.StandardCharsets;

public class SubTaskHandler extends BaseHttpHandler implements HttpHandler {

    public SubTaskHandler(TaskManager taskManager) {
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
                    sendText(exchange, createGson().toJson(getTaskManager().getAllSubTasks()));
                } else {
                    try {
                        sendText(exchange, createGson().toJson(getTaskManager().getSubTask(getId(url))));
                    } catch (NullPointerException e) {
                        sendNotFound(exchange);
                    }
                }
                break;
            case "POST":
                SubTask subTask = createGson().fromJson(body, SubTask.class);
                try {
                    if (subTask.getIdentifier() == 0) {
                        getTaskManager().addSubTask(subTask);
                        send(exchange);
                    } else {
                        getTaskManager().upDateSubTask(subTask);
                        send(exchange);
                    }
                } catch (ManagerIntersectionException e) {
                    sendHasInteractions(exchange);
                }
                break;
            case "DELETE":
                getTaskManager().removeSubtask(getId(url));
                exchange.sendResponseHeaders(200, 0);
                exchange.close();
        }
    }
}
