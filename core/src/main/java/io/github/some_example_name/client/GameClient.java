package io.github.some_example_name.client;

import com.badlogic.gdx.Gdx;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import io.github.some_example_name.client.controller.AudioStreamer;
import io.github.some_example_name.client.controller.WorldController;
import io.github.some_example_name.client.controller.mainMenu.StartGameMenuController;
import io.github.some_example_name.client.view.GameView;
import io.github.some_example_name.client.view.mainMenu.FireMenu;
import io.github.some_example_name.client.view.mainMenu.TerminateMenu;
import io.github.some_example_name.common.JsonMessageHandler;
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
import io.github.some_example_name.common.model.relations.Trade;
import io.github.some_example_name.common.model.structure.Structure;
import io.github.some_example_name.common.model.structure.stores.Shop;
import io.github.some_example_name.common.model.tools.MilkPail;
import io.github.some_example_name.common.utils.App;
import io.github.some_example_name.common.utils.GameAsset;
import io.github.some_example_name.common.variables.Session;
import io.github.some_example_name.server.service.GameService;
import io.github.some_example_name.server.service.RelationService;
import io.github.some_example_name.server.service.TradeService;

import java.io.IOException;
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
    private P2PConnection p2PConnection;
    private P2PReceiving p2PReceiving;
    private JsonMessageHandler jsonMessageHandler;
    private final Timer timer = new Timer();
    private final ClientService service = new ClientService();
    private final AtomicReference<TerminateMenu> terminateMenu = new AtomicReference<>();
    private final AtomicReference<FireMenu> fireMenu = new AtomicReference<>();

    public static GameClient getInstance() {
        if (instance == null) {
            instance = new GameClient();
        }
        return instance;
    }

    private GameClient() {
        if (p2PConnection != null && p2PConnection.isAlive()) {
            System.out.println("already p2p is alive");
        } else {
            p2PConnection = new P2PConnection(App.PORT);
            p2PConnection.start();
        }
    }

    private void pingMassage() {
        try {
            Map<String, Object> msg = Map.of(
                "action", "ping",
                "id", Session.getCurrentUser().getUsername()
            );

            jsonMessageHandler.send(GSON.toJson(msg));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void lobbyLoginPingMassage() {
        try {
            Map<String, Object> msg = Map.of(
                "action", "login_ping",
                "id", Session.getCurrentUser().getUsername()
            );

            jsonMessageHandler.send(GSON.toJson(msg));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void DCReconnect(String username) {
        try {
            Map<String, Object> msg = Map.of(
                "action", "DC_reconnect",
                "id", username
            );

            jsonMessageHandler.send(GSON.toJson(msg));
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

    public void connectToServer() {
        try {
            socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
            jsonMessageHandler = new JsonMessageHandler(socket.getInputStream(), socket.getOutputStream());

            Map<String, Object> msg = Map.of(
                "action", "connected",
                "body", Map.of()
            );

            jsonMessageHandler.send(GSON.toJson(msg));
            startListening();
        } catch (IOException e) {
            debug(e);
            System.err.println(e.getMessage());
        }
    }

    public void loggedIn() {
        try {
            Map<String, Object> msg = Map.of(
                "action", "login",
                "id", Session.getCurrentUser().getUsername()
            );

            jsonMessageHandler.send(GSON.toJson(msg));
            Timer userTimer = new Timer();
            userTimer.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    lobbyLoginPingMassage();
                }
            }, 0, 5000);
        } catch (IOException e) {
            debug(e);
            System.err.println(e.getMessage());
        }
    }

    public void readyForGame() {
        try {
            Map<String, Object> msg = Map.of(
                "action", "ready_for_game",
                "id", Session.getCurrentUser().getUsername(),
                "body", Map.of()
            );

            jsonMessageHandler.send(GSON.toJson(msg));
            timer.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    pingMassage();
                }
            }, 0, 5000);
        } catch (IOException e) {
            debug(e);
        }
    }

    public void createLobbyMessage(String lobbyName, boolean isPrivate, String password, boolean isVisible, long id) {
        try {
            Map<String, Object> msg = new HashMap<>();
            msg.put("action", "=create_lobby");
            msg.put("id", Session.getCurrentUser().getUsername());
            msg.put("name", lobbyName);
            msg.put("private", isPrivate);
            msg.put("password", password);
            msg.put("visible", isVisible);
            msg.put("lobby_id", id);

            jsonMessageHandler.send(GSON.toJson(msg));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void joinLobbyMessage(long id) {
        try {
            Map<String, Object> msg = new HashMap<>();
            msg.put("action", "=join_lobby");
            msg.put("id", Session.getCurrentUser().getUsername());
            msg.put("lobby_id", id);

            jsonMessageHandler.send(GSON.toJson(msg));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void leftLobbyMessage(long id) {
        try {
            Map<String, Object> msg = new HashMap<>();
            msg.put("action", "=left_lobby");
            msg.put("id", Session.getCurrentUser().getUsername());
            msg.put("lobby_id", id);

            jsonMessageHandler.send(GSON.toJson(msg));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void startGame(long id) {
        try {
            Map<String, Object> msg = new HashMap<>();
            msg.put("action", "=start_game");
            msg.put("id", Session.getCurrentUser().getUsername());
            msg.put("lobby_id", id);

            jsonMessageHandler.send(GSON.toJson(msg));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void startListening() {
        new Thread(() -> {
            try {
                String serverMessage;
                while ((serverMessage = jsonMessageHandler.receive()) != null) {
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
                            if (App.getInstance().getCurrentGame().getCurrentPlayer().getAudioStreamer() == null) {
                                App.getInstance().getCurrentGame().getCurrentPlayer().setAudioStreamer(new AudioStreamer());
                                App.getInstance().getCurrentGame().getCurrentPlayer().getAudioStreamer().start();
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
                        } else if (obj.get("action").getAsString().equals("=update_player_add_to_inventory")) {
                            String username = obj.get("id").getAsString();
                            int amount = obj.get("amount").getAsInt();
                            Object salable = decodeObject(body);
                            service.handleAddToInventory(username, (Salable) salable, amount);
                        } else if (obj.get("action").getAsString().equals("=update_player_delete_from_inventory")) {
                            String username = obj.get("id").getAsString();
                            int amount = obj.get("amount").getAsInt();
                            Object salable = decodeObject(body);
                            service.handleDeleteFromInventory(username, (Salable) salable, amount);
                        } else if (obj.get("action").getAsString().equals("=update_player_just_delete_from_inventory")) {
                            String username = obj.get("id").getAsString();
                            int amount = obj.get("amount").getAsInt();
                            Object salable = decodeObject(body);
                            service.handleJustDeleteFromInventory(username, (Salable) salable, amount);
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
                        } else if (obj.get("action").getAsString().equals("propose_fire")) {
                            fireMenu.set(new FireMenu());
                            synchronized (terminateMenu) {
                                fireMenu.get().setPlayer(service.getPlayerByUsername(body.get("player").getAsString()));
                                fireMenu.get().createMenu(GameView.stage, GameAsset.SKIN, WorldController.getInstance());
                            }
                        } else if (obj.get("action").getAsString().equals("fire")) {
                            synchronized (fireMenu) {
                                if (!body.get("vote").getAsBoolean()) {
                                    fireMenu.get().undoTermination();
                                }
                            }
                        } else if (obj.get("action").getAsString().equals("fire_accomplished")) {
                            if (service.getPlayerByUsername(body.get("player").getAsString()).equals(App.getInstance().getCurrentGame().getCurrentPlayer())) {
                                service.die();
                            } else {
                                synchronized (fireMenu) {
                                    fireMenu.get().terminate();
                                }
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
                        } else if (obj.get("action").getAsString().equals("trade")) {
                            TradeService.getInstance().addTradeByServer(
                                service.getPlayerByUsername(body.get("customer").getAsString()),
                                service.getPlayerByUsername(body.get("trader").getAsString()),
                                body.get("salable").getAsString(),
                                body.get("quantity").getAsInt(),
                                body.has("requiredItem") ? body.get("requiredItem").getAsString() : null,
                                body.has("quantityRequired") ? body.get("quantityRequired").getAsInt() : null,
                                body.has("price") ? body.get("price").getAsInt() : null,
                                body.get("id").getAsInt()
                            );
                        } else if (obj.get("action").getAsString().equals("trade_reject")) {
                            TradeService.getInstance().rejectTrade(
                                service.getPlayerByUsername(obj.get("id").getAsString()),
                                body.get("id").getAsInt()
                            );
                        } else if (obj.get("action").getAsString().equals("trade_reject_by_state")) {
                            TradeService.getInstance().rejectTradeByState(
                                service.getPlayerByUsername(obj.get("id").getAsString()),
                                body.get("id").getAsInt()
                            );
                        } else if (obj.get("action").getAsString().equals("trade_accept")) {
                            TradeService.getInstance().handleAccept(
                                service.getPlayerByUsername(obj.get("id").getAsString()),
                                body.get("id").getAsInt()
                            );
                        } else if (obj.get("action").getAsString().equals("send_trade")) {
                            TradeService.getInstance().sendTrade(
                                service.getPlayerByUsername(body.get("customer").getAsString()),
                                body.get("id").getAsInt(),
                                body.has("salable") ? (Salable) decodeObject(body.getAsJsonObject("salable")) : null,
                                body.get("count").getAsInt(),
                                body.get("shouldRespond").getAsBoolean()
                            );
                        } else if (obj.get("action").getAsString().equals("_send_public")) {
                            App.getInstance().getCurrentGame().addPublicMessage(
                                service.getPlayerByUsername(obj.get("id").getAsString()),
                                body.get("message").getAsString()
                            );
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
                        } else if (obj.get("action").getAsString().equals("DC_player")) {
                            String username = obj.get("id").getAsString();
                            long time = obj.get("time").getAsLong();
                            service.handleDCPlayer(username, time);
                        } else if (obj.get("action").getAsString().equals("DC_termination")) {
                            GameService.getInstance().finalTermination();
                            //TODO delete gameServer
                            Gdx.app.postRunnable(() -> {
                                MainGradle.getInstance().getScreen().dispose();
                                MainGradle.getInstance().initialMenu();
                            });
                            //TODO transfer to lobby
                        } else if (obj.get("action").getAsString().equals("update_player_connection")) {
                            String username = obj.get("id").getAsString();
                            service.handlePlayerReConnect(username);
                        } else if (obj.get("action").getAsString().equals("finish_reconnect")) {
                            StartGameMenuController.getInstance().setReconnect(false);
                        } else if (obj.get("action").getAsString().equals("connect_radio")) {
                            int port = obj.get("port").getAsInt();
                            p2PReceiving = new P2PReceiving(port);
                            p2PReceiving.start();
                            App.getInstance().getCurrentGame().getCurrentPlayer().setReceiving(true);
                        } else if (obj.get("action").getAsString().equals("finish_load")) {
                            StartGameMenuController.getInstance().setLoad(false);
                            StartGameMenuController.getInstance().setReconnect(false);
                        } else if (obj.get("action").getAsString().equals("=create_lobby")) {
                            String username = obj.get("id").getAsString();
                            String lobbyName = obj.get("name").getAsString();
                            boolean isPrivate = obj.get("private").getAsBoolean();
                            boolean visible = obj.get("visible").getAsBoolean();
                            String password = isPrivate ? obj.get("password").getAsString() : null;
                            long id = obj.get("lobby_id").getAsLong();
                            service.createLobby(username, lobbyName, isPrivate, password, visible, id);
                        } else if (obj.get("action").getAsString().equals("=join_lobby")) {
                            String username = obj.get("id").getAsString();
                            long id = obj.get("lobby_id").getAsLong();
                            service.joinLobby(id, username);
                        } else if (obj.get("action").getAsString().equals("=left_lobby")) {
                            String username = obj.get("id").getAsString();
                            long id = obj.get("lobby_id").getAsLong();
                            service.leftLobby(id, username);
                        } else if (obj.get("action").getAsString().equals("send_lobbies")) {
                            JsonArray lobbyArray = obj.get("lobbies").getAsJsonArray();
                            Type lobbyListType = new TypeToken<List<Lobby>>() {
                            }.getType();
                            List<Lobby> lobbies = GSON.fromJson(lobbyArray, lobbyListType);
                            service.receiveLobbies(lobbies);
                        } else if (obj.get("action").getAsString().equals("delete_lobby")) {
                            long id = obj.get("lobby_id").getAsLong();
                            service.handleDeleteLobby(id);
                        } else if (obj.get("action").getAsString().equals("=start_game")) {
                            long id = obj.get("lobby_id").getAsLong();
                            service.startGame(id);
                        }
                    } catch (JsonParseException e) {
                        System.out.println("Received non-JSON: " + serverMessage);
                    }
                }
            } catch (IOException e) {
                debug(e);
            }
        }).start();
    }

    public void sendGameStateToServer(String gameState) {
        try {
            jsonMessageHandler.send(GSON.toJson(gameState));
        } catch (Exception e) {
        }
    }

    public void enterRoom(long id) {
        try {
            Map<String, Object> msg = Map.of(
                "action", "enter_room",
                "id", Session.getCurrentUser().getUsername(),
                "port", App.PORT,
                "body", Map.of("id", id)
            );

            jsonMessageHandler.send(GSON.toJson(msg));
        } catch (IOException e) {
            debug(e);
        }
    }

    public void reLoadGame(int id) {
        try {
            Map<String, Object> msg = Map.of(
                "action", "load",
                "id", Session.getCurrentUser().getUsername(),
                "port", App.PORT,
                "body", Map.of("id", id)
            );

            jsonMessageHandler.send(GSON.toJson(msg));
        } catch (IOException e) {
            debug(e);
        }
    }

    public void chooseFarm(int farmId, String character) {
        try {
            Map<String, Object> msg = Map.of(
                "action", "choose_farm",
                "id", Session.getCurrentUser().getUsername(),
                "body", Map.of("farmId", farmId, "character", character)
            );

            jsonMessageHandler.send(GSON.toJson(msg));
        } catch (IOException e) {
            debug(e);
        }
    }

    public void updateFarmCrowAttack(Farm farm, boolean AttackToday, HarvestAbleProduct harvestAbleProduct) {
        try {
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

            jsonMessageHandler.send(GSON.toJson(msg));
        } catch (IOException e) {
            debug(e);
        }
    }

    public void updateStoreDailySold(Shop shop, int amount) {
        try {
            Map<String, Object> msg = Map.of(
                "action", "=increase_daily_sold",
                "id", Session.getCurrentUser().getUsername(),
                "amount", amount,
                "body", encodeObject(shop)
            );

            jsonMessageHandler.send(GSON.toJson(msg));
        } catch (IOException e) {
            debug(e);
        }
    }

    public void updatePlayerPosition(Player player) {
        try {
            Map<String, Object> msg = Map.of(
                "action", "=update_player_position",
                "id", player.getUser().getUsername(),
                "body", Map.of("position_x", player.getTiles().get(0).getX(),
                    "position_y", player.getTiles().get(0).getY(), "direction", player.getDirection().ordinal())
            );
            jsonMessageHandler.send(GSON.toJson(msg));
        } catch (IOException e) {
            debug(e);
        }
    }

    public void updatePlayerAddToInventory(Player player, Salable salable, int amount) {
//        try {
//            Map<String, Object> msg = Map.of(
//                "action", "=update_player_add_to_inventory",
//                "id", player.getUser().getUsername(),
//                "amount", amount,
//                "body", encodeObject(salable)
//            );
//            out.println(GSON.toJson(msg));
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }

    public void updatePlayerDeleteFromInventory(Player player, Salable salable, int amount) {
//        try {
//            Map<String, Object> msg = Map.of(
//                "action", "=update_player_delete_from_inventory",
//                "id", player.getUser().getUsername(),
//                "amount", amount,
//                "body", encodeObject(salable)
//            );
//            out.println(GSON.toJson(msg));
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }

    public void updatePlayerJustDeleteFromInventory(Player player, Salable salable, int amount) {
        try {
            Map<String, Object> msg = Map.of(
                "action", "=update_player_just_delete_from_inventory",
                "id", player.getUser().getUsername(),
                "amount", amount,
                "body", encodeObject(salable)
            );
            jsonMessageHandler.send(GSON.toJson(msg));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void updatePlayerCarryingObject(Player player) {
        try {
            Map<String, Object> msg = Map.of(
                "action", "=update_player_carrying",
                "id", player.getUser().getUsername(),
                "body", encodeObject(player.getCurrentCarrying())
            );
            jsonMessageHandler.send(GSON.toJson(msg));
        } catch (IOException e) {
            debug(e);
        }
    }

    public void updateNpcMissionState(Mission mission) {
        try {
            Map<String, Object> msg = Map.of(
                "action", "=update_npc_mission",
                "id", Session.getCurrentUser().getUsername(),
                "mission_id", mission.getId(),
                "npc_type", mission.getRequester().getName()
            );
            jsonMessageHandler.send(GSON.toJson(msg));
        } catch (IOException e) {
            debug(e);
        }
    }

    public void updatePlayerReaction(Player player) {
        try {
            Map<String, Object> msg = new HashMap<>();
            msg.put("action", "=update_player_reaction");
            msg.put("id", Session.getCurrentUser().getUsername());
            msg.put("emoji_index", player.getEmojiReactionIndex());
            msg.put("text", player.getTextReaction());

            jsonMessageHandler.send(GSON.toJson(msg));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void updatePlayerGold(Player player) {
        try {
            Map<String, Object> msg = new HashMap<>();
            msg.put("action", "=update_player_gold");
            msg.put("id", Session.getCurrentUser().getUsername());
            msg.put("gold", player.getAccount().getGolds());

            jsonMessageHandler.send(GSON.toJson(msg));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void updatePlayerNumberOfCompleteMissions(Player player) {
        try {
            Map<String, Object> msg = new HashMap<>();
            msg.put("action", "=update_player_number_of_complete_missions");
            msg.put("id", Session.getCurrentUser().getUsername());
            msg.put("complete_missions", player.getNumberOfCompleteMission());

            jsonMessageHandler.send(GSON.toJson(msg));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void updatePlayerSkill(Player player, Ability ability) {
        try {
            Map<String, Object> msg = new HashMap<>();
            msg.put("action", "=update_player_skill");
            msg.put("id", Session.getCurrentUser().getUsername());
            msg.put("ability", ability.getName());
            msg.put("amount", player.getAbilities().get(ability));

            jsonMessageHandler.send(GSON.toJson(msg));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void updateMultiMissionPlayer(MultiMission mission) {
        try {
            Map<String, Object> msg = new HashMap<>();
            msg.put("action", "=update_multi_mission_player");
            msg.put("id", Session.getCurrentUser().getUsername());
            msg.put("mission_id", mission.getId());

            jsonMessageHandler.send(GSON.toJson(msg));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void updateMultiMissionAdd(MultiMission mission, int amount) {
        try {
            Map<String, Object> msg = new HashMap<>();
            msg.put("action", "=update_multi_mission_add");
            msg.put("id", Session.getCurrentUser().getUsername());
            msg.put("mission_id", mission.getId());
            msg.put("amount", amount);

            jsonMessageHandler.send(GSON.toJson(msg));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void updateRadioConnection(String username) {
        try {
            Map<String, Object> msg = new HashMap<>();
            msg.put("action", "update_radio_connection");
            msg.put("id", Session.getCurrentUser().getUsername());
            msg.put("connect_to", username);

            jsonMessageHandler.send(GSON.toJson(msg));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Object decodeObject(JsonObject body) {
        if (body == null) return null;
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
            debug(e);
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
            debug(e);
            return null;
        }
        return obj;
    }

    private Map<String, Object> encodeObject(Object object) {
        if (object == null) return null;
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
            if (state.equals(StructureUpdateState.DELETE)) {
                Map<String, Object> msg = Map.of(
                    "action", state.getName(),
                    "id", Session.getCurrentUser().getUsername(),
                    "body", Map.of("tiles", structure.getTiles(),
                        "inFarm", inFarm)
                );
                jsonMessageHandler.send(GSON.toJson(msg));
            } else {
                Map<String, Object> msg = Map.of(
                    "action", state.getName(),
                    "id", Session.getCurrentUser().getUsername(),
                    "body", encodeStructure(structure, previousTile)
                );
                jsonMessageHandler.send(GSON.toJson(msg));
            }
        } catch (IOException e) {
            debug(e);
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
            debug(e);
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
            debug(e);
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
                    debug(e);
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
                    debug(e);
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
            Map<String, Object> msg = Map.of(
                "action", "=update_tile",
                "id", Session.getCurrentUser().getUsername(),
                "body", Map.of("tile", tile)
            );

            jsonMessageHandler.send(GSON.toJson(msg));
        } catch (IOException e) {
            debug(e);
        }
    }

    public void skipTime(int minutes) {
        try {
            Map<String, Object> msg = Map.of(
                "action", "_skip_time",
                "id", Session.getCurrentUser().getUsername(),
                "body", Map.of("minutes", minutes)
            );

            jsonMessageHandler.send(GSON.toJson(msg));
        } catch (IOException e) {
            debug(e);
        }
    }

    public void readyForSleep() {
        try {
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

            jsonMessageHandler.send(GSON.toJson(msg));
        } catch (IOException e) {
            debug(e);
        }
    }

    public void thor(String x, String y) {
        try {
            Map<String, Object> msg = Map.of(
                "action", "_thor",
                "id", Session.getCurrentUser().getUsername(),
                "body", Map.of("x", x, "y", y)
            );

            jsonMessageHandler.send(GSON.toJson(msg));
        } catch (IOException e) {
            debug(e);
        }
    }

    public void setWeather(String type) {
        try {
            Map<String, Object> msg = Map.of(
                "action", "_set_weather",
                "id", Session.getCurrentUser().getUsername(),
                "body", Map.of("weather", type)
            );

            jsonMessageHandler.send(GSON.toJson(msg));
        } catch (IOException e) {
            debug(e);
        }
    }

    public void buildGreenhouse() {
        try {
            Map<String, Object> msg = Map.of(
                "action", "=build_greenhouse",
                "id", Session.getCurrentUser().getUsername(),
                "body", Map.of()
            );

            jsonMessageHandler.send(GSON.toJson(msg));
        } catch (IOException e) {
            debug(e);
        }
    }

    public void faint() {
        try {
            Map<String, Object> msg = Map.of(
                "action", "_faint",
                "id", Session.getCurrentUser().getUsername(),
                "body", Map.of()
            );

            jsonMessageHandler.send(GSON.toJson(msg));
        } catch (IOException e) {
            debug(e);
        }
    }

    public void askMarriage(String username) {
        handleInteraction(username, "_ask_marriage");
    }

    private void handleInteraction(String username, String action) {
        try {
            Map<String, Object> msg = Map.of(
                "action", action,
                "id", Session.getCurrentUser().getUsername(),
                "body", Map.of("requested", username)
            );

            jsonMessageHandler.send(GSON.toJson(msg));
        } catch (IOException e) {
            debug(e);
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
            Map<String, Object> msg = Map.of(
                "action", "_respond_marriage",
                "id", Session.getCurrentUser().getUsername(),
                "body", Map.of("requested", username, "response", b)
            );
            jsonMessageHandler.send(GSON.toJson(msg));
        } catch (IOException e) {
            debug(e);
        }
    }

    public void setGolds(int count, String couple) {
        try {
            Map<String, Object> msg = Map.of(
                "action", "set_golds",
                "id", Session.getCurrentUser().getUsername(),
                "body", Map.of("count", count, "receiver", couple)
            );
            jsonMessageHandler.send(GSON.toJson(msg));
        } catch (IOException e) {
            debug(e);
        }
    }

    public void sendGift(Player player, Salable gift, int amount) {
        try {
            Map<String, Object> msg = Map.of(
                "action", "send_gift",
                "id", Session.getCurrentUser().getUsername(),
                "body", Map.of("amount", amount, "receiver", player.getUser().getUsername(),
                    "gift", encodeStructure(gift, null))
            );
            jsonMessageHandler.send(GSON.toJson(msg));
        } catch (IOException e) {
            debug(e);
        }
    }

    public void notifyPlayer(String username, Response response, NotificationType type, Actor source) {
        try {
            Map<String, Object> msg = Map.of(
                "action", "notify",
                "id", Session.getCurrentUser().getUsername(),
                "body", Map.of("response_message", response.message(), "receiver", username,
                    "response_bool", response.shouldBeBack(), "type", type.ordinal(),
                    "isFromPlayer", source instanceof Player, "source", source.getName())
            );
            jsonMessageHandler.send(GSON.toJson(msg));
        } catch (IOException e) {
            debug(e);
        }
    }

    public void talk(Player anotherPlayer, String message) {
        try {
            Map<String, Object> msg = Map.of(
                "action", "talk",
                "id", Session.getCurrentUser().getUsername(),
                "body", Map.of("message", message, "receiver", anotherPlayer.getName())
            );
            jsonMessageHandler.send(GSON.toJson(msg));
        } catch (IOException e) {
            debug(e);
        }
    }

    public void rateGift(Integer giftId, int rate, String giver) {
        try {
            Map<String, Object> msg = Map.of(
                "action", "rate_gift",
                "id", Session.getCurrentUser().getUsername(),
                "body", Map.of("giftId", giftId, "receiver", giver, "rate", rate)
            );
            jsonMessageHandler.send(GSON.toJson(msg));
        } catch (IOException e) {
            debug(e);
        }
    }

    public void emptyAction(String string) {
        try {
            Map<String, Object> msg = Map.of(
                "action", string,
                "id", Session.getCurrentUser().getUsername(),
                "body", Map.of()
            );
            jsonMessageHandler.send(GSON.toJson(msg));
        } catch (IOException e) {
            debug(e);
        }
    }

    public void fire(boolean b, String name) {
        try {
            Map<String, Object> msg = Map.of(
                "action", "fire",
                "id", Session.getCurrentUser().getUsername(),
                "body", Map.of("player", name, "vote", b)
            );
            jsonMessageHandler.send(GSON.toJson(msg));
        } catch (IOException e) {
            debug(e);
        }
    }

    public void startFiring(String name) {
        try {
            Map<String, Object> msg = Map.of(
                "action", "propose_fire",
                "id", Session.getCurrentUser().getUsername(),
                "body", Map.of("player", name)
            );
            jsonMessageHandler.send(GSON.toJson(msg));
        } catch (IOException e) {
            debug(e);
        }
    }

    public void refrigeratorPut(Salable name, Integer count) {
        try {
            Map<String, Object> msg = Map.of(
                "action", "=fridge_put",
                "id", Session.getCurrentUser().getUsername(),
                "body", Map.of("name", encodeStructure(name, null), "count", count)
            );
            jsonMessageHandler.send(GSON.toJson(msg));
        } catch (IOException e) {
            debug(e);
        }
    }

    public void refrigeratorPick(String name) {
        try {
            Map<String, Object> msg = Map.of(
                "action", "=fridge_pick",
                "id", Session.getCurrentUser().getUsername(),
                "body", Map.of("name", name)
            );
            jsonMessageHandler.send(GSON.toJson(msg));
        } catch (IOException e) {
            debug(e);
        }
    }

    public void setTrade(Trade trade) {
        try {
            Map<String, Object> msg = Map.of(
                "action", "trade",
                "id", Session.getCurrentUser().getUsername(),
                "body", Map.of(
                    "receiver", trade.getCustomer().equals(App.getInstance().getCurrentGame().getCurrentPlayer()) ? trade.getTrader().getName() : trade.getCustomer().getName(),
                    "salable", trade.getSalable(),
                    "quantity", trade.getQuantity(),
                    "trader", trade.getTrader().getName(),
                    "customer", trade.getCustomer().getName(),
                    trade.getPrice() == null ? "requiredItem" : "gold", trade.getPrice() == null ? trade.getRequiredItem() : "",
                    trade.getPrice() == null ? "quantityRequired" : "price", trade.getPrice() == null ? trade.getQuantityRequired() : trade.getPrice(),
                    "id", trade.getId()
                )
            );
            jsonMessageHandler.send(GSON.toJson(msg));
        } catch (IOException e) {
            debug(e);
        }
    }

    private void debug(Exception e) {
        e.printStackTrace();
    }

    public void rejectTrade(Trade trade) {
        try {
            Map<String, Object> msg = Map.of(
                "action", "trade_reject",
                "id", Session.getCurrentUser().getUsername(),
                "body", Map.of(
                    "receiver", trade.getTrader().getName(),
                    "id", trade.getId()
                )
            );
            jsonMessageHandler.send(GSON.toJson(msg));
        } catch (IOException e) {
            debug(e);
        }
    }

    public void acceptTrade(Player trader, int tradeId, boolean b) {
        try {
            Map<String, Object> msg = Map.of(
                "action", "trade_accept",
                "id", Session.getCurrentUser().getUsername(),
                "body", Map.of(
                    "receiver", trader.getName(),
                    "id", tradeId,
                    "shouldRespond", b
                )
            );
            jsonMessageHandler.send(GSON.toJson(msg));
        } catch (IOException e) {
            debug(e);
        }
    }

    public void rejectTradeByState(Trade trade) {
        try {
            Map<String, Object> msg = Map.of(
                "action", "trade_reject_by_state",
                "id", Session.getCurrentUser().getUsername(),
                "body", Map.of(
                    "receiver", trade.getTrader().getName(),
                    "id", trade.getId()
                )
            );
            jsonMessageHandler.send(GSON.toJson(msg));
        } catch (IOException e) {
            debug(e);
        }
    }


    public void sendTrade(Player receiver, Trade trade, Integer value, Salable key, boolean b) {
        try {
            Map<String, Object> msg = Map.of(
                "action", "send_trade",
                "id", Session.getCurrentUser().getUsername(),
                "body", Map.of(
                    "receiver", receiver.getName(),
                    "customer", trade.getCustomer().getName(),
                    "id", trade.getId(),
                    "shouldRespond", b,
                    key == null ? "price" : "salable", key == null ? "" : encodeObject(key),
                    "count", value
                )
            );
            jsonMessageHandler.send(GSON.toJson(msg));
        } catch (IOException e) {
            debug(e);
        }
    }

    public void sendPublic(String message) {
        try {
            Map<String, Object> msg = Map.of(
                "action", "_send_public",
                "id", Session.getCurrentUser().getUsername(),
                "body", Map.of("message", message)
            );
            jsonMessageHandler.send(GSON.toJson(msg));
        } catch (IOException e) {
            debug(e);
        }
    }
}

