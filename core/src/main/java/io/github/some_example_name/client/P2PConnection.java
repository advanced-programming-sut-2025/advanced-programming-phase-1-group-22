package io.github.some_example_name.client;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class P2PConnection extends Thread {
    private final int PORT;

    public P2PConnection(int PORT) {
        this.PORT = PORT;
    }

    @Override
    public void run() {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client connected: " + clientSocket.getInetAddress());
                P2PHandler p2PHandler = new P2PHandler(clientSocket);
                p2PHandler.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
