package io.github.some_example_name.client;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import io.github.some_example_name.client.controller.mainMenu.StartGameMenuController;
import io.github.some_example_name.common.model.Farm;
import io.github.some_example_name.common.model.Game;
import io.github.some_example_name.common.model.User;
import io.github.some_example_name.common.model.enums.Gender;
import io.github.some_example_name.common.model.enums.SecurityQuestion;
import io.github.some_example_name.common.model.enums.Weather;
import io.github.some_example_name.client.service.ClientService;
import io.github.some_example_name.common.model.*;
import io.github.some_example_name.common.model.structure.Structure;
import io.github.some_example_name.common.utils.App;
import io.github.some_example_name.common.variables.Session;
import io.github.some_example_name.server.service.GameService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.lang.reflect.Type;
import java.net.Socket;
import java.util.List;
import java.util.Map;

public class GameClient {
    private static final String SERVER_ADDRESS = "localhost";
    private static final int SERVER_PORT = 5000;
    private static final Gson GSON = new Gson();
    private static GameClient instance;
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private final ClientService service = new ClientService();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public static GameClient getInstance() {
        if (instance == null) {
            instance = new GameClient();
        }
        return instance;
    }

    private GameClient(){
        objectMapper.activateDefaultTyping(LaissezFaireSubTypeValidator.instance,
            ObjectMapper.DefaultTyping.NON_FINAL, JsonTypeInfo.As.PROPERTY);
        objectMapper.addMixIn(Sprite.class, SpriteMixIn.class);
        objectMapper.addMixIn(TextureRegion.class, SpriteMixIn.class);
    }

    public void connectToServer() {
        try {
            socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            Map<String, Object> msg = Map.of(
                "action", "connected",
//                "id", Session.getCurrentUser().getUsername(),
                "body", Map.of()
            );

            out.println(GSON.toJson(msg));
            startListening();
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println(e.getMessage());
        }
    }

