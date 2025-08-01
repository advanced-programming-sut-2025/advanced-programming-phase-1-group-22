package io.github.some_example_name.client;

import io.github.some_example_name.client.controller.AudioReceiver;
import io.github.some_example_name.common.utils.App;

import java.io.IOException;
import java.net.Socket;

public class P2PReceiving extends Thread {
    private Socket socket;

    public P2PReceiving(int PORT) {
        try {
            this.socket = new Socket("localhost", PORT);
        } catch (IOException ignored) {
        }
    }

    @Override
    public void run() {
        if (App.getInstance().getCurrentGame().getCurrentPlayer().getAudioReceiver() != null &&
            App.getInstance().getCurrentGame().getCurrentPlayer().getAudioReceiver().isAlive()) {
            App.getInstance().getCurrentGame().getCurrentPlayer().getAudioReceiver().stopReceiver();
        }
        App.getInstance().getCurrentGame().getCurrentPlayer().setAudioReceiver(new AudioReceiver(socket));
        App.getInstance().getCurrentGame().getCurrentPlayer().getAudioReceiver().start();
    }
}
