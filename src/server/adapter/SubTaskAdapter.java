package server.adapter;

import data.Status;
import data.SubTask;
import data.Variety;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;

public class SubTaskAdapter extends TypeAdapter<SubTask> {

    @Override
    public void write(JsonWriter out, SubTask subTask) throws IOException {
        if (subTask == null) {
            out.nullValue();
            return;
        }

        out.beginObject();

        // Поля из родительского класса Data.Task
        out.name("name").value(subTask.getName());
        out.name("description").value(subTask.getDescription());
        out.name("identifier").value(subTask.getIdentifier());
        out.name("status").value(subTask.getStatus().toString()); // Используется Server.Adapter.StatusAdapter
        out.name("variety").value(subTask.getVariety().toString()); // Используется Server.Adapter.VarietyAdapter

        // Поля LocalDateTime и Duration (используют зарегистрированные адаптеры)
        if (subTask.getDuration() != null) {
            out.name("duration");
            new DurationAdapter().write(out, subTask.getDuration());
        }
        if (subTask.getStartTime() != null) {
            out.name("startTime");
            new LocalDateTimeAdapter().write(out, subTask.getStartTime());
        }

        // Поле из Data.SubTask
        out.name("idMasterTask").value(subTask.getIdMasterTask());

        out.endObject();
    }

    @Override
    public SubTask read(JsonReader in) throws IOException {
        if (in.peek() == JsonToken.NULL) {
            in.nextNull();
            return null;
        }

        in.beginObject();

        String name = null;
        String description = null;
        int identifier = 0;
        Status status = Status.NEW;
        Variety variety = Variety.SUBTASK;
        Duration duration = null;
        LocalDateTime startTime = null;
        Integer idMasterTask = null;

        while (in.hasNext()) {
            String field = in.nextName();
            switch (field) {
                case "name":
                    name = in.nextString();
                    break;
                case "description":
                    description = in.nextString();
                    break;
                case "identifier":
                    identifier = in.nextInt();
                    break;
                case "status":
                    status = new StatusAdapter().read(in); // Используется Server.Adapter.StatusAdapter
                    break;
                case "variety":
                    variety = new VarietyAdapter().read(in); // Используется Server.Adapter.VarietyAdapter
                    break;
                case "duration":
                    duration = new DurationAdapter().read(in);
                    break;
                case "startTime":
                    startTime = new LocalDateTimeAdapter().read(in);
                    break;
                case "idMasterTask":
                    if (in.peek() == JsonToken.NULL) {
                        in.nextNull();
                        idMasterTask = null;
                    } else {
                        idMasterTask = in.nextInt();
                    }
                    break;
                default:
                    in.skipValue();
                    break;
            }
        }
        in.endObject();

        // Используем конструктор с максимальным набором параметров
        return new SubTask(
                name,
                description,
                idMasterTask,
                identifier,
                status,
                variety,
                duration != null ? duration : Duration.ZERO,
                startTime
        );
    }
}