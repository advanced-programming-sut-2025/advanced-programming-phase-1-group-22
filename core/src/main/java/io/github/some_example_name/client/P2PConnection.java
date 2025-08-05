package io.github.some_example_name.client;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class P2PConnection extends Thread {
    private final int PORT;
    private boolean running = true;
    private ServerSocket serverSocket;

    public P2PConnection(int PORT) {
        this.PORT = PORT;
    }

    @Override
    public void run() {
        try {
            serverSocket = new ServerSocket(PORT);
            while (running) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client connected: " + clientSocket.getInetAddress());
                P2PHandler p2PHandler = new P2PHandler(clientSocket);
                p2PHandler.start();
            }
        } catch (IOException e) {
            if (running) e.printStackTrace();
        } finally {
            try {
                if (serverSocket != null && !serverSocket.isClosed()) {
                    serverSocket.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void stopThread() {
        running = false;
        try {
            if (serverSocket != null && !serverSocket.isClosed()) {
                serverSocket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
