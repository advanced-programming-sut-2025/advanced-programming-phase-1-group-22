package io.github.some_example_name.server;

import com.google.gson.*;
import io.github.some_example_name.common.JsonMessageHandler;
import io.github.some_example_name.common.model.*;
import io.github.some_example_name.common.utils.App;
import io.github.some_example_name.server.model.GameServer;
import io.github.some_example_name.server.model.GameThread;
import io.github.some_example_name.server.model.ServerPlayer;
import io.github.some_example_name.server.saveGame.GameSaver;
import io.github.some_example_name.server.service.ServerService;
import lombok.Getter;
import lombok.Setter;

import java.io.EOFException;
import java.io.IOException;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Getter
@Setter
public class ClientHandler extends Thread {
    private static final Gson GSON = new GsonBuilder().serializeNulls().create();
    private Socket clientSocket;
    private JsonMessageHandler jsonMessageHandler;
    private boolean dead = false;
    private boolean ready = false;
    private boolean running = true;
    private boolean inFavor = false;
    private GameServer gameServer;
    private final ScheduledExecutorService pingHandler = Executors.newScheduledThreadPool(1);
    private final ScheduledExecutorService loginPingHandler = Executors.newScheduledThreadPool(1);
    private final ServerService service = new ServerService(this);

    public ClientHandler(Socket socket) {
        this.clientSocket = socket;
        try {
            this.jsonMessageHandler = new JsonMessageHandler(clientSocket.getInputStream(), clientSocket.getOutputStream());
        } catch (Exception ignored) {
        }
        handleUnUseLobbies();
    }

    private void handleUnUseLobbies() {
        ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();

        executor.scheduleAtFixedRate(() -> {
            long now = System.currentTimeMillis();

            synchronized (App.getInstance().getLobbies()) {
                Iterator<Lobby> iterator = App.getInstance().getLobbies().iterator();

                while (iterator.hasNext()) {
                    Lobby lobby = iterator.next();
                    boolean hasActiveMembers = false;
                    for (String member : lobby.getMembers()) {
                        if (lobby.getAdmin().equals(member)) continue;
                        if (GameThread.getInstance().getConnections().containsKey(member)) {
                            hasActiveMembers = true;
                            break;
                        }
                    }

                    long inactiveTime = now - lobby.getLastTimeNoPlayer();

                    if (!lobby.isGameStart() && !hasActiveMembers && inactiveTime > 5 * 60 * 1000) {
                        iterator.remove();
                        deleteLobbyMessage(lobby.getId());
                    }
                }
            }
        }, 0, 30, TimeUnit.SECONDS);
    }

    private void deleteLobbyMessage(long id) {
        Map<String, Object> msg = Map.of(
            "action", "delete_lobby",
            "lobby_id", id
        );
        GameThread.getInstance().sendAll(GSON.toJson(msg));
    }

    private void clientStatus(String username, String lobby, boolean add) {
        Map<String, Object> msg;
        if ("".equals(lobby)) {
            msg = Map.of(
                "action", "client_status",
                "id", "!server!",
                "body", Map.of("username", username)
            );
            if (GameThread.getInstance().getUsers().containsKey(username)) {
                GameThread.getInstance().getUsers().remove(username);
            } else {
                GameThread.getInstance().getUsers().put(username, new ArrayList<>());
            }
        } else {
            msg = Map.of(
                "action", "client_status",
                "id", "!server!",
                "body", Map.of("username", username, "lobby", lobby, "add", add)
            );

            if (add) {
                GameThread.getInstance().getUsers().get(username).add(lobby);
            } else {
                GameThread.getInstance().getUsers().get(username).remove(lobby);
            }
        }
        GameThread.getInstance().sendAll(GSON.toJson(msg));
    }


    private void handlePlayerDC(String username, long DCTime, long lastPing) {
        if (DCTime >= 2 * 60 * 1000) {
            sendTerminationMessage(username);
        } else if (DCTime > 20 * 1000) {
            if (!gameServer.getDCPlayers().containsKey(username)) {
                gameServer.getDCPlayers().put(username, lastPing);
                sendDCMessage(username, lastPing);
                clientStatus(username, "", true);
            }
        }
    }

    private void sendDCMessage(String username, long DCTime) {
        Map<String, Object> msg = Map.of(
            "action", "DC_player",
            "id", username,
            "time", DCTime
        );
        gameServer.sendAllBut(GSON.toJson(msg), username);
    }

