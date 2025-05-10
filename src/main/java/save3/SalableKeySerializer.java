package save3;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import java.io.IOException;
import com.fasterxml.jackson.databind.ObjectMapper;
import model.Salable;

public class SalableKeySerializer extends JsonSerializer<Salable> {
    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public void serialize(Salable value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        String className = value.getClass().getName();
        String objJson = mapper.writeValueAsString(value);
        String combinedKey = className + "|" + objJson;
        gen.writeFieldName(combinedKey);
    }
}