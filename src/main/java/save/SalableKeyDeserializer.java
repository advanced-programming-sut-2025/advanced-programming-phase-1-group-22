package save;

import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.KeyDeserializer;
import model.Salable;

import java.io.IOException;

public class SalableKeyDeserializer extends KeyDeserializer {
	@Override
	public Object deserializeKey(String key, DeserializationContext ctxt) throws IOException {
		// فرض بر این که از روی id می‌سازیم
	//	return new Salable(key); // سازنده‌ای که از ID بسازد
		return null;
	}
}

