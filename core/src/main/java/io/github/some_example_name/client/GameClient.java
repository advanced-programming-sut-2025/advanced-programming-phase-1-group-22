package io.github.some_example_name.client;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import io.github.some_example_name.client.controller.WorldController;
import io.github.some_example_name.client.controller.mainMenu.StartGameMenuController;
import io.github.some_example_name.client.view.GameView;
import io.github.some_example_name.client.view.mainMenu.TerminateMenu;
import io.github.some_example_name.common.model.Farm;
import io.github.some_example_name.common.model.abilitiy.Ability;
import io.github.some_example_name.common.model.enums.Weather;
import io.github.some_example_name.client.service.ClientService;
import io.github.some_example_name.common.model.*;
import io.github.some_example_name.common.model.products.HarvestAbleProduct;
import io.github.some_example_name.common.model.records.Response;
import io.github.some_example_name.common.model.relations.Mission;
import io.github.some_example_name.common.model.relations.NPC;
import io.github.some_example_name.common.model.relations.Player;
import io.github.some_example_name.common.model.structure.Structure;
import io.github.some_example_name.common.model.structure.stores.Shop;
import io.github.some_example_name.common.model.tools.MilkPail;
import io.github.some_example_name.common.utils.App;
import io.github.some_example_name.common.utils.GameAsset;
import io.github.some_example_name.common.variables.Session;
import io.github.some_example_name.server.service.GameService;
import io.github.some_example_name.server.service.RelationService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.lang.reflect.*;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

public class GameClient {
    private static final String SERVER_ADDRESS = "localhost";
    private static final int SERVER_PORT = 5000;
    private static final Gson GSON = new GsonBuilder().serializeNulls().create();
    private static GameClient instance;
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private final Timer timer = new Timer();
    private final ClientService service = new ClientService();
    private final AtomicReference<TerminateMenu> terminateMenu = new AtomicReference<>();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public static GameClient getInstance() {
        if (instance == null) {
            instance = new GameClient();
        }
        return instance;
    }

    private GameClient() {
        objectMapper.activateDefaultTyping(LaissezFaireSubTypeValidator.instance,
            ObjectMapper.DefaultTyping.NON_FINAL, JsonTypeInfo.As.PROPERTY);
        objectMapper.addMixIn(Sprite.class, SpriteMixIn.class);
        objectMapper.addMixIn(TextureRegion.class, SpriteMixIn.class);
    }

