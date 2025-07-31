package io.github.some_example_name.server;

import com.google.gson.*;
import io.github.some_example_name.common.JsonMessageHandler;
import io.github.some_example_name.common.model.*;
import io.github.some_example_name.server.model.GameServer;
import io.github.some_example_name.server.model.GameThread;
import io.github.some_example_name.server.model.ServerPlayer;
import io.github.some_example_name.server.service.ServerService;
import lombok.Getter;
import lombok.Setter;

import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
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
    private final Map<String, Long> playerLastPing = new HashMap<>();
    private final ScheduledExecutorService pingHandler = Executors.newScheduledThreadPool(1);
    private final ServerService service = new ServerService(this);

    public ClientHandler(Socket socket) {
        this.clientSocket = socket;
        try {
            this.jsonMessageHandler = new JsonMessageHandler(clientSocket.getInputStream(), clientSocket.getOutputStream());
        } catch (Exception ignored) {
        }
    }

    private void handlePlayerDC(String username, long DCTime, long lastPing) {
        if (DCTime >= 2 * 60 * 1000) {
            sendTerminationMessage(username);
        } else if (DCTime > 20 * 1000) {
            if (!gameServer.getDCPlayers().containsKey(username)) {
                gameServer.getDCPlayers().put(username, lastPing);
                sendDCMessage(username, lastPing);
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
        gameServer.sendAllBut(GSON.toJson(msg), username);
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
                    JsonObject obj = JsonParser.parseString(message).getAsJsonObject();

                    if (obj.get("action").getAsString().equals("connected")) {
//                        String username = obj.get("id").getAsString();
                        System.out.println("Client Connected");
//                        send("Game state updated for " + username);
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
                        gameServer = GameThread.getInstance().getGameServer(
                            obj.getAsJsonObject("body").get("id").getAsInt()
                        );
                        String username = obj.get("id").getAsString();
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
                            gameServer.getClients().add(new Entry<>(new ServerPlayer(username), this));
                        }
                        pingHandler.scheduleAtFixedRate(() -> {
                            for (Map.Entry<String, Long> stringLongEntry : gameServer.getPlayerLastPing().entrySet()) {
                                long lastPing = stringLongEntry.getValue();
                                long now = System.currentTimeMillis();
                                handlePlayerDC(stringLongEntry.getKey(), now - lastPing, lastPing);
                            }
                        }, 0, 5, TimeUnit.SECONDS);
                    } else if (obj.get("action").getAsString().equals("choose_farm")) {
                        Map<String, Object> msg = Map.of(
                            "action", "response_choose_farm",
                            "id", "!server!",
                            "body", Map.of("response", gameServer.checkAvailability(
                                obj.get("id").getAsString(),
                                obj.getAsJsonObject("body").get("farmId").getAsInt(),
                                obj.getAsJsonObject("body").get("character").getAsString())
                            )
                        );
                        send(GSON.toJson(msg));
                    } else if (obj.get("action").getAsString().equals("DC_reconnect")) {
                        String username = obj.get("id").getAsString();
                        handlePlayerReConnect(username);
                        service.DCReconnect(username);
                        Map<String, Object> msg = Map.of(
                            "action", "finish_reconnect"
                        );
                        send(GSON.toJson(msg));
                    } else if (obj.get("action").getAsString().equals("ping")) {
                        String username = obj.get("id").getAsString();
                        gameServer.getPlayerLastPing().put(username, System.currentTimeMillis());
                    } else if (obj.get("action").getAsString().equals("ready_for_sleep")) {
                        ready = true;
                        if (gameServer.isReady()) gameServer.sendAll(message);
                    } else if (obj.get("action").getAsString().equals("propose_end")) {
                        gameServer.clearFavors();
                        gameServer.sendAll(message);
                    } else if (obj.get("action").getAsString().equals("stop_termination")) {
                        gameServer.clearFavors();
                        gameServer.sendAllBut(message, obj.get("id").getAsString());
                    } else if (obj.get("action").getAsString().equals("continue_termination")) {
                        inFavor = true;
                        if (gameServer.isMajority()) {
                            gameServer.terminate();
                        }
                    } else if (obj.get("action").getAsString().equals("propose_fire")) {
                        gameServer.clearFavors();
                        gameServer.sendFire(message, obj.getAsJsonObject("body").get("player").getAsString());
                    } else if (obj.get("action").getAsString().equals("fire")) {
                        if (obj.getAsJsonObject("body").get("vote").getAsBoolean()) {
                            inFavor = true;
                            if (gameServer.isMajority()) {
                                String player = obj.getAsJsonObject("body").get("player").getAsString();
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
                    } else if (obj.get("action").getAsString().charAt(0) == '_') {
                        gameServer.sendAll(message);
                    } else if (obj.get("action").getAsString().charAt(0) == '=') {
                        String username = obj.get("id").getAsString();
                        gameServer.sendAllBut(GSON.toJson(obj), username);
                    } else if (obj.getAsJsonObject("body").has("receiver")) {
                        String username = obj.getAsJsonObject("body").get("receiver").getAsString();
                        for (Entry<ServerPlayer, ClientHandler> client : gameServer.getClients()) {
                            if (client.getKey().getUsername().equals(username)) {
                                client.getValue().send(message);
                            }
                        }
                    }
                } catch (JsonParseException e) {
                    System.out.println("Received non-JSON message: " + message);
                }
            }
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
