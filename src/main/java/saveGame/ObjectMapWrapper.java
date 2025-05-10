package saveGame;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ObjectMapWrapper {
    public List<Entry> entries = new ArrayList<>();

    public ObjectMapWrapper() {
    }

    public ObjectMapWrapper(Map<Object, Integer> map, ObjectMapper objectMapper) {
        for (Map.Entry<Object, Integer> entry : map.entrySet()) {
            entries.add(new Entry(new ObjectWrapper(entry.getKey(), objectMapper), entry.getValue()));
        }
    }

    public Map<Object, Integer> toMap(ObjectMapper objectMapper) {
        Map<Object, Integer> result = new HashMap<>();
        for (Entry entry : entries) {
            result.put(entry.objectWrapper.toObject(objectMapper), entry.amount);
        }
        return result;
    }

    public static class Entry {
        public ObjectWrapper objectWrapper;
        public int amount;

        public Entry() {
        }

        public Entry(ObjectWrapper objectWrapper, int amount) {
            this.objectWrapper = objectWrapper;
            this.amount = amount;
        }

        public Entry(ObjectWrapper actorWrapper, String key) {
        }
    }
}
