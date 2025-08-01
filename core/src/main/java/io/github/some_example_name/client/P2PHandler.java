package io.github.some_example_name.client;

import io.github.some_example_name.common.utils.App;

import java.net.Socket;

public class P2PHandler extends Thread {
    private final Socket socket;

    public P2PHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        if (App.getInstance().getCurrentGame().getCurrentPlayer().getAudioStreamer() != null)
            App.getInstance().getCurrentGame().getCurrentPlayer().getAudioStreamer().addListener(socket);
    }
}
