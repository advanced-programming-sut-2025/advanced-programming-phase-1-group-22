package io.github.some_example_name.server.service;

import com.google.gson.Gson;
import io.github.some_example_name.server.ClientHandler;

public class ServerService {
    private final ClientHandler clientHandler;
    private final Gson GSON = new Gson();

    public ServerService(ClientHandler clientHandler) {
        this.clientHandler = clientHandler;
    }
}
