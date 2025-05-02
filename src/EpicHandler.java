import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.net.URI;
import java.nio.charset.StandardCharsets;

public class EpicHandler extends BaseHttpHandler implements HttpHandler {
    TaskManager taskManager;

    public EpicHandler(TaskManager taskManager) {
        this.taskManager = taskManager;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        URI url = exchange.getRequestURI();
        String body = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);

        switch (method) {
            case "GET":
                if (url.getPath().endsWith("subtask")) {
                    try {
                        sendText(exchange, createGson().toJson(taskManager.getSubTaskSpecificEpic(getId(url))));
                        break;
                    } catch (NullPointerException e) {
                        sendNotFound(exchange);
                    }
                }
            if (checkLength(url)) {
                sendText(exchange, createGson().toJson(taskManager.getAllEpics()));
            } else {
                try {
                    sendText(exchange, createGson().toJson(taskManager.getEpic(getId(url))));
                } catch (NullPointerException e) {
                    sendNotFound(exchange);
                }
            }
            break;
            case "POST":
                Epic epic = createGson().fromJson(body, Epic.class);
                try {
                    if (epic.getIdentifier() == 0) {
                        taskManager.addEpic(epic);
                        send(exchange);
                    } else {
                        taskManager.upDateEpic(epic);
                        send(exchange);
                    }
                } catch (ManagerSaveException e) {
                    sendHasInteractions(exchange);
                }
                break;
            case "DELETE":
                taskManager.removeEpic(getId(url));
                exchange.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
                exchange.sendResponseHeaders(200, 0);
                exchange.close();
        }
    }
}