    private void sendTerminationMessage(String username) {
        Map<String, Object> msg = Map.of(
            "action", "DC_termination",
            "id", username
        );
        synchronized (App.getInstance().getLobbies()) {
            for (Lobby lobby : App.getInstance().getLobbies()) {
                if (lobby.getGameServer().equals(gameServer)) {
                    lobby.setGameStart(false);
                    lobby.setGameServerSaved(true);
                }
            }
        }
        gameServer.sendAllBut(GSON.toJson(msg), username);
        try {
            GameSaver.saveLobbies(App.getInstance().getLobbies(), "games.json");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void handlePlayerReConnect(String username) {
        gameServer.getDCPlayers().remove(username);
        gameServer.getPlayerLastPing().put(username, System.currentTimeMillis());
        Map<String, Object> msg = Map.of(
            "action", "update_player_connection",
            "id", username
        );
        gameServer.sendAll(GSON.toJson(msg));
    }

    @Override
    public void run() {
        try {
            String message;
            while (running && (message = jsonMessageHandler.receive()) != null) {
                try {
                    JsonObject obj = null;
                    try {
                        obj = JsonParser.parseString(message).getAsJsonObject();
                    } catch (JsonSyntaxException e) {
                        System.out.println("Received non-JSON message: " + message);
                    }
                    if (obj == null) continue;

                    JsonObject body = obj.getAsJsonObject("body");
                    if (obj.get("action").getAsString().equals("connected")) {
                        String port = obj.get("port").getAsString();
                        System.out.println("Client Connected: " + port);
                        if (GameThread.getInstance().getAutoLogins().containsKey(port)) {
                            Map<String, Object> msg = Map.of(
                                "action", "auto_login",
                                "body", Map.of(
                                    "username", GameThread.getInstance().getAutoLogins().get(port).getX(),
                                    "password", GameThread.getInstance().getAutoLogins().get(port).getY()
                                )
                            );
                            send(GSON.toJson(msg));
                        }
                    } else if (obj.get("action").getAsString().equals("login")) {
                        String username = obj.get("id").getAsString();
                        GameThread.getInstance().getConnections().put(username, this);
                        if (!App.getInstance().getLobbies().isEmpty()) {
                            Map<String, Object> msg = Map.of(
                                "action", "send_lobbies",
                                "lobbies", App.getInstance().getLobbies()
                            );
                            send(GSON.toJson(msg));
                        }
                        loginPingHandler.scheduleAtFixedRate(() -> {
                            for (Map.Entry<String, Long> stringLongEntry : GameThread.getInstance().getLastConnections().entrySet()) {
                                long lastPing = stringLongEntry.getValue();
                                long now = System.currentTimeMillis();
                                if ((now - lastPing) > 30 * 1000) {
                                    GameThread.getInstance().getConnections().remove(stringLongEntry.getKey());
                                    System.out.println(GameThread.getInstance().getConnections().size());
                                }
                            }
                        }, 0, 5, TimeUnit.SECONDS);
                        if (!GameThread.getInstance().getUsers().isEmpty()) {
                            Map<String, Object> msg = Map.of(
                                "action", "send_users",
                                "body", GameThread.getInstance().getUsers()
                            );
                            send(GSON.toJson(msg));
                        }
                        clientStatus(username, "", false);
                        if (body.get("stay_logged").getAsBoolean()) {
                            GameThread.getInstance().getAutoLogins().put(
                                body.get("port").getAsString(),
                                new Tuple<>(username, body.get("password").getAsString())
                            );
                        }
                    } else if (obj.get("action").getAsString().equals("create_lobby")) {
                        String username = obj.get("id").getAsString();
                        String lobbyName = obj.get("name").getAsString();
                        boolean isPrivate = obj.get("private").getAsBoolean();
                        boolean visible = obj.get("visible").getAsBoolean();
                        String password = isPrivate ? obj.get("password").getAsString() : null;
                        long id = obj.get("lobby_id").getAsLong();
                        service.createLobby(username, lobbyName, isPrivate, password, visible, id);
                        GameThread.getInstance().sendAllBut(GSON.toJson(obj), username);
                        clientStatus(username, isPrivate ? "<PrivateLobby>" : lobbyName, true);
                    } else if (obj.get("action").getAsString().equals("join_lobby")) {
                        String username = obj.get("id").getAsString();
                        long id = obj.get("lobby_id").getAsLong();
                        String lobbyName = service.joinLobby(id, username);
                        GameThread.getInstance().sendAllBut(GSON.toJson(obj), username);
                        clientStatus(username, lobbyName, true);
                    } else if (obj.get("action").getAsString().equals("left_lobby")) {
                        String username = obj.get("id").getAsString();
                        long id = obj.get("lobby_id").getAsLong();
                        String lobbyName = service.leftLobby(id, username);
                        GameThread.getInstance().sendAllBut(GSON.toJson(obj), username);
                        clientStatus(username, lobbyName, false);
                    } else if (obj.get("action").getAsString().equals("start_game")) {
                        String username = obj.get("id").getAsString();
                        long id = obj.get("lobby_id").getAsLong();
                        service.startGame(id);
                        GameThread.getInstance().sendAllBut(GSON.toJson(obj), username);
                    } else if (obj.get("action").getAsString().equals("ready_for_load_game")) {
                        String username = obj.get("id").getAsString();
                        long id = obj.get("lobby_id").getAsLong();
                        GameThread.getInstance().getReadyPlayersForLoad().put(username, id);
                        boolean ready = service.handleReadyForLoad(id);
                        if (ready) {
                            service.sendReadyMessage(id);
                        }
                    } else if (obj.get("action").getAsString().equals("give_up_load")) {
                        String username = obj.get("id").getAsString();
                        GameThread.getInstance().getReadyPlayersForLoad().remove(username);
                    } else if (obj.get("action").getAsString().equals("ready_for_game")) {
                        String username = obj.get("id").getAsString();
                        System.out.println("Client Ready: " + username);
                        ready = true;
                        if (gameServer.isReady()) {
                            Map<String, Object> msg = Map.of(
                                "action", "init_game",
                                "id", "!server!",
                                "body", Map.of(
                                    "stores", List.of(getRandomNumber(1, 10), getRandomNumber(71, 88),
                                        getRandomNumber(90, 106), getRandomNumber(27, 37), getRandomNumber(1, 10),
                                        getRandomNumber(130, 140), getRandomNumber(1, 9), getRandomNumber(29, 37),
                                        getRandomNumber(100, 112), getRandomNumber(132, 145), getRandomNumber(30, 40),
                                        getRandomNumber(100, 110)
                                    ),
                                    "doors", List.of(getRandomNumber(0, 300), getRandomNumber(0, 300),
                                        getRandomNumber(0, 300), getRandomNumber(0, 300), getRandomNumber(0, 300),
                                        getRandomNumber(0, 300), getRandomNumber(0, 300), getRandomNumber(0, 300)),
                                    "players", gameServer.getClients().stream()
                                        .map(e -> e.getKey().getUsername()).toList(),
                                    "farms", gameServer.getClients().stream()
                                        .map(e -> e.getKey().getFarmId()).toList(),
                                    "characters", gameServer.getClients().stream()
                                        .map(e -> e.getKey().getCharacter()).toList()
                                )
                            );

                            gameServer.sendAll(GSON.toJson(msg));
                        }
                    } else if (obj.get("action").getAsString().equals("enter_room")) {
                        gameServer = GameThread.getInstance().getGameServerForStart(
                            body.get("id").getAsLong()
                        );
                        String username = obj.get("id").getAsString();
                        int port = obj.get("port").getAsInt();
                        synchronized (gameServer) {
                            Entry<ServerPlayer, ClientHandler> entry = null;
                            for (Entry<ServerPlayer, ClientHandler> client : gameServer.getClients()) {
                                if (client.getKey().username.equals(username)) {
                                    entry = client;
                                    break;
                                }
                            }
                            if (entry != null) {
                                gameServer.getClients().remove(entry);
                            }
                            ServerPlayer serverPlayer = new ServerPlayer(username);
                            serverPlayer.port = port;
                            gameServer.getClients().add(new Entry<>(serverPlayer, this));
                        }
                        pingHandler.scheduleAtFixedRate(() -> {
                            for (Map.Entry<String, Long> stringLongEntry : gameServer.getPlayerLastPing().entrySet()) {
                                long lastPing = stringLongEntry.getValue();
                                long now = System.currentTimeMillis();
                                handlePlayerDC(stringLongEntry.getKey(), now - lastPing, lastPing);
                            }
                        }, 0, 5, TimeUnit.SECONDS);
                    } else if (obj.get("action").getAsString().equals("load")) {
                        long id = body.get("id").getAsLong();
                        synchronized (App.getInstance().getLobbies()) {
                            for (Lobby lobby : App.getInstance().getLobbies()) {
                                if (lobby.getId() == id) {
                                    gameServer = lobby.getGameServer();
                                }
                            }
                        }
                        String username = obj.get("id").getAsString();
                        int port = obj.get("port").getAsInt();
                        synchronized (gameServer) {
                            Entry<ServerPlayer, ClientHandler> entry = null;
                            for (Entry<ServerPlayer, ClientHandler> client : gameServer.getClients()) {
                                if (client.getKey().username.equals(username)) {
                                    entry = client;
                                    break;
                                }
                            }
                            if (entry != null) {
                                gameServer.getClients().remove(entry);
                            }
                            ServerPlayer serverPlayer = new ServerPlayer(username);
                            serverPlayer.port = port;
                            gameServer.getClients().add(new Entry<>(serverPlayer, this));
                        }
                        pingHandler.scheduleAtFixedRate(() -> {
                            for (Map.Entry<String, Long> stringLongEntry : gameServer.getPlayerLastPing().entrySet()) {
                                long lastPing = stringLongEntry.getValue();
                                long now = System.currentTimeMillis();
                                handlePlayerDC(stringLongEntry.getKey(), now - lastPing, lastPing);
                            }
                        }, 0, 5, TimeUnit.SECONDS);
                        service.DCReconnect();
                        Map<String, Object> msg = Map.of(
                            "action", "finish_load"
                        );
                        send(GSON.toJson(msg));
                    } else if (obj.get("action").getAsString().equals("choose_farm")) {
                        Map<String, Object> msg = Map.of(
                            "action", "response_choose_farm",
                            "id", "!server!",
                            "body", Map.of("response", gameServer.checkAvailability(
                                obj.get("id").getAsString(),
                                body.get("farmId").getAsInt(),
                                body.get("character").getAsString())
                            )
                        );
                        send(GSON.toJson(msg));
                    } else if (obj.get("action").getAsString().equals("DC_reconnect")) {
                        String username = obj.get("id").getAsString();
                        handlePlayerReConnect(username);
                        service.DCReconnect();
                        Map<String, Object> msg = Map.of(
                            "action", "finish_reconnect"
                        );
                        send(GSON.toJson(msg));
                    } else if (obj.get("action").getAsString().equals("ping")) {
                        String username = obj.get("id").getAsString();
                        gameServer.getPlayerLastPing().put(username, System.currentTimeMillis());
                    } else if (obj.get("action").getAsString().equals("login_ping")) {
                        String username = obj.get("id").getAsString();
                        GameThread.getInstance().getLastConnections().put(username, System.currentTimeMillis());
                    } else if (obj.get("action").getAsString().equals("ready_for_sleep")) {
                        ready = true;
                        if (gameServer.isReady()) {
                            gameServer.setDayEvents(new StringBuilder());
                            gameServer.getDayEvents().append("Today weather is ").append(gameServer.getTomorrowWeather()).append(".");
                            gameServer.sendAll(message);
                        }
                    } else if (obj.get("action").getAsString().equals("propose_end")) {
                        gameServer.clearFavors();
                        gameServer.sendAll(message);
                    } else if (obj.get("action").getAsString().equals("stop_termination")) {
                        gameServer.clearFavors();
                        gameServer.sendAllBut(message, obj.get("id").getAsString());
                    } else if (obj.get("action").getAsString().equals("continue_termination")) {
                        inFavor = true;
                        if (gameServer.isMajority()) {
                            synchronized (App.getInstance().getLobbies()) {
                                for (Lobby lobby : App.getInstance().getLobbies()) {
                                    if (lobby.getGameServer().equals(gameServer)) {
                                        lobby.setGameStart(false);
                                        lobby.setGameServerSaved(false);
                                        lobby.setGameServer(null);
                                    }
                                }
                            }
                            try {
                                GameSaver.saveLobbies(App.getInstance().getLobbies(), "games.json");
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            gameServer.terminate();
                        }
                    } else if (obj.get("action").getAsString().equals("save_game")) {
                        long id = obj.get("lobby_id").getAsLong();
                        String username = obj.get("id").getAsString();
                        synchronized (App.getInstance().getLobbies()) {
                            for (Lobby lobby : App.getInstance().getLobbies()) {
                                if (lobby.getId() == id) {
                                    lobby.setGameStart(false);
                                    lobby.setGameServerSaved(true);
                                }
                            }
                        }
                        try {
                            GameSaver.saveLobbies(App.getInstance().getLobbies(), "games.json");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        gameServer.sendAllBut(GSON.toJson(obj), username);
                    } else if (obj.get("action").getAsString().equals("logout")) {
                        String username = obj.get("id").getAsString();
                        GameThread.getInstance().getConnections().remove(username);
                        clientStatus(username,"",false);
                        if (GameThread.getInstance().getAutoLogins().containsKey(obj.get("port").getAsString())) {
                            GameThread.getInstance().getAutoLogins().remove(obj.get("port").getAsString());
                        }
                    } else if (obj.get("action").getAsString().equals("propose_fire")) {
                        gameServer.clearFavors();
                        gameServer.sendFire(message, body.get("player").getAsString());
                    } else if (obj.get("action").getAsString().equals("fire")) {
                        if (body.get("vote").getAsBoolean()) {
                            inFavor = true;
                            if (gameServer.isMajority()) {
                                String player = body.get("player").getAsString();
                                Map<String, Object> msg = Map.of(
                                    "action", "fire_accomplished",
                                    "id", "!server!",
                                    "body", Map.of("player", player)
                                );
                                gameServer.sendAll(GSON.toJson(msg));
                                gameServer.findClient(player).die();
                            }
                        } else {
                            gameServer.clearFavors();
                            gameServer.sendAllBut(message, obj.get("id").getAsString());
                        }
                    } else if (obj.get("action").getAsString().equals("_set_weather")) {
                        gameServer.setTomorrowWeather(body.get("weather").getAsString());
                    } else if (obj.get("action").getAsString().equals("_respond_marriage")) {
                        if (body.get("response").getAsBoolean()) {
                            gameServer.getDayEvents()
                                .append(body.get("requested").getAsString())
                                .append(" has married ").append(obj.get("id").getAsString());
                        } else {
                            gameServer.getDayEvents()
                                .append(body.get("requested").getAsString())
                                .append(" was rejected by ").append(obj.get("id").getAsString())
                                .append(". He feels so sad; what will he do with the ring?");
                        }
                    } else if (obj.get("action").getAsString().equals("npc_gift")) {
                        gameServer.addGift(
                            body.get("npc").getAsString(),
                            body.get("gift").getAsString(),
                            obj.get("id").getAsString()
                        );
                    } else if (obj.get("action").getAsString().equals("add_dialog")) {
                        gameServer.addDialog(
                            body.get("npc").getAsString(),
                            body.get("personality").getAsString()
                        );
                    } else if (obj.get("action").getAsString().equals("update_radio_connection")) {
                        String username = obj.get("id").getAsString();
                        String connect_to = obj.get("connect_to").getAsString();
                        Integer port = null;
                        for (Entry<ServerPlayer, ClientHandler> client : gameServer.getClients()) {
                            if (client.getKey().username.equals(connect_to)) {
                                port = client.getKey().port;
                            }
                        }
                        if (port == null) continue;
                        Map<String, Object> msg = Map.of(
                            "action", "connect_radio",
                            "port", port
                        );
                        gameServer.sendTo(GSON.toJson(msg), username);
                    }

                    if (obj.get("action").getAsString().charAt(0) == '_') {
                        gameServer.sendAll(message);
                    } else if (obj.get("action").getAsString().charAt(0) == '=') {
                        String username = obj.get("id").getAsString();
                        gameServer.sendAllBut(GSON.toJson(obj), username);
                    } else if (obj.has("body") && body.has("receiver")) {
                        String username = body.get("receiver").getAsString();
                        for (Entry<ServerPlayer, ClientHandler> client : gameServer.getClients()) {
                            if (client.getKey().getUsername().equals(username)) {
                                client.getValue().send(message);
                            }
                        }
                    }
                } catch (JsonParseException e) {
                    System.out.println("Received non-JSON message: " + message);
                    e.printStackTrace();
                }
            }
        } catch (EOFException eofException) {
            System.out.println("client disconnect: " + clientSocket);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                clientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void die() {
        dead = true;
    }

    private int getRandomNumber(int start, int end) {
        Random random = new Random();
        return random.nextInt(end - start + 1) + start;
    }

    public void send(String message) throws IOException {
        if (!dead) jsonMessageHandler.send(message);
    }
}
