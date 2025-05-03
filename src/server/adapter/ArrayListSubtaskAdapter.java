package server.adapter;

import data.SubTask;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.util.ArrayList;

public class ArrayListSubtaskAdapter extends TypeAdapter<ArrayList<SubTask>> {

    @Override
    public void write(JsonWriter out, ArrayList<SubTask> subTasks) throws IOException {
        if (subTasks == null) {
            throw new IOException("Data.SubTask list cannot be null"); // По условию список не null
        }

        out.beginArray();
        for (SubTask subTask : subTasks) {
            // Используем Gson для сериализации каждого Data.SubTask
            new SubTaskAdapter().write(out, subTask);
        }
        out.endArray();
    }

    @Override
    public ArrayList<SubTask> read(JsonReader in) throws IOException {
        ArrayList<SubTask> subTasks = new ArrayList<>();

        if (in.peek() == JsonToken.NULL) {
            in.nextNull();
            throw new IOException("Data.SubTask list cannot be null"); // Защита от null
        }

        in.beginArray();
        while (in.hasNext()) {
            SubTask subTask = new SubTaskAdapter().read(in);
            subTasks.add(subTask);
        }
        in.endArray();

        return subTasks;
    }
}