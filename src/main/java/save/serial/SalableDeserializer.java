package save.serial;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import model.Flower;
import model.Salable;
import model.source.Seed;
import model.source.SeedType;

import java.io.IOException;

public class SalableDeserializer extends JsonDeserializer<Salable> {
	@Override
	public Salable deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
		JsonNode node = p.getCodec().readTree(p);
		String type = node.get("type").asText();

		switch (type) {
			case "flower" -> {
				String seedName = node.get("flower").asText();
				return new Flower();
			}
			// بقیه‌ی کلاس‌های Salable را هم همین‌جا هندل کن
			default -> throw new IllegalArgumentException("Unknown Salable type: " + type);
		}
	}
}
