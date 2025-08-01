package io.github.some_example_name.client.controller;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class AudioStreamer extends Thread {
    private final List<Socket> listeners = new CopyOnWriteArrayList<>();
    private volatile boolean running = true;
    private volatile File currentTrack = null;
    private volatile boolean playing = false;
    private final Object lock = new Object();

    public void addListener(Socket socket) {
        listeners.add(socket);
    }

    public synchronized void changeTrack(File newTrack) {
        synchronized (lock) {
            currentTrack = newTrack;
            playing = true;
        }
    }

    public synchronized void stopTrack() {
        playing = false;
        currentTrack = null;
    }

    public void stopStreamer() {
        running = false;
        interrupt();
    }

    @Override
    public void run() {
        while (running) {
            if (playing && currentTrack != null) {
                streamTrack(currentTrack);
                synchronized (this) {
                    playing = false;
                    currentTrack = null;
                }
            } else {
                try {
                    Thread.sleep(200);
                } catch (InterruptedException ignored) {
                }
            }
        }
    }

    private void streamTrack(File file) {
        try (AudioInputStream originalStream = AudioSystem.getAudioInputStream(file)) {
            AudioFormat targetFormat = new AudioFormat(
                AudioFormat.Encoding.PCM_SIGNED,
                44100,
                16,
                2,
                4,
                44100,
                false
            );

            try (AudioInputStream pcmStream = AudioSystem.getAudioInputStream(targetFormat, originalStream)) {
                byte[] buffer = new byte[4096];
                int bytesRead;

                while (playing && (bytesRead = pcmStream.read(buffer)) != -1) {
                    for (Socket socket : listeners) {
                        try {
                            OutputStream out = socket.getOutputStream();
                            out.write(buffer, 0, bytesRead);
                            out.flush();
                        } catch (IOException e) {
                            listeners.remove(socket);
                        }
                    }
                    Thread.sleep(20);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

