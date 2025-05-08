package save;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import org.reflections.Reflections;
import save.EnumTypeAdapter;

import java.io.IOException;
import java.nio.file.*;
import java.lang.reflect.Type;

/**
 * Manages saving and loading of game state with automatic type handling
 */
public final class GameSaveManager {
    private static final Gson GSON = createConfiguredGson();

    private GameSaveManager() {} // Prevent instantiation

    private static Gson createConfiguredGson() {
        return new GsonBuilder()
                .setPrettyPrinting()
                .enableComplexMapKeySerialization()
                .registerTypeAdapterFactory(createModelAdapterFactory())
                .registerTypeAdapterFactory(createEnumAdapterFactory())
                .create();
    }

    private static TypeAdapterFactory createModelAdapterFactory() {
        return new TypeAdapterFactory() {
            @Override
            public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
                if (isModelClass(type.getRawType())) {
                    return new ModelPackageAdapter<>(gson, type);
                }
                return null;
            }
        };
    }

    private static TypeAdapterFactory createEnumAdapterFactory() {
        return new TypeAdapterFactory() {
            @Override
            @SuppressWarnings({"unchecked", "rawtypes"})
            public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
                Class<?> rawType = type.getRawType();
                return rawType.isEnum() ?
                        (TypeAdapter<T>) new EnumTypeAdapter(rawType) :
                        null;
            }
        };
    }

    private static boolean isModelClass(Class<?> clazz) {
        return clazz.getPackage() != null &&
                clazz.getPackage().getName().startsWith("model");
    }

    public static void saveGame(Object gameState, Path filePath) throws IOException {
        Files.writeString(filePath, GSON.toJson(gameState));
    }

    public static <T> T loadGame(Path filePath, Class<T> type) throws IOException {
        return GSON.fromJson(Files.readString(filePath), type);
    }

    public static <T> T loadGame(Path filePath, Type type) throws IOException {
        return GSON.fromJson(Files.readString(filePath), type);
    }
}