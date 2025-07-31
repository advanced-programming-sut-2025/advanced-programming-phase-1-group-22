package io.github.some_example_name.server.model;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.github.some_example_name.client.service.ClientService;
import io.github.some_example_name.common.model.Entry;
import io.github.some_example_name.server.ClientHandler;
import lombok.Getter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Getter
public class GameServer {
    private static final Gson GSON = new GsonBuilder().serializeNulls().create();
    private final ArrayList<Entry<ServerPlayer, ClientHandler>> clients = new ArrayList<>();

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

    public void sendAll(String message) {
        clients.forEach(client -> {
            client.getValue().send(message);
        });
    }

    public void sendAllBut(String message, String username) {
        for (Entry<ServerPlayer, ClientHandler> client : clients) {
            if (!client.getKey().username.equals(username)) client.getValue().send(message);
        }
    }

    public void sendFire(String message, String username) {
        for (Entry<ServerPlayer, ClientHandler> client : clients) {
            if (!client.getKey().username.equals(username))  {
                client.getValue().send(message);
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
}
