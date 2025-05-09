package save;

import com.fasterxml.jackson.core.StreamWriteConstraints;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import model.Game;
import model.Salable;
import save.serial.SalableDeserializer;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class GameSaver {
	private static final ObjectMapper mapper = new ObjectMapper();

	public GameSaver() {
		SimpleModule module = new SimpleModule();
		module.addDeserializer(Salable.class, new SalableDeserializer());
		mapper.registerModule(module);
	}

//	public static void save(Game game, String filePath) throws IOException {
//
//		try {
//			mapper.writeValue(new File(filePath), game);
//			System.out.println("Game saved successfully to " + filePath);
//		} catch (IOException e) {
//			System.err.println("Error saving game: " + e.getMessage());
//		}
//	}

	public static void save(Game game, String directoryPath) throws IOException {
//		SimpleModule module = new SimpleModule();
//		module.addDeserializer(Salable.class, new SalableDeserializer());
//		mapper.registerModule(module);
//		try {
//			// اطمینان از وجود مسیر
//			new File(directoryPath).mkdirs();
//
//			// استفاده از ID بازی در نام فایل
//			//String fileName = "game_" + game.getId() + ".json";
//			File file = new File(directoryPath, fileName);
//
//			mapper.writeValue(file, game);
//			System.out.println("Game saved successfully to " + file.getAbsolutePath());
//		} catch (IOException e) {
//			System.err.println("Error saving game: " + e.getMessage());
//		}
	}

//
//	public static Game load(String filePath){
//		try {
//			return mapper.readValue(new File(filePath), Game.class);
//		} catch (IOException e) {
//			//System.err.println("Error loading game: " + e.getMessage());
//			e.printStackTrace();
//			return null;
//		}
//	}

//	public static Game load(UUID gameId, String directoryPath){
//		try {
//			String fileName = "game_" + gameId.toString() + ".json";
//			File file = new File(directoryPath, fileName);
//
//			if (!file.exists()) {
//				throw new IOException("Game file not found: " + file.getAbsolutePath());
//			}
//
//			Game loadedGame = mapper.readValue(file, Game.class);
//			System.out.println("Game loaded successfully from " + file.getAbsolutePath());
//			return loadedGame;
//		} catch (IOException e) {
//			System.err.println("Error loading game: " + e.getMessage());
//			return null;
//		}
//	}

	public static Game load(String path) {
		SimpleModule module = new SimpleModule();
		module.addDeserializer(Salable.class, new SalableDeserializer());
		mapper.registerModule(module);
		File folder = new File(path);
		File[] files = folder.listFiles((dir, name) -> name.startsWith("game_") && name.endsWith(".json"));

		if (files == null || files.length == 0) {
			System.err.println("No saved game found.");
			return null;
		}

		File gameFile = files[0];

		try {
			return mapper.readValue(gameFile, Game.class);
		} catch (IOException e) {
			System.err.println("Error loading game from " + gameFile.getName() + ": " + e.getMessage());
			return null;
		}
	}


}
