package io.github.some_example_name.server.model;


import io.github.some_example_name.common.model.Entry;
import io.github.some_example_name.server.ClientHandler;
import lombok.Getter;

import java.util.ArrayList;
import java.util.HashMap;

@Getter
public class GameServer {
    private final ArrayList<Entry<ServerPlayer, ClientHandler>> clients = new ArrayList<>();

    public boolean isReady() {
        for (Entry<ServerPlayer, ClientHandler> entry : clients) {
            if (!entry.getValue().isReady()) return false;
        }
        return clients.size() > 1;
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
}
