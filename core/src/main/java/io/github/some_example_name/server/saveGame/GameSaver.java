package io.github.some_example_name.server.saveGame;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.github.some_example_name.server.model.GameServer;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class GameSaver {
    private static final Gson GSON = new GsonBuilder().serializeNulls().create();

    public static void saveGame(GameServer game, String filePath) throws Exception {
        File file = new File(filePath);
        File parentDir = file.getParentFile();

        if (parentDir != null && !parentDir.exists()) {
            boolean created = parentDir.mkdirs();
            if (!created) {
                throw new IOException("Could not create directory: " + parentDir.getAbsolutePath());
            }
        }

        try (FileWriter writer = new FileWriter(filePath)) {
            String json = GSON.toJson(game);
            writer.write(json);
        }
    }

    public static GameServer loadGame(String filePath) throws Exception {
        File file = new File(filePath);
        if (!file.exists()) {
            throw new IOException("Save file not found: " + filePath);
        }

        try (FileReader reader = new FileReader(file)) {
            return GSON.fromJson(reader, GameServer.class);
        }
    }
}