    private void pingMassage() {
        try {
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            Map<String, Object> msg = Map.of(
                "action", "ping",
                "id", Session.getCurrentUser().getUsername()
            );

            out.println(GSON.toJson(msg));
        } catch (IOException e) {
            e.printStackTrace();
        }
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
            timer.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    pingMassage();
                }
            }, 0, 5000);
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
                            App.getInstance().getCurrentGame().getVillage().addStoresAndNpcs(body.getAsJsonArray("stores"));
                            StartGameMenuController.getInstance().startGame(
                                body.getAsJsonArray("players"),
                                body.getAsJsonArray("farms"),
                                body.getAsJsonArray("characters")
                            );
                            for (int i = 0; i < App.getInstance().getCurrentGame().getVillage().getFarms().size(); i++) {
                                Farm farm = App.getInstance().getCurrentGame().getVillage().getFarms().get(i);
                                farm.setDoor(
                                    body.getAsJsonArray("doors").get(2 * i).getAsInt(),
                                    body.getAsJsonArray("doors").get(2 * i + 1).getAsInt()
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
                        } else if (obj.get("action").getAsString().equals("=update_tile")) {
                            JsonObject tileObject = body.get("tile").getAsJsonObject();
                            Tile tile = GSON.fromJson(tileObject, Tile.class);
                            service.updateTileState(tile);
                        } else if (obj.get("action").getAsString().equals("=build_greenhouse")) {
                            Farm farm = null;
                            for (Farm farm1 : App.getInstance().getCurrentGame().getVillage().getFarms()) {
                                if (farm1.getPlayers().get(0).getUser().getUsername().equals(obj.get("id").getAsString())) {
                                    farm = farm1;
                                    break;
                                }
                            }
                            if (farm != null) {
                                farm.getGreenHouse().build(farm);
                            }
                        } else if (obj.get("action").getAsString().equals("_faint")) {
                            String username = obj.get("id").getAsString();
                            service.getPlayerByUsername(username).applyFaint();
                        } else if (obj.get("action").getAsString().equals("_ask_marriage")) {
                            Player requester = service.getPlayerByUsername(obj.get("id").getAsString());
                            Player requested = service.getPlayerByUsername(obj.getAsJsonObject("body")
                                .get("requested").getAsString());
                            RelationService.getInstance().handleAskMarriage(requester, requested);
                        } else if (obj.get("action").getAsString().equals("_handle_flower")) {
                            Player requester = service.getPlayerByUsername(obj.get("id").getAsString());
                            Player requested = service.getPlayerByUsername(obj.getAsJsonObject("body")
                                .get("requested").getAsString());
                            RelationService.getInstance().drawFlower(requester, requested);
                        } else if (obj.get("action").getAsString().equals("_handle_hug")) {
                            Player requester = service.getPlayerByUsername(obj.get("id").getAsString());
                            Player requested = service.getPlayerByUsername(obj.getAsJsonObject("body")
                                .get("requested").getAsString());
                            RelationService.getInstance().handleHug(requester, requested);
                        } else if (obj.get("action").getAsString().equals("_respond_marriage")) {
                            Player requester = service.getPlayerByUsername(obj.get("id").getAsString());
                            Player requested = service.getPlayerByUsername(body.get("requested").getAsString());
                            if (body.get("response").getAsBoolean()) {
                                RelationService.getInstance().handleYes(requester, requested);
                            } else {
                                RelationService.getInstance().handleNo(requester, requested);
                            }
                            RelationService.getInstance().handleHug(requester, requested);
                        } else if (obj.get("action").getAsString().equals("set_golds")) {
                            App.getInstance().getCurrentGame().getCurrentPlayer().getAccount().setGoldsByServer(
                                body.get("count").getAsInt()
                            );
                        } else if (obj.get("action").getAsString().equals("send_gift")) {
                            RelationService.getInstance().getGift(
                                service.getPlayerByUsername(obj.get("id").getAsString()),
                                (Salable) decodeStructureAdd(body.getAsJsonObject("gift")),
                                body.get("amount").getAsInt()
                            );
                        } else if (obj.get("action").getAsString().equals("talk")) {
                            RelationService.getInstance().privateTalk(
                                service.getPlayerByUsername(obj.get("id").getAsString()),
                                service.getPlayerByUsername(body.get("receiver").getAsString()),
                                body.get("message").getAsString()
                            );
                        } else if (obj.get("action").getAsString().equals("rate_gift")) {
                            RelationService.getInstance().rateGiftByServer(
                                body.get("giftId").getAsInt(),
                                body.get("rate").getAsInt(),
                                service.getPlayerByUsername(body.get("receiver").getAsString()),
                                service.getPlayerByUsername(obj.get("id").getAsString())
                            );
                        } else if (obj.get("action").getAsString().equals("propose_end")) {
                            terminateMenu.set(new TerminateMenu());
                            synchronized (terminateMenu) {
                                terminateMenu.get().createMenu(GameView.stage, GameAsset.SKIN, WorldController.getInstance());
                                terminateMenu.get().setState(1);
                            }
                        } else if (obj.get("action").getAsString().equals("stop_termination")) {
                            synchronized (terminateMenu) {
                                terminateMenu.get().undoTermination();
                            }
                        } else if (obj.get("action").getAsString().equals("terminate_game")) {
                            synchronized (terminateMenu) {
                                terminateMenu.get().terminate();
                            }
                        } else if (obj.get("action").getAsString().equals("=fridge_pick")) {
                            for (Farm farm : App.getInstance().getCurrentGame().getVillage().getFarms()) {
                                if (!farm.getPlayers().isEmpty() && farm.getPlayers().get(0).getName().equals(obj.get("id").getAsString())) {
                                    Salable salable = farm.getFridge().findProduct(body.get("name").getAsString());
                                    farm.getFridge().getProducts().remove(salable);
                                }
                            }
                        } else if (obj.get("action").getAsString().equals("=fridge_put")) {
                            for (Farm farm : App.getInstance().getCurrentGame().getVillage().getFarms()) {
                                if (!farm.getPlayers().isEmpty() && farm.getPlayers().get(0).getName().equals(obj.get("id").getAsString())) {
                                    Salable salable = (Salable) decodeStructureAdd(body.get("name").getAsJsonObject());
                                    farm.getFridge().addProduct(salable, body.get("count").getAsInt());
                                }
                            }
                        } else if (obj.get("action").getAsString().equals("notify")) {
                            Actor source = null;
                            if (body.get("isFromPlayer").getAsBoolean()) {
                                source = service.getPlayerByUsername(body.get("source").getAsString());
                            } else {
                                for (NPC npc : App.getInstance().getCurrentGame().getNpcs()) {
                                    if (npc.getName().equals(body.get("source").getAsString())) {
                                        source = npc;
                                        break;
                                    }
                                }
                            }
                            service.getPlayerByUsername(body.get("receiver").getAsString()).getNotified(
                                new Response(
                                    body.get("response_message").getAsString(),
                                    body.get("response_bool").getAsBoolean()
                                ),
                                NotificationType.values()[body.get("type").getAsInt()],
                                source
                            );
                        } else if (obj.get("action").getAsString().equals(StructureUpdateState.ADD.getName())) {
                            decodeStructureAdd(body);
                        } else if (obj.get("action").getAsString().equals(StructureUpdateState.UPDATE.getName())) {
                            decodeStructureUpdate(body, findObject(body));
                        } else if (obj.get("action").getAsString().equals(StructureUpdateState.DELETE.getName())) {
                            JsonArray jsonTiles = body.get("tiles").getAsJsonArray();
                            Type listType = new TypeToken<List<Tile>>() {
                            }.getType();
                            List<Tile> tiles = GSON.fromJson(jsonTiles, listType);
                            service.handleDeleteStructure(tiles);
                        } else if (obj.get("action").getAsString().equals("=update_player_carrying")) {
                            String username = obj.get("id").getAsString();
                            Object carrying = decodeObject(body);
                            service.handleCurrentCarrying((Salable) carrying, username);
                        } else if (obj.get("action").getAsString().equals("=update_farm_crow_attack")) {
                            String farmType = obj.get("farmType").getAsString();
                            boolean isCrowAttack = obj.get("isCrowAttack").getAsBoolean();
                            if (!isCrowAttack) {
                                Object harvest = decodeStructure(body);
                                service.handleCrowAttack(farmType, false, (HarvestAbleProduct) harvest);
                            } else {
                                service.handleCrowAttack(farmType, true, null);
                            }
                        } else if (obj.get("action").getAsString().equals("=increase_daily_sold")) {
                            int amount = obj.get("amount").getAsInt();
                            Object shop = decodeObject(body);
                            service.handleStore((Shop) shop, amount);
                        } else if (obj.get("action").getAsString().equals("=update_npc_mission")) {
                            String username = obj.get("id").getAsString();
                            String npcName = obj.get("npc_type").getAsString();
                            int mission_id = obj.get("mission_id").getAsInt();
                            service.handleNpcMission(mission_id, npcName, username);
                        } else if (obj.get("action").getAsString().equals("=update_player_reaction")) {
                            String username = obj.get("id").getAsString();
                            JsonElement emojiIndexJson = obj.get("emoji_index");
                            Integer emojiIndex = (emojiIndexJson == null || emojiIndexJson.isJsonNull()) ? null : emojiIndexJson.getAsInt();
                            JsonElement textJson = obj.get("text");
                            String text = (textJson == null || textJson.isJsonNull()) ? null : textJson.getAsString();
                            service.handlePlayerReaction(username, emojiIndex, text);
                        } else if (obj.get("action").getAsString().equals("=update_player_gold")) {
                            String username = obj.get("id").getAsString();
                            int gold = obj.get("gold").getAsInt();
                            service.handlePlayerGold(username, gold);
                        } else if (obj.get("action").getAsString().equals("=update_player_number_of_complete_missions")) {
                            String username = obj.get("id").getAsString();
                            int completeMissions = obj.get("complete_missions").getAsInt();
                            service.handlePlayerNumberOfCompleteMissions(username, completeMissions);
                        } else if (obj.get("action").getAsString().equals("=update_player_skill")) {
                            String username = obj.get("id").getAsString();
                            String ability = obj.get("ability").getAsString();
                            int amount = obj.get("amount").getAsInt();
                            service.handlePlayerSkill(username, ability, amount);
                        } else if (obj.get("action").getAsString().equals("=update_multi_mission_player")) {
                            String username = obj.get("id").getAsString();
                            int missionId = obj.get("mission_id").getAsInt();
                            service.handleMissionPlayer(username, missionId);
                        } else if (obj.get("action").getAsString().equals("=update_multi_mission_add")) {
                            String username = obj.get("id").getAsString();
                            int missionId = obj.get("mission_id").getAsInt();
                            int amount = obj.get("amount").getAsInt();
                            service.handleMissionAdd(username, missionId, amount);
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

    public void updateFarmCrowAttack(Farm farm, boolean AttackToday, HarvestAbleProduct harvestAbleProduct) {
        try {
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            Map<String, Object> msg;
            if (!AttackToday) {
                msg = Map.of(
                    "action", "=update_farm_crow_attack",
                    "id", Session.getCurrentUser().getUsername(),
                    "farmType", farm.getFarmType().getName(),
                    "isCrowAttack", false,
                    "body", encodeStructure(harvestAbleProduct, null)
                );
            } else {
                msg = Map.of(
                    "action", "=update_farm_crow_attack",
                    "id", Session.getCurrentUser().getUsername(),
                    "farmType", farm.getFarmType().getName(),
                    "isCrowAttack", true
                );
            }

            out.println(GSON.toJson(msg));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void updateStoreDailySold(Shop shop, int amount) {
        try {
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            Map<String, Object> msg = Map.of(
                "action", "=increase_daily_sold",
                "id", Session.getCurrentUser().getUsername(),
                "amount", amount,
                "body", encodeObject(shop)
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
                    "position_y", player.getTiles().get(0).getY(), "direction", player.getDirection().ordinal())
            );
            out.println(GSON.toJson(msg));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void updatePlayerCarryingObject(Player player) {
        try {
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            Map<String, Object> msg = Map.of(
                "action", "=update_player_carrying",
                "id", player.getUser().getUsername(),
                "body", encodeObject(player.getCurrentCarrying())
            );
            out.println(GSON.toJson(msg));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void updateNpcMissionState(Mission mission) {
        try {
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            Map<String, Object> msg = Map.of(
                "action", "=update_npc_mission",
                "id", Session.getCurrentUser().getUsername(),
                "mission_id", mission.getId(),
                "npc_type", mission.getRequester().getName()
            );
            out.println(GSON.toJson(msg));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void updatePlayerReaction(Player player) {
        try {
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            Map<String, Object> msg = new HashMap<>();
            msg.put("action", "=update_player_reaction");
            msg.put("id", Session.getCurrentUser().getUsername());
            msg.put("emoji_index", player.getEmojiReactionIndex());
            msg.put("text", player.getTextReaction());

            out.println(GSON.toJson(msg));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void updatePlayerGold(Player player) {
        try {
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            Map<String, Object> msg = new HashMap<>();
            msg.put("action", "=update_player_gold");
            msg.put("id", Session.getCurrentUser().getUsername());
            msg.put("gold", player.getAccount().getGolds());

            out.println(GSON.toJson(msg));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void updatePlayerNumberOfCompleteMissions(Player player) {
        try {
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            Map<String, Object> msg = new HashMap<>();
            msg.put("action", "=update_player_number_of_complete_missions");
            msg.put("id", Session.getCurrentUser().getUsername());
            msg.put("complete_missions", player.getNumberOfCompleteMission());

            out.println(GSON.toJson(msg));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void updatePlayerSkill(Player player, Ability ability) {
        try {
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            Map<String, Object> msg = new HashMap<>();
            msg.put("action", "=update_player_skill");
            msg.put("id", Session.getCurrentUser().getUsername());
            msg.put("ability", ability.getName());
            msg.put("amount", player.getAbilities().get(ability));

            out.println(GSON.toJson(msg));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void updateMultiMissionPlayer(MultiMission mission) {
        try {
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            Map<String, Object> msg = new HashMap<>();
            msg.put("action", "=update_multi_mission_player");
            msg.put("id", Session.getCurrentUser().getUsername());
            msg.put("mission_id", mission.getId());

            out.println(GSON.toJson(msg));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void updateMultiMissionAdd(MultiMission mission, int amount) {
        try {
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            Map<String, Object> msg = new HashMap<>();
            msg.put("action", "=update_multi_mission_add");
            msg.put("id", Session.getCurrentUser().getUsername());
            msg.put("mission_id", mission.getId());
            msg.put("amount", amount);

            out.println(GSON.toJson(msg));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Object decodeObject(JsonObject body) {
        Object obj;
        try {
            Class<?> clazz = Class.forName(body.get("!class").getAsString());
            if (clazz.isEnum()) {
                JsonElement valueElem = body.get("value");
                if (valueElem != null && valueElem.isJsonPrimitive()) {
                    return clazz.getEnumConstants()[valueElem.getAsInt()];
                } else {
                    return clazz.getEnumConstants()[0];
                }
            }
            if (clazz == MilkPail.class) {
                return MilkPail.getInstance();
            }
            Constructor<?> constructor = clazz.getConstructor();
            obj = constructor.newInstance();

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
                } else if (entry.getValue().isJsonObject()) {
                    field.set(obj, decodeObject(entry.getValue().getAsJsonObject()));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return obj;
    }


    private Object decodeStructure(JsonObject body) {
        Object obj;
        try {
            Class<?> clazz = Class.forName(body.get("!class").getAsString());
            Constructor<?> constructor = clazz.getConstructor();
            obj = constructor.newInstance();
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
                    field.set(obj, decodeStructureAdd(entry.getValue().getAsJsonObject()));
                }
            }
        } catch (ClassNotFoundException | NoSuchMethodException | InvocationTargetException |
                 InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
            return null;
        }
        return obj;
    }


    private Map<String, Object> encodeObject(Object object) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("!class", object.getClass().getName());

        Class<?> clazz = object.getClass();
        if (clazz.isEnum()) {
            map.put("value", ((Enum<?>) object).ordinal());
            return map;
        }
        while (clazz != Object.class) {
            for (Field field : clazz.getDeclaredFields()) {
                if (Modifier.isTransient(field.getModifiers()) || Modifier.isStatic(field.getModifiers()) ||
                    Modifier.isFinal(field.getModifiers())) continue;
                field.setAccessible(true);
                try {
                    Object value = field.get(object);
                    if (value == null) {
                        map.put(field.getName(), null);
                    } else {
                        Class<?> fieldType = field.getType();
                        if (fieldType.getName().startsWith("com.badlogic.gdx.") ||
                            Collection.class.isAssignableFrom(fieldType) ||
                            Map.class.isAssignableFrom(fieldType)) {
                            continue;
                        }
                        if (fieldType.equals(int.class) || fieldType.equals(Integer.class) ||
                            fieldType.equals(boolean.class) || fieldType.equals(Boolean.class) ||
                            fieldType.equals(String.class) || fieldType.equals(float.class) ||
                            fieldType.equals(Float.class) || fieldType.equals(double.class) ||
                            fieldType.equals(Double.class)) {
                            map.put(field.getName(), value);
                        } else if (fieldType.isEnum()) {
                            map.put(field.getName(), ((Enum<?>) value).ordinal());
                        } else {
                            map.put(field.getName(), encodeObject(value));
                        }
                    }
                } catch (IllegalAccessException ignored) {
                }
            }
            clazz = clazz.getSuperclass();
        }

        return map;
    }

    public void updateStructureState(Structure structure, StructureUpdateState state, Boolean inFarm, Tile previousTile) {
        try {
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            if (state.equals(StructureUpdateState.DELETE)) {
                Map<String, Object> msg = Map.of(
                    "action", state.getName(),
                    "id", Session.getCurrentUser().getUsername(),
                    "body", Map.of("tiles", structure.getTiles(),
                        "inFarm", inFarm)
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
                 InstantiationException | IllegalAccessException e) {
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
        } catch (ClassNotFoundException | IllegalAccessException e) {
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
            App.getInstance().getCurrentGame().getVillage().applyPendingChanges();
            List<Structure> structures = App.getInstance().getCurrentGame().getVillage().getStructuresSnapshot();
            for (Structure structure : structures) {
                try {
                    if (structure.getClass().equals(Class.forName(body.get("!class").getAsString())) &&
                        structure.getTiles().get(0).getX() == prevX &&
                        structure.getTiles().get(0).getY() == prevY) {
                        return structure;
                    }
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        } else {
            farm.applyPendingChanges();
            List<Structure> structures = farm.getStructuresSnapshot();
            for (Structure structure : structures) {
                try {
                    if (structure.getClass().equals(Class.forName(body.get("!class").getAsString())) &&
                        structure.getTiles().get(0).getX() == prevX &&
                        structure.getTiles().get(0).getY() == prevY) {
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
                if (Modifier.isTransient(field.getModifiers()) || Modifier.isStatic(field.getModifiers()) ||
                    Modifier.isFinal(field.getModifiers())) continue;
                field.setAccessible(true);
                try {
                    Class<?> fieldType = field.getType();
                    if (fieldType.getName().startsWith("com.badlogic.gdx.") ||
                        Collection.class.isAssignableFrom(fieldType) ||
                        Map.class.isAssignableFrom(fieldType)) {
                        continue;
                    }
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
                            map.put(field.getName(), encodeStructure(structure, structure.getTiles().get(0)));
                        } else {
                            map.put(field.getName(), encodeStructure(obj, null));
                        }
                    }
                } catch (IllegalAccessException ignored) {
                }
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
            if (farm1.isPairInFarm(new Pair(obj.getTiles().get(0).getX(), obj.getTiles().get(0).getY()))) {
                farm = farm1;
                break;
            }
        }
        if (farm == null) {
            App.getInstance().getCurrentGame().getVillage().addStructure(obj);
        } else {
            farm.addStructure(obj);
        }
    }

    private void updateTiles(Structure obj, JsonObject body) {
        if (obj == null) return;
        if (!obj.getTiles().isEmpty()) obj.getTiles().clear();
        Tile[][] tiles = App.getInstance().getCurrentGame().getTiles();
        for (int i = 0; i < body.get("!tiles").getAsJsonArray().size(); ) {
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

    public void updateTileState(Tile tile) {
        try {
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            Map<String, Object> msg = Map.of(
                "action", "=update_tile",
                "id", Session.getCurrentUser().getUsername(),
                "body", Map.of("tile", tile)
            );

            out.println(GSON.toJson(msg));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void skipTime(int minutes) {
        try {
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

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
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
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
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

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
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

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

    public void buildGreenhouse() {
        try {
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            Map<String, Object> msg = Map.of(
                "action", "=build_greenhouse",
                "id", Session.getCurrentUser().getUsername(),
                "body", Map.of()
            );

            out.println(GSON.toJson(msg));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void faint() {
        try {
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            Map<String, Object> msg = Map.of(
                "action", "_faint",
                "id", Session.getCurrentUser().getUsername(),
                "body", Map.of()
            );

            out.println(GSON.toJson(msg));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void askMarriage(String username) {
        handleInteraction(username, "_ask_marriage");
    }

    private void handleInteraction(String username, String action) {
        try {
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            Map<String, Object> msg = Map.of(
                "action", action,
                "id", Session.getCurrentUser().getUsername(),
                "body", Map.of("requested", username)
            );

            out.println(GSON.toJson(msg));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void handleFlower(String username) {
        handleInteraction(username, "_handle_flower");
    }


    public void handleHug(String username) {
        handleInteraction(username, "_handle_hug");
    }

    public void respondMarriage(boolean b, String username) {
        try {
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            Map<String, Object> msg = Map.of(
                "action", "_respond_marriage",
                "id", Session.getCurrentUser().getUsername(),
                "body", Map.of("requested", username, "response", b)
            );
            out.println(GSON.toJson(msg));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setGolds(int count, String couple) {
        try {
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            Map<String, Object> msg = Map.of(
                "action", "set_golds",
                "id", Session.getCurrentUser().getUsername(),
                "body", Map.of("count", count, "receiver", couple)
            );
            out.println(GSON.toJson(msg));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendGift(Player player, Salable gift, int amount) {
        try {
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            Map<String, Object> msg = Map.of(
                "action", "send_gift",
                "id", Session.getCurrentUser().getUsername(),
                "body", Map.of("amount", amount, "receiver", player.getUser().getUsername(),
                    "gift", encodeStructure(gift, null))
            );
            out.println(GSON.toJson(msg));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void notifyPlayer(String username, Response response, NotificationType type, Actor source) {
        try {
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            Map<String, Object> msg = Map.of(
                "action", "notify",
                "id", Session.getCurrentUser().getUsername(),
                "body", Map.of("response_message", response.message(), "receiver", username,
                    "response_bool", response.shouldBeBack(), "type", type.ordinal(),
                    "isFromPlayer", source instanceof Player, "source", source.getName())
            );
            out.println(GSON.toJson(msg));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void talk(Player anotherPlayer, String message) {
        try {
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            Map<String, Object> msg = Map.of(
                "action", "talk",
                "id", Session.getCurrentUser().getUsername(),
                "body", Map.of("message", message, "receiver", anotherPlayer.getName())
            );
            out.println(GSON.toJson(msg));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void rateGift(Integer giftId, int rate, String giver) {
        try {
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            Map<String, Object> msg = Map.of(
                "action", "rate_gift",
                "id", Session.getCurrentUser().getUsername(),
                "body", Map.of("giftId", giftId, "receiver", giver, "rate", rate)
            );
            out.println(GSON.toJson(msg));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void emptyAction(String string) {
        try {
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            Map<String, Object> msg = Map.of(
                "action", string,
                "id", Session.getCurrentUser().getUsername(),
                "body", Map.of()
            );
            out.println(GSON.toJson(msg));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void refrigeratorPut(Salable name, Integer count) {
        try {
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            Map<String, Object> msg = Map.of(
                "action", "=fridge_put",
                "id", Session.getCurrentUser().getUsername(),
                "body", Map.of("name", encodeStructure(name, null), "count", count)
            );
            out.println(GSON.toJson(msg));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void refrigeratorPick(String name) {
        try {
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            Map<String, Object> msg = Map.of(
                "action", "=fridge_pick",
                "id", Session.getCurrentUser().getUsername(),
                "body", Map.of("name", name)
            );
            out.println(GSON.toJson(msg));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

