package io.github.some_example_name.server.service;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.github.some_example_name.server.ClientHandler;
import io.github.some_example_name.server.model.Message;

import java.util.Comparator;

public class ServerService {
    private final ClientHandler clientHandler;

    public ServerService(ClientHandler clientHandler) {
        this.clientHandler = clientHandler;
    }

    public void DCReconnect() {
        clientHandler.getGameServer().getServerToClientsMessages().stream()
            .sorted(Comparator.comparingLong(Message::getTime))
            .forEach(message -> {
                try {
                    JsonObject obj = JsonParser.parseString(message.getMessage()).getAsJsonObject();
                    if (!(obj.get("action").getAsString().equals("DC_player") ||
                        obj.get("action").getAsString().equals("DC_termination") ||
                    obj.get("action").getAsString().equals("update_player_connection") ||
                    obj.get("action").getAsString().equals("finish_reconnect"))){
                        clientHandler.send(message.getMessage());
                    }
                } catch (Exception ignored) {
                }
            });
    }
}
