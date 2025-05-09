package save3;

import com.fasterxml.jackson.databind.ObjectMapper;
import model.Game;

import java.io.File;
import java.io.IOException;

public class GameSaver {
    public static final ObjectMapper objectMapper = new ObjectMapper();

    public static void saveGame(Game game, String filename) throws IOException {
        game.prepareForSave(objectMapper);
        objectMapper.writeValue(new File(filename), game);
    }

    public static Game loadGame(String filename) throws IOException {
        Game game = objectMapper.readValue(new File(filename), Game.class);
        game.unpackAfterLoad(objectMapper);
        return game;
    }
}
