package Server.Handler;

import Data.Epic;
import Manager.TaskManager;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.net.URI;
import java.nio.charset.StandardCharsets;

public class EpicHandler extends BaseHttpHandler implements HttpHandler {

    public EpicHandler(TaskManager taskManager) {
        super(taskManager);
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
                        sendText(exchange, createGson().toJson(getTaskManager().getSubTaskSpecificEpic(getId(url))));
                        break;
                    } catch (NullPointerException e) {
                        sendNotFound(exchange);
                    }
                }
            if (checkLength(url)) {
                sendText(exchange, createGson().toJson(getTaskManager().getAllEpics()));
            } else {
                try {
                    sendText(exchange, createGson().toJson(getTaskManager().getEpic(getId(url))));
                } catch (NullPointerException e) {
                    sendNotFound(exchange);
                }
            }
            break;
            case "POST":
                Epic epic = createGson().fromJson(body, Epic.class);
                if (epic.getIdentifier() == 0) {
                    getTaskManager().addEpic(epic);
                    send(exchange);
                } else {
                    getTaskManager().upDateEpic(epic);
                    send(exchange);
                    }
                break;
            case "DELETE":
                getTaskManager().removeEpic(getId(url));
                exchange.sendResponseHeaders(200, 0);
                exchange.close();
        }
    }
}

