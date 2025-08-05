package io.github.some_example_name.server.model;

import io.github.some_example_name.common.model.Lobby;
import io.github.some_example_name.common.model.Tuple;
import io.github.some_example_name.common.utils.App;
import io.github.some_example_name.server.ClientHandler;
import io.github.some_example_name.server.saveGame.GameSaver;
import lombok.Getter;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
public class GameThread extends Thread {
    private static GameThread instance;
    private static final int PORT = 5000;
    private final HashMap<Integer, GameServer> games = new HashMap<>();
    private final Map<String, ClientHandler> connections = Collections.synchronizedMap(new HashMap<>());
    private final Map<String, Long> lastConnections = Collections.synchronizedMap(new HashMap<>());
    private final Map<String, List<String>> users = new HashMap<>();
    private final Map<String, Long> readyPlayersForLoad = new HashMap<>();
    private final Map<String, Tuple<String>> autoLogins = new HashMap<>();

    private GameThread() {
        loadSavedLobbies();
        for (Lobby lobby : App.getInstance().getLobbies()) {
            lobby.setLastTimeNoPlayer(System.currentTimeMillis());
        }
    }

    public static GameThread getInstance() {
        if (instance == null) {
            instance = new GameThread();
        }
        return instance;
    }

    public GameServer getGameServerForStart(long id) {
        synchronized (App.getInstance().getLobbies()) {
            for (Lobby lobby : App.getInstance().getLobbies()) {
                if (lobby.getId() == id) {
                    if (lobby.getGameServer() == null) {
                        lobby.setGameServer(new GameServer());
                    } else if (lobby.isGameServerSaved()) {
                        lobby.setGameServer(new GameServer());
                        lobby.setGameServerSaved(false);
                    }
                    return lobby.getGameServer();
                }
            }
            return null;
        }
    }

    @Override
    public void run() {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client connected: " + clientSocket.getInetAddress());
                ClientHandler clientThread = new ClientHandler(clientSocket);
                clientThread.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadSavedLobbies() {
        try {
            List<Lobby> lobbies = GameSaver.loadLobbies("games.json");
            if (lobbies != null) {
                App.getInstance().getLobbies().addAll(lobbies);
            }
        } catch (IOException ignored) {
        }
    }

    public void sendAll(String message) {
        synchronized (connections) {
            for (Map.Entry<String, ClientHandler> stringClientHandlerEntry : connections.entrySet()) {
                try {
                    stringClientHandlerEntry.getValue().send(message);
                } catch (IOException ignored) {
                }
            }
        }
    }

    public void sendAllBut(String message, String username) {
        synchronized (connections) {
            for (Map.Entry<String, ClientHandler> stringClientHandlerEntry : connections.entrySet()) {
                if (!stringClientHandlerEntry.getKey().equals(username)) {
                    try {
                        stringClientHandlerEntry.getValue().send(message);
                    } catch (IOException ignored) {
                    }
                }
            }
        }
    }

    public void sentTo(String message, String username) {
        synchronized (connections) {
            for (Map.Entry<String, ClientHandler> stringClientHandlerEntry : connections.entrySet()) {
                if (stringClientHandlerEntry.getKey().equals(username)) {
                    try {
                        stringClientHandlerEntry.getValue().send(message);
                    } catch (IOException ignored) {
                    }
                }
            }
        }
    }
}
