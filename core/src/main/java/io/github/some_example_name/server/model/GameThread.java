package io.github.some_example_name.server.model;

import io.github.some_example_name.server.ClientHandler;
import lombok.Getter;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

@Getter
public class GameThread extends Thread {
    private static GameThread instance;
    private static final int PORT = 5000;
    private final HashMap<Integer, GameServer> games = new HashMap<>();

    private GameThread() {
    }

    public static GameThread getInstance() {
        if (instance == null) {
            instance = new GameThread();
        }
        return instance;
    }

    public GameServer getGameServer(Integer id) {
        if (!games.containsKey(id)) {
            games.put(id, new GameServer());
        }
        return games.get(id);
    }

    public void addGameServer(Integer id, GameServer gameServer) {
        games.put(id, gameServer);
    }

    public boolean haveGame(Integer id) {
        return games.containsKey(id);
    }

    @Override
    public void start() {
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
}
