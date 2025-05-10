package save3;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import model.Game;

import java.io.File;
import java.io.IOException;

public class GameSaver {

        public static void saveGame(Game game, String filePath) throws Exception {
            ObjectMapper mapper = new ObjectMapper();
            mapper.findAndRegisterModules();
            mapper.enable(SerializationFeature.INDENT_OUTPUT);
            mapper.writeValue(new File(filePath), game);
        }

        public static Game loadGame(String filePath) throws Exception {
            ObjectMapper mapper = new ObjectMapper();
            mapper.findAndRegisterModules();
            return mapper.readValue(new File(filePath), Game.class);
        }
}