    public void readyForGame() {
        try {
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            Map<String, Object> msg = Map.of(
                "action", "ready_for_game",
                "id", Session.getCurrentUser().getUsername(),
                "body", Map.of()
            );

            out.println(GSON.toJson(msg));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void startListening() {
        new Thread(() -> {
            try {
                String serverMessage;
                while ((serverMessage = in.readLine()) != null) {
                    try {
                        JsonObject obj = JsonParser.parseString(serverMessage).getAsJsonObject();

                        if (obj.get("action").getAsString().equals("init_game")) {
                            JsonObject bodyArray = obj.getAsJsonObject("body");
                            App.getInstance().getCurrentGame().getVillage().addStoresAndNpcs(bodyArray.getAsJsonArray("stores"));
                            StartGameMenuController.getInstance().startGame(
                                bodyArray.getAsJsonArray("players"),
                                bodyArray.getAsJsonArray("farms"),
                                bodyArray.getAsJsonArray("characters")
                            );
                            for (int i = 0; i < App.getInstance().getCurrentGame().getVillage().getFarms().size(); i++) {
                                Farm farm = App.getInstance().getCurrentGame().getVillage().getFarms().get(i);
                                farm.setDoor(
                                    bodyArray.getAsJsonArray("doors").get(2 * i).getAsInt(),
                                    bodyArray.getAsJsonArray("doors").get(2 * i + 1).getAsInt()
                                );
                            }
                        } else if (obj.get("action").getAsString().equals("response_choose_farm")) {
                            StartGameMenuController.getInstance().responseToChooseFarm(
                                obj.getAsJsonObject("body").get("response").getAsString()
                            );
                        } else if (obj.get("action").getAsString().equals("_skip_time")) {
                            Gdx.app.postRunnable(() -> GameService.getInstance().skipTimeByServer(
                                obj.getAsJsonObject("body").get("minutes").getAsInt()
                            ));
                        } else if (obj.get("action").getAsString().equals("ready_for_sleep")) {
                            Gdx.app.postRunnable(() -> App.getInstance().getCurrentGame().startDayEvents(
                                obj.getAsJsonObject("body").get("tomorrowWeather").getAsInt()
                            ));
                        } else if (obj.get("action").getAsString().equals("_thor")) {
                            Gdx.app.postRunnable(
                                () -> App.getInstance().getCurrentGame().getVillage().getWeather().thunderBolt(
                                    obj.getAsJsonObject("body").get("x").getAsInt(),
                                    obj.getAsJsonObject("body").get("y").getAsInt()
                                )
                            );
                        } else if (obj.get("action").getAsString().equals("_set_weather")) {
                            App.getInstance().getCurrentGame().getVillage().setTomorrowWeather(
                                Weather.valueOf(obj.getAsJsonObject("body").get("weather").getAsString().toUpperCase())
                            );
                        } else if (obj.get("action").getAsString().equals("update_player_position")) {
                            String username = obj.get("id").getAsString();
                            int position_x = obj.getAsJsonObject("body").get("position_x").getAsInt();
                            int position_Y = obj.getAsJsonObject("body").get("position_y").getAsInt();
                            service.handleUpdatePosition(username, position_x, position_Y);
                        }else if (obj.get("action").getAsString().equals("update tile")){
                            JsonObject tileObject = obj.getAsJsonObject("body").get("tile").getAsJsonObject();
                            Tile tile = GSON.fromJson(tileObject,Tile.class);
                            service.updateTileState(tile);
                        }
                        else if (obj.get("action").getAsString().equals(StructureUpdateState.ADD.getName())){
                            JsonNode object = objectMapper.readTree(serverMessage);
                            JsonNode structureJson = object.get("body").get("structure");
                            Structure structure = null;
                            try {
                                structure = objectMapper.treeToValue(structureJson,Structure.class);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            Boolean inFarm = object.get("body").get("inFarm").asBoolean();
                            String username = object.get("id").asText();
                            service.handleAddStructure(structure, username, inFarm);
                        } else if (obj.get("action").getAsString().equals(StructureUpdateState.DELETE.getName())){
                            JsonArray jsonTiles = obj.getAsJsonObject("body").get("tiles").getAsJsonArray();
                            Type listType = new TypeToken<List<Tile>>(){}.getType();
                            List<Tile> tiles = GSON.fromJson(jsonTiles,listType);
                            service.handleDeleteStructure(tiles);
                        }
                    } catch (JsonParseException e) {
                        System.out.println("Received non-JSON: " + serverMessage);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    public void sendGameStateToServer(String gameState) {
        out.println(gameState);
    }

    public void enterRoom(int id) {
        try {
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            Map<String, Object> msg = Map.of(
                "action", "enter_room",
                "id", Session.getCurrentUser().getUsername(),
                "body", Map.of("id", id)
            );

            out.println(GSON.toJson(msg));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void chooseFarm(int farmId, String character) {
        try {
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            Map<String, Object> msg = Map.of(
                "action", "choose_farm",
                "id", Session.getCurrentUser().getUsername(),
                "body", Map.of("farmId", farmId, "character", character)
            );

            out.println(GSON.toJson(msg));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void updateStructureState(Structure structure, StructureUpdateState state,Boolean inFarm){
        try {
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            if (state.equals(StructureUpdateState.DELETE)){
                Map<String, Object> msg = Map.of(
                    "action", state.getName(),
                    "id", Session.getCurrentUser().getUsername(),
                    "body", Map.of("tiles",structure.getTiles(),
                        "inFarm",inFarm)
                );
                out.println(GSON.toJson(msg));
            }else {
                JsonNode structureJson = objectMapper.valueToTree(structure);

                Map<String, Object> msg = Map.of(
                    "action", state.getName(),
                    "id", Session.getCurrentUser().getUsername(),
                    "body", Map.of("structure",structureJson,
                        "inFarm",inFarm)
                );
                out.println(objectMapper.writeValueAsString(msg));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void updateTileState(Tile tile){
        try {
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            Map<String, Object> msg = Map.of(
                "action", "update tile",
                "id", Session.getCurrentUser().getUsername(),
                "body", Map.of("tile",tile)
            );

            out.println(GSON.toJson(msg));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void skipTime(int minutes) {
        try {
            out = new PrintWriter(socket.getOutputStream(), true);
            in  = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            Map<String, Object> msg = Map.of(
                "action", "_skip_time",
                "id", Session.getCurrentUser().getUsername(),
                "body", Map.of("minutes", minutes)
            );

            out.println(GSON.toJson(msg));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void readyForSleep() {
        try {
            out = new PrintWriter(socket.getOutputStream(), true);
            in  = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            Weather tomorrowWeather;
            Random random = new Random();
            do {
                tomorrowWeather = Weather.values()[random.nextInt(0, 4)];
            } while (!tomorrowWeather.getSeasons().contains(App.getInstance().getCurrentGame().getTimeAndDate().getSeason()));

            Map<String, Object> msg = Map.of(
                "action", "ready_for_sleep",
                "id", Session.getCurrentUser().getUsername(),
                "body", Map.of("tomorrowWeather", tomorrowWeather.ordinal())
            );

            out.println(GSON.toJson(msg));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void thor(String x, String y) {
        try {
            out = new PrintWriter(socket.getOutputStream(), true);
            in  = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            Map<String, Object> msg = Map.of(
                "action", "_thor",
                "id", Session.getCurrentUser().getUsername(),
                "body", Map.of("x", x, "y", y)
            );

            out.println(GSON.toJson(msg));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setWeather(String type) {
        try {
            out = new PrintWriter(socket.getOutputStream(), true);
            in  = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            Map<String, Object> msg = Map.of(
                "action", "_set_weather",
                "id", Session.getCurrentUser().getUsername(),
                "body", Map.of("weather", type)
            );

            out.println(GSON.toJson(msg));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

