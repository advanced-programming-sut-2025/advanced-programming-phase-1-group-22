package io.github.some_example_name.saveGame;

import com.fasterxml.jackson.databind.KeyDeserializer;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.io.IOException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.some_example_name.model.Salable;

public class SalableKeyDeserializer extends KeyDeserializer {
    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public Salable deserializeKey(String key, DeserializationContext ctxt) throws IOException {
        int sep = key.indexOf('|');
        if (sep < 0) {
            throw new IOException("Malformed key: missing '|': " + key);
        }

        String className = key.substring(0, sep);
        String json = key.substring(sep + 1);

        try {
            Class<?> clazz = Class.forName(className);
            Object obj = mapper.readValue(json, clazz);
            return (Salable) obj;
        } catch (ClassNotFoundException e) {
            throw new IOException("Class not found: " + className, e);
        } catch (JsonProcessingException e) {
            throw new IOException("Failed to parse JSON in key: " + key, e);
        }
    }
}

