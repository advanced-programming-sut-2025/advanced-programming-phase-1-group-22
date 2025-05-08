package save;


import com.google.gson.*;

import java.io.IOException;
import java.lang.reflect.Type;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.lang.reflect.Type;
import com.google.gson.*;
import java.io.IOException;
import java.lang.reflect.Type;

/**
 * Handles serialization/deserialization for all classes in the model package
 * while preserving concrete runtime types.
 */
public class ModelPackageAdapter<T> extends TypeAdapter<T> {
    private static final String CLASS_KEY = "_class";
    private final Gson gson;
    private final TypeAdapter<T> delegate;
    private final TypeAdapter<JsonElement> elementAdapter;

    public ModelPackageAdapter(Gson gson, TypeToken<T> type) {
        this.gson = gson;
        this.delegate = gson.getDelegateAdapter((TypeAdapterFactory) this, type);
        this.elementAdapter = gson.getAdapter(JsonElement.class);
    }

    @Override
    public void write(JsonWriter out, T value) throws IOException {
        JsonElement tree = delegate.toJsonTree(value);
        if (tree.isJsonObject()) {
            tree.getAsJsonObject().addProperty(CLASS_KEY, value.getClass().getName());
        }
        elementAdapter.write(out, tree);
    }

    @Override
    public T read(JsonReader in) throws IOException {
        JsonElement tree = elementAdapter.read(in);
        if (tree.isJsonObject()) {
            JsonObject obj = tree.getAsJsonObject();
            if (obj.has(CLASS_KEY)) {
                try {
                    Class<?> clazz = Class.forName(obj.get(CLASS_KEY).getAsString());
                    return (T) gson.fromJson(tree, clazz);
                } catch (ClassNotFoundException e) {
                    throw new JsonParseException("Unknown model class: " + obj.get(CLASS_KEY), e);
                }
            }
        }
        return delegate.fromJsonTree(tree);
    }
}