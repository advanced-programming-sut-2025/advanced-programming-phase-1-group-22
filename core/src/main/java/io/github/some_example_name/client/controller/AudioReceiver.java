package io.github.some_example_name.client.controller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.AudioDevice;
import io.github.some_example_name.common.utils.App;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

public class AudioReceiver extends Thread {
    private final Socket socket;
    private volatile boolean running = true;
    private AudioDevice device;

    public AudioReceiver(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            device = Gdx.audio.newAudioDevice(44100, false);
            InputStream input = socket.getInputStream();

            byte[] buffer = new byte[4096];
            int bytesRead;

            while (running && (bytesRead = input.read(buffer)) != -1) {
                short[] samples = new short[bytesRead / 2];
                for (int i = 0; i < samples.length; i++) {
                    int low = buffer[i * 2] & 0xff;
                    int high = buffer[i * 2 + 1];
                    samples[i] = (short) ((high << 8) | low);
                }

                device.writeSamples(samples, 0, samples.length);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            cleanup();
            try {
                socket.close();
            } catch (IOException ignored) {
            }
        }
    }

    public void stopReceiver() {
        running = false;
        cleanup();
        try {
            socket.shutdownInput();
        } catch (IOException ignored) {
        }
        try {
            socket.close();
        } catch (IOException ignored) {
        }
    }

    private void cleanup() {
        if (device != null) {
            device.dispose();
        }
        App.getInstance().getCurrentGame().getCurrentPlayer().setReceiving(false);
    }
}
