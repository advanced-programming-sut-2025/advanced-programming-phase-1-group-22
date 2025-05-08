package save;

import com.google.gson.*;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;

/**
 * Handles enum serialization consistently
 */
public class EnumTypeAdapter<T extends Enum<T>> extends TypeAdapter<T> {
    private final Class<T> enumClass;

    public EnumTypeAdapter(Class<T> enumClass) {
        this.enumClass = enumClass;
    }

    @Override
    public void write(JsonWriter out, T value) throws IOException {
        out.value(value != null ? value.name() : null);
    }

    @Override
    public T read(JsonReader in) throws IOException {
        return Enum.valueOf(enumClass, in.nextString());
    }
}