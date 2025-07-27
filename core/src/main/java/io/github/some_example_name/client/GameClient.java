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
import io.github.some_example_name.common.model.records.Response;
import io.github.some_example_name.common.model.structure.Structure;
import io.github.some_example_name.common.utils.App;
import io.github.some_example_name.common.variables.Session;
import io.github.some_example_name.server.service.GameService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.lang.reflect.*;
import java.net.Socket;
import java.util.*;

public class GameClient {
    private static final String SERVER_ADDRESS = "localhost";
    private static final int SERVER_PORT = 5000;
    private static final Gson GSON = new GsonBuilder().serializeNulls().create();
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

                        JsonObject body = obj.getAsJsonObject("body");
                        if (obj.get("action").getAsString().equals("init_game")) {
                            JsonObject bodyArray = body;
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
                                body.get("response").getAsString()
                            );
                        } else if (obj.get("action").getAsString().equals("_skip_time")) {
                            Gdx.app.postRunnable(() -> GameService.getInstance().skipTimeByServer(
                                body.get("minutes").getAsInt()
                            ));
                        } else if (obj.get("action").getAsString().equals("ready_for_sleep")) {
                            Gdx.app.postRunnable(() -> App.getInstance().getCurrentGame().startDayEvents(
                                body.get("tomorrowWeather").getAsInt()
                            ));
                        } else if (obj.get("action").getAsString().equals("_thor")) {
                            Gdx.app.postRunnable(
                                () -> App.getInstance().getCurrentGame().getVillage().getWeather().thunderBolt(
                                    body.get("x").getAsInt(),
                                    body.get("y").getAsInt()
                                )
                            );
                        } else if (obj.get("action").getAsString().equals("_set_weather")) {
                            App.getInstance().getCurrentGame().getVillage().setTomorrowWeather(
                                Weather.valueOf(body.get("weather").getAsString().toUpperCase())
                            );
                        } else if (obj.get("action").getAsString().equals("=update_player_position")) {
                            String username = obj.get("id").getAsString();
                            int position_x = body.get("position_x").getAsInt();
                            int position_Y = body.get("position_y").getAsInt();
                            Direction direction = Direction.values()[body.get("direction").getAsInt()];
                            service.handleUpdatePosition(username, position_x, position_Y, direction);
                        } else if (obj.get("action").getAsString().equals("=update_tile")){
                            JsonObject tileObject = body.get("tile").getAsJsonObject();
                            Tile tile = GSON.fromJson(tileObject,Tile.class);
                            service.updateTileState(tile);
                        } else if (obj.get("action").getAsString().equals(StructureUpdateState.ADD.getName())){
                            decodeStructureAdd(body);
                        } else if (obj.get("action").getAsString().equals(StructureUpdateState.UPDATE.getName())){
                            decodeStructureUpdate(body, findObject(body));
                        } else if (obj.get("action").getAsString().equals(StructureUpdateState.DELETE.getName())){
                            JsonArray jsonTiles = body.get("tiles").getAsJsonArray();
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

    public void updatePlayerPosition(Player player) {
        try {
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            Map<String, Object> msg = Map.of(
                "action", "=update_player_position",
                "id", player.getUser().getUsername(),
                "body", Map.of("position_x", player.getTiles().get(0).getX(),
                    "position_y", player.getTiles().get(0).getY())
            );
            out.println(GSON.toJson(msg));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void updatePlayerCarryingObject(Player player){
        try {
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            Map<String, Object> msg = Map.of(
                "action", "=update_player_carrying",
                "id", player.getUser().getUsername(),
                "body", Map.of("carrying",encodeStructure(player.getCurrentCarrying(),null))
            );
            out.println(GSON.toJson(msg));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void updateStructureState(Structure structure, StructureUpdateState state, Boolean inFarm, Tile previousTile) {
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
            } else {
                Map<String, Object> msg = Map.of(
                    "action", state.getName(),
                    "id", Session.getCurrentUser().getUsername(),
                    "body", encodeStructure(structure, previousTile)
                );
                out.println(GSON.toJson(msg));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Object decodeStructureAdd(JsonObject body) {
        Object obj;
        try {
            Class<?> clazz = Class.forName(body.get("!class").getAsString());
            Constructor<?> constructor = clazz.getConstructor();
            obj = constructor.newInstance();
            if (Structure.class.isAssignableFrom(clazz)) {
                putBackTiles((Structure) obj, body);
            }
            for (Map.Entry<String, JsonElement> entry : body.entrySet()) {
                if (entry.getKey().charAt(0) == '!') continue;
                Field field = findField(clazz, entry.getKey());
                if (field == null) continue;
                field.setAccessible(true);
                Class<?> fieldType = field.getType();
                if (entry.getValue().isJsonNull()) {
                    field.set(obj, null);
                } else if (fieldType.equals(int.class) || fieldType.equals(Integer.class)) {
                    field.set(obj, entry.getValue().getAsInt());
                } else if (fieldType.equals(boolean.class) || fieldType.equals(Boolean.class)) {
                    field.set(obj, entry.getValue().getAsBoolean());
                } else if (fieldType.equals(String.class)) {
                    field.set(obj, entry.getValue().getAsString());
                } else if (fieldType.equals(float.class) || fieldType.equals(Float.class)) {
                    field.set(obj, entry.getValue().getAsFloat());
                } else if (fieldType.equals(double.class) || fieldType.equals(Double.class)) {
                    field.set(obj, entry.getValue().getAsDouble());
                } else if (fieldType.isEnum()) {
                    field.set(obj, fieldType.getEnumConstants()[entry.getValue().getAsInt()]);
                } else {
                    field.set(obj, decodeStructureAdd(entry.getValue().getAsJsonObject()));
                }
            }
        } catch (ClassNotFoundException | NoSuchMethodException | InvocationTargetException |
                 InstantiationException | IllegalAccessException e ) {
            e.printStackTrace();
            return null;
        }
        return obj;
    }

    private Object decodeStructureUpdate(JsonObject body, Object obj) {
        try {
            Class<?> clazz = Class.forName(body.get("!class").getAsString());
            if (Structure.class.isAssignableFrom(clazz)) {
                updateTiles((Structure) obj, body);
            }
            for (Map.Entry<String, JsonElement> entry : body.entrySet()) {
                if (entry.getKey().charAt(0) == '!') continue;
                Field field = findField(clazz, entry.getKey());
                if (field == null) continue;
                field.setAccessible(true);
                Class<?> fieldType = field.getType();
                if (entry.getValue().isJsonNull()) {
                    field.set(obj, null);
                } else if (fieldType.equals(int.class) || fieldType.equals(Integer.class)) {
                    field.set(obj, entry.getValue().getAsInt());
                } else if (fieldType.equals(boolean.class) || fieldType.equals(Boolean.class)) {
                    field.set(obj, entry.getValue().getAsBoolean());
                } else if (fieldType.equals(String.class)) {
                    field.set(obj, entry.getValue().getAsString());
                } else if (fieldType.equals(float.class) || fieldType.equals(Float.class)) {
                    field.set(obj, entry.getValue().getAsFloat());
                } else if (fieldType.equals(double.class) || fieldType.equals(Double.class)) {
                    field.set(obj, entry.getValue().getAsDouble());
                } else if (fieldType.isEnum()) {
                    field.set(obj, fieldType.getEnumConstants()[entry.getValue().getAsInt()]);
                } else {
                    if (field.get(obj) != null) {
                        field.set(obj, decodeStructureUpdate(entry.getValue().getAsJsonObject(), field.get(obj)));
                    } else {
                        field.set(obj, decodeStructureAdd(entry.getValue().getAsJsonObject()));
                    }
                }
            }
        } catch (ClassNotFoundException | IllegalAccessException e ) {
            e.printStackTrace();
            return null;
        }
        return obj;
    }

    private Object findObject(JsonObject body) {
        Farm farm = null;
        int prevX = body.get("!previousTileX").getAsInt();
        int prevY = body.get("!previousTileY").getAsInt();
        for (Farm farm1 : App.getInstance().getCurrentGame().getVillage().getFarms()) {
            if (farm1.isPairInFarm(new Pair(prevX, prevY))) {
                farm = farm1;
                break;
            }
        }
        if (farm == null) {
            for (Structure structure : App.getInstance().getCurrentGame().getVillage().getStructures()) {
                try {
                    if (structure.getClass().equals(Class.forName(body.get("!class").getAsString())) &&
                        structure.getTiles().getFirst().getX() == prevX &&
                        structure.getTiles().getFirst().getY() == prevY) {
                        return structure;
                    }
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        } else {
            for (Structure structure : farm.getStructures()) {
                try {
                    if (structure.getClass().equals(Class.forName(body.get("!class").getAsString())) &&
                        structure.getTiles().getFirst().getX() == prevX &&
                        structure.getTiles().getFirst().getY() == prevY) {
                        return structure;
                    }
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    private Field findField(Class<?> clazz, String fieldName) {
        if (clazz.equals(Object.class)) return null;
        try {
            return clazz.getDeclaredField(fieldName);
        } catch (NoSuchFieldException e) {
            return findField(clazz.getSuperclass(), fieldName);
        }
    }


    private Map<String, Object> encodeStructure(Object object, Tile previouseTile) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("!class", object.getClass().getName());
        if (previouseTile != null) {
            map.put("!previousTileX", previouseTile.getX());
            map.put("!previousTileY", previouseTile.getY());
        }
        if (object instanceof Structure) {
            putTiles(map, (Structure) object);
        }
        Class<?> clazz = object.getClass();
        while (clazz != Object.class && clazz != Structure.class) {
            for (Field field : clazz.getDeclaredFields()) {
                if (Modifier.isTransient(field.getModifiers())) continue;
                field.setAccessible(true);
                try {
                    Class<?> fieldType = field.getType();
                    Object obj = field.get(object);
                    if (obj == null) {
                        map.put(field.getName(), null);
                    } else if (fieldType.equals(int.class) || fieldType.equals(Integer.class) ||
                        fieldType.equals(boolean.class) || fieldType.equals(Boolean.class) ||
                        fieldType.equals(String.class) || fieldType.equals(float.class) ||
                        fieldType.equals(Float.class) || fieldType.equals(double.class) ||
                        fieldType.equals(Double.class)) {
                        map.put(field.getName(), obj);
                    } else if (fieldType.isEnum()) {
                        map.put(field.getName(), ((Enum<?>) obj).ordinal());
                    } else {
                        if (obj instanceof Structure structure && !structure.getTiles().isEmpty()) {
                            map.put(field.getName(), encodeStructure(structure, structure.getTiles().getFirst()));
                        } else {
                            map.put(field.getName(), encodeStructure(obj, null));
                        }
                    }
                } catch (IllegalAccessException ignored) {}
            }
            clazz = clazz.getSuperclass();
        }
        return map;
    }


    private void putBackTiles(Structure obj, JsonObject body) {
        updateTiles(obj, body);
        if (obj.getTiles().isEmpty()) return;
        Farm farm = null;
        for (Farm farm1 : App.getInstance().getCurrentGame().getVillage().getFarms()) {
            if (farm1.isPairInFarm(new Pair(obj.getTiles().getFirst().getX(), obj.getTiles().getFirst().getY()))) {
                farm = farm1;
                break;
            }
        }
        if (farm == null) {
            App.getInstance().getCurrentGame().getVillage().getStructures().add(obj);
        } else {
            farm.getStructures().add(obj);
        }
    }

    private void updateTiles(Structure obj, JsonObject body) {
        obj.getTiles().clear();
        Tile[][] tiles = App.getInstance().getCurrentGame().getTiles();
        for (int i = 0; i < body.get("!tiles").getAsJsonArray().size();) {
            Tile tile = tiles[body.get("!tiles").getAsJsonArray().get(i++).getAsInt()]
                [body.get("!tiles").getAsJsonArray().get(i++).getAsInt()];
            obj.getTiles().add(tile);
        }
        obj.setIsPickable(body.get("!isPickable").getAsBoolean());
    }

    private void putTiles(Map<String, Object> map, Structure structure) {
        ArrayList<Integer> list = new ArrayList<>();
        for (Tile tile : structure.getTiles()) {
            list.add(tile.getX());
            list.add(tile.getY());
        }
        map.put("!tiles", list);
        map.put("!isPickable", structure.getIsPickable());
    }

    public void updateTileState(Tile tile){
        try {
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            Map<String, Object> msg = Map.of(
                "action", "=update_tile",
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

