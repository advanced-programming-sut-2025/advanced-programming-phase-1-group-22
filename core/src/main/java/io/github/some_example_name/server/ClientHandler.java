package io.github.some_example_name.server;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.google.gson.*;
import io.github.some_example_name.common.model.*;
import io.github.some_example_name.server.model.GameServer;
import io.github.some_example_name.server.model.GameThread;
import io.github.some_example_name.server.model.ServerPlayer;
import io.github.some_example_name.server.service.ServerService;
import lombok.Getter;
import lombok.Setter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;
import java.util.Map;
import java.util.Random;

@Getter
@Setter
public class ClientHandler extends Thread {
    private static final Gson GSON = new GsonBuilder().serializeNulls().create();
    private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;
    private boolean ready = false;
    private boolean inFavor = false;
    private GameServer gameServer;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final ServerService service = new ServerService(this);

    public ClientHandler(Socket socket) {
        this.clientSocket = socket;
        objectMapper.activateDefaultTyping(LaissezFaireSubTypeValidator.instance,
            ObjectMapper.DefaultTyping.NON_FINAL, JsonTypeInfo.As.PROPERTY);
        objectMapper.addMixIn(Sprite.class, SpriteMixIn.class);
        objectMapper.addMixIn(Texture.class, SpriteMixIn.class);
        objectMapper.addMixIn(TextureRegion.class, SpriteMixIn.class);
    }

    @Override
    public void run() {
        try {
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            out = new PrintWriter(clientSocket.getOutputStream(), true);
            String message;
            while ((message = in.readLine()) != null) {
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
                            gameServer.getClients().add(new Entry<>(new ServerPlayer(username), this));
                        }
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
                            Map<String, Object> msg = Map.of(
                                "action", "terminate_game",
                                "id", "!server!",
                                "body", Map.of()
                            );
                            gameServer.sendAll(GSON.toJson(msg));
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

    private int getRandomNumber(int start, int end) {
        Random random = new Random();
        return random.nextInt(end - start + 1) + start;
    }

    public void send(String message) {
        out.println(message);
    }
}
