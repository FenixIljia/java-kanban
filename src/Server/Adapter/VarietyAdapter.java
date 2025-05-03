package Server.Adapter;

import Data.Variety;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;

public class VarietyAdapter extends TypeAdapter<Variety> {
    @Override
    public void write(JsonWriter jsonWriter, Variety variety) throws IOException {
        jsonWriter.value(variety.toString());
    }

    @Override
    public Variety read(JsonReader jsonReader) throws IOException {
        switch (jsonReader.nextString()) {
            case "TASK":
                return Variety.TASK;
            case "EPIC":
                return Variety.EPIC;
            default:
                return Variety.SUBTASK;
        }
    }
}
