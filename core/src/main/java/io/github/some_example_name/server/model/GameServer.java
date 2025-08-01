package io.github.some_example_name.server.model;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.github.some_example_name.client.service.ClientService;
import io.github.some_example_name.common.model.Entry;
import io.github.some_example_name.common.model.enums.Weather;
import io.github.some_example_name.server.ClientHandler;
import lombok.Getter;
import lombok.Setter;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Getter
@Setter
public class GameServer {
    private static final Gson GSON = new GsonBuilder().serializeNulls().create();
    private final ArrayList<Entry<ServerPlayer, ClientHandler>> clients = new ArrayList<>();
    private final Map<String, Long> DCPlayers = Collections.synchronizedMap(new HashMap<>());
    private final Map<String, Long> playerLastPing = Collections.synchronizedMap(new HashMap<>());
    private final List<Message> serverToClientsMessages = new ArrayList<>();
    private StringBuilder dayEvents = new StringBuilder();
    private HashMap<String, NpcGift> npcGifts = new HashMap<>();
    private String tomorrowWeather = "sunny";

    public boolean isReady() {
        for (Entry<ServerPlayer, ClientHandler> entry : clients) {
            if (!entry.getValue().isDead() && !entry.getValue().isReady()) return false;
        }
        if (clients.size() > 1) {
            for (Entry<ServerPlayer, ClientHandler> entry : clients) {
                entry.getValue().setReady(false);
            }
            return true;
        }
        return false;
    }

    public void addGift(String npc, String gift, String player) {
        if (!npcGifts.containsKey(npc)) {
            npcGifts.put(npc, new NpcGift());
        }
        npcGifts.get(npc).addGift(player, gift);
    }

    public void sendAll(String message) {
        serverToClientsMessages.add(new Message(message));
        clients.forEach(client -> {
            try {
                client.getValue().send(message);
            } catch (Exception ignored) {
            }
        });
    }

    public void sendAllBut(String message, String username) {
        serverToClientsMessages.add(new Message(message));
        for (Entry<ServerPlayer, ClientHandler> client : clients) {
            if (!client.getKey().username.equals(username)) {
                try {
                    client.getValue().send(message);
                } catch (Exception ignored) {
                }
            }
        }
    }

    public void sendFire(String message, String username) {
        for (Entry<ServerPlayer, ClientHandler> client : clients) {
            if (!client.getKey().username.equals(username)) {
                try {
                    client.getValue().send(message);
                } catch (Exception ignored) {
                }
            } else {
                client.getValue().setInFavor(true);
            }
        }
    }

    public String checkAvailability(String username, Integer farmId, String character) {
        ServerPlayer currentPlayer = null;
        for (Entry<ServerPlayer, ClientHandler> entry : clients) {
            ServerPlayer serverPlayer = entry.getKey();
            if (serverPlayer.getUsername().equals(username)) {
                currentPlayer = serverPlayer;
            } else {
                if (farmId.equals(serverPlayer.getFarmId())) return "Farm not available";
                if (character.equals(serverPlayer.getCharacter())) return "Character not available";
            }
        }
        if (currentPlayer == null) return "Something went wrong";
        currentPlayer.setFarmId(farmId);
        currentPlayer.setCharacter(character);
        return "Good!";
    }

    public void clearFavors() {
        for (Entry<ServerPlayer, ClientHandler> client : clients) {
            client.getValue().setInFavor(false);
        }
    }

    public boolean isMajority() {
        for (Entry<ServerPlayer, ClientHandler> client : clients) {
            if (!client.getValue().isDead() && !client.getValue().isInFavor()) return false;
        }
        return true;
    }

    public ClientHandler findClient(String player) {
        for (Entry<ServerPlayer, ClientHandler> client : clients) {
            if (client.getKey().username.equals(player)) return client.getValue();
        }
        return null;
    }

    public void terminate() {
        Map<String, Object> msg = Map.of(
            "action", "terminate_game",
            "id", "!server!",
            "body", Map.of()
        );
        sendAll(GSON.toJson(msg));
        for (Entry<ServerPlayer, ClientHandler> client : clients) {
            client.getValue().setRunning(false);
        }
        clients.clear();
        for (Map.Entry<Integer, GameServer> entry : GameThread.getInstance().getGames().entrySet()) {
            if (entry.getValue() == this) {
                GameThread.getInstance().getGames().remove(entry.getKey());
                break;
            }
        }
    }

    public void addDialog(String npc, String events) {
        StringBuilder eventsBuilder = new StringBuilder(events);
        if (npcGifts.get(npc) != null) {
            for (Map.Entry<String, ArrayList<String>> entry : npcGifts.get(npc).getGifts().entrySet()) {
                eventsBuilder.append("\n").append(entry.getKey())
                    .append(" has given me very lovely gifts such as ");
                for (String string : entry.getValue()) {
                    eventsBuilder.append(string).append(", ");
                }
                eventsBuilder.append(".");
            }
        }
        eventsBuilder.append("\n");
        eventsBuilder.append(getDayEvents());

        String finalEvent = eventsBuilder.toString();

        new Thread(() -> {
            String model = "mistralai/mistral-7b-instruct";
            String iAm = "Talk as you want to talk to an friend but very short and sound";
            String requestBody = """
               {
                  "model": "%s",
                  "messages": [
                      {"role": "system", "content": "%s"},
                      {"role": "user", "content": "%s"}
                  ]
               }
               """.formatted(model, finalEvent, iAm);
            try {
                InputStream responseStream = getInputStream(requestBody);
                BufferedReader reader = new BufferedReader(new InputStreamReader(responseStream));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line.trim());
                }
                JsonObject obj = JsonParser.parseString(response.toString()).getAsJsonObject();
                String resp = obj.getAsJsonArray("choices").get(0).getAsJsonObject()
                    .getAsJsonObject("message").get("content").getAsString();
                Map<String, Object> msg = Map.of(
                    "action", "add_dialog",
                    "id", "!server!",
                    "body", Map.of("npc", npc, "response", resp)
                );
                sendAll(GSON.toJson(msg));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }).start();
    }
    private static InputStream getInputStream(String requestBody) throws IOException {
        String API_URL = "https://openrouter.ai/api/v1/chat/completions";
        String API_KEY = "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";
        HttpURLConnection connection = (HttpURLConnection) new URL(API_URL).openConnection();
        connection.setRequestMethod("POST");
        connection.setDoOutput(true);
        connection.setRequestProperty("Authorization", "Bearer " + API_KEY);
        connection.setRequestProperty("Content-Type", "application/json");

        try (OutputStream os = connection.getOutputStream()) {
            byte[] input = requestBody.getBytes(StandardCharsets.UTF_8);
            os.write(input);
        }

        int responseCode = connection.getResponseCode();
        return responseCode < 400
            ? connection.getInputStream()
            : connection.getErrorStream();
    }
}
