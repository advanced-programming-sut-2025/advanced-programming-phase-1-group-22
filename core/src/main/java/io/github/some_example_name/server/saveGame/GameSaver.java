package io.github.some_example_name.server.saveGame;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import io.github.some_example_name.common.model.Lobby;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

public class GameSaver {
    private static final Gson GSON = new GsonBuilder().serializeNulls().create();

    public static void saveLobbies(List<Lobby> lobbies, String filePath) throws IOException {
        File file = new File(filePath);
        File parentDir = file.getParentFile();

        if (parentDir != null && !parentDir.exists()) {
            boolean created = parentDir.mkdirs();
            if (!created) {
                throw new IOException("Could not create directory: " + parentDir.getAbsolutePath());
            }
        }

        try (FileWriter writer = new FileWriter(filePath)) {
            String json = GSON.toJson(lobbies);
            writer.write(json);
        }
    }

    public static List<Lobby> loadLobbies(String filePath) throws IOException {
        File file = new File(filePath);
        if (!file.exists()) {
            throw new IOException("Save file not found: " + filePath);
        }

        try (FileReader reader = new FileReader(file)) {
            Type listType = new TypeToken<List<Lobby>>() {
            }.getType();
            return GSON.fromJson(reader, listType);
        }
    }
}
