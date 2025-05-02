import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class BaseHttpHandler{

    private final Path file = Paths.get(
            "D:" +
                    "\\Программирование" +
                    "\\Проекты на JavaScript" +
                    "\\Яндекс Практикум" +
                    "\\Проект Java Practicum" +
                    "\\Test.txt");
    private final Path fileHistory = Paths.get(
            "D:" +
                    "\\Программирование" +
                    "\\Проекты на JavaScript" +
                    "\\Яндекс Практикум" +
                    "\\Проект Java Practicum" +
                    "\\TaskHistory.txt");

    private final FileBackedTaskManager fileBackedTaskManager = FileBackedTaskManager.loadFromFile(file, fileHistory);

    protected void sendText (HttpExchange httpExchange, String text) throws IOException{
        byte[] resp = text.getBytes(StandardCharsets.UTF_8);
        httpExchange.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
        httpExchange.sendResponseHeaders(200, resp.length);
        httpExchange.getResponseBody().write(resp);
        httpExchange.close();
    }

    public void sendNotFound (HttpExchange httpExchange) throws IOException{
        httpExchange.sendResponseHeaders(404, 0);
        httpExchange.close();
    }

    public void sendHasInteractions (HttpExchange httpExchange) throws IOException{
        httpExchange.sendResponseHeaders(406, 0);
        httpExchange.close();
    }

    protected void send (HttpExchange httpExchange) throws IOException{
        httpExchange.sendResponseHeaders(201, 0);
        httpExchange.close();
    }

    //Проверка URI на наличия id задач. Если false, значит id есть
    protected boolean checkLength(URI url) {
        return url.getPath().split("/").length < 3;
    }

    //Получение id задачи
    protected int getId(URI url) {
        return Integer.parseInt(url.getPath().split("/")[2]);
    }

    //Создание json обьекта для сериализации и десирализации
    protected Gson createGson() {
        return new GsonBuilder()
                .setPrettyPrinting()
                .serializeNulls()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                .registerTypeAdapter(Duration.class, new DurationAdapter())
                .registerTypeAdapter(Status.class, new StatusAdapter())
                .registerTypeAdapter(VarietyAdapter.class, new VarietyAdapter())
                .registerTypeAdapter(SubTask.class, new SubTaskAdapter())
                .registerTypeAdapter(new TypeToken<ArrayList<SubTask>>() {
                }.getType(), new ArrayListSubtaskAdapter())
                .create();
    }
}
