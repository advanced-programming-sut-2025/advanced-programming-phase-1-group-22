package io.github.some_example_name.server.saveGame;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ObjectWrapper {
    public String className;
    public JsonNode data;

    public ObjectWrapper() {
    }

    public ObjectWrapper(Object obj, ObjectMapper objectMapper) {
        this.className = obj.getClass().getName();
        this.data = objectMapper.valueToTree(obj);
    }

    public Object toObject(ObjectMapper objectMapper) {
        try {
            Class<?> clazz = Class.forName(className);
            return objectMapper.treeToValue(data, clazz);
        } catch (Exception e) {
            throw new RuntimeException("Failed to deserialize object of type: " + className, e);
        }
    }
}
