package save;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import model.Salable;

import java.io.IOException;

public class SalableKeySerializer extends JsonSerializer<Salable> {
	@Override
	public void serialize(Salable salable, JsonGenerator gen, SerializerProvider serializers) throws IOException {
		gen.writeFieldName(salable.getName()); // یا هر ویژگی یکتای دیگر
	}
}
