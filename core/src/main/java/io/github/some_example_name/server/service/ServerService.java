package io.github.some_example_name.server.service;

import io.github.some_example_name.server.ClientHandler;
import io.github.some_example_name.server.model.Message;

import java.util.Comparator;

public class ServerService {
    private final ClientHandler clientHandler;

    public ServerService(ClientHandler clientHandler) {
        this.clientHandler = clientHandler;
    }

    public void DCReconnect(String username) {
        clientHandler.getGameServer().getServerToClientsMessages().stream()
            .sorted(Comparator.comparingLong(Message::getTime))
            .forEach(message -> clientHandler.send(message.getMessage()));
    }
}
