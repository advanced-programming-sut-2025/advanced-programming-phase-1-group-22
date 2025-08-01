package io.github.some_example_name.client.view.mainMenu;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import io.github.some_example_name.client.GameClient;
import io.github.some_example_name.client.MainGradle;
import io.github.some_example_name.client.controller.WorldController;
import io.github.some_example_name.common.model.Salable;
import io.github.some_example_name.common.utils.App;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;

public class RadioMenu extends PopUp {
    private Table musics;
    private File AUDIO_DIR;
    private Music currentMusic = null;
    private boolean isMuted = false;
    private String currentMusicName;
    private Runnable updateMusics;

    @Override
    public void createMenu(Stage stage, Skin skin, WorldController playerController) {
        super.createMenu(stage, skin, playerController);
        AUDIO_DIR = new File("user_audio/" + App.getInstance().getCurrentGame().getCurrentPlayer().getUser().getUsername() + "/");

        if (!AUDIO_DIR.exists()) {
            boolean success = AUDIO_DIR.mkdirs();
            if (!success) {
                close();
                return;
            }
        }
        OrthographicCamera camera = MainGradle.getInstance().getCamera();
        Window window = new Window("Radio Menu", skin);
        window.setSize(camera.viewportWidth * 0.9f, camera.viewportHeight * 0.6f);
        window.setPosition(
            (MainGradle.getInstance().getCamera().viewportWidth - window.getWidth()) / 2f,
            (MainGradle.getInstance().getCamera().viewportHeight - window.getHeight()) / 2f
        );
        window.setMovable(false);

        Table content = new Table(skin);
        content.setFillParent(true);
        content.defaults().pad(10);

        Table buttonContainer = new Table(skin);
        buttonContainer.defaults().pad(8).fillX();

        TextButton addMusic = new TextButton("Add", skin);
        addMusic.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                new Thread(() -> {
                    File selectedFile = openFileChooser();
                    if (selectedFile != null) {
                        Gdx.app.postRunnable(() -> {
                            String filename = selectedFile.getName();
                            String displayName = filename.substring(filename.lastIndexOf("/") + 1);
                            addMusicRow(displayName, skin);
                        });
                    }
                }).start();
            }
        });
        TextButton mute = new TextButton("Mute", skin);
        mute.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                isMuted = !isMuted;
                handleMuteMusic();
                updateMusics.run();
            }
        });
        TextButton connectToOther = new TextButton("📡Connect to other", skin);
        connectToOther.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                createConnectDialog();
            }
        });
        TextButton disconnect = new TextButton("Disconnect", skin);
        disconnect.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (App.getInstance().getCurrentGame().getCurrentPlayer().getAudioReceiver() != null &&
                    App.getInstance().getCurrentGame().getCurrentPlayer().getAudioReceiver().isAlive()) {
                    App.getInstance().getCurrentGame().getCurrentPlayer().getAudioReceiver().stopReceiver();
                }
            }
        });

        buttonContainer.add(addMusic).row();
        buttonContainer.add(mute).row();
        buttonContainer.add(connectToOther).row();
        buttonContainer.add(disconnect).row();

        musics = new Table(skin);
        musics.defaults().pad(5);

        refreshMusicList();
        this.updateMusics = this::refreshMusicList;

        ScrollPane scrollPane = new ScrollPane(musics, skin);
        scrollPane.setFadeScrollBars(false);
        scrollPane.setScrollingDisabled(true, false);
        scrollPane.setScrollbarsOnTop(true);

        content.add(buttonContainer).width(180).top().left().padRight(30).padTop(50).padLeft(50);
        content.add(scrollPane).padLeft(60).expand().fill();

        window.add(content).expand().fill().pad(10);

        ArrayList<Actor> array = new ArrayList<>();
        array.add(window);
        ImageButton exitButton = provideExitButton(array);

        Group group = new Group() {
            @Override
            public void act(float delta) {
                window.setPosition(
                    (MainGradle.getInstance().getCamera().viewportWidth - window.getWidth()) / 2f +
                        MainGradle.getInstance().getCamera().position.x - MainGradle.getInstance().getCamera().viewportWidth / 2,
                    (MainGradle.getInstance().getCamera().viewportHeight - window.getHeight()) / 2f +
                        MainGradle.getInstance().getCamera().position.y - MainGradle.getInstance().getCamera().viewportHeight / 2
                );
                exitButton.setPosition(
                    window.getX() + window.getWidth() - exitButton.getWidth() / 2f + 16,
                    window.getY() + window.getHeight() - exitButton.getHeight() / 2f
                );
                super.act(delta);
            }
        };
        group.addActor(window);
        group.addActor(exitButton);
        getMenuGroup().addActor(group);
    }

    private void createConnectDialog() {
        Dialog dialog = new Dialog("Connect to Radio", skin);
        dialog.setModal(true);
        dialog.setMovable(false);
        dialog.setResizable(false);

        Table content = new Table(skin);

        Label label = new Label("Enter username:", skin);
        final TextField usernameField = new TextField("", skin);

        TextButton connectButton = new TextButton("Connect", skin);
        TextButton cancelButton = new TextButton("Cancel", skin);

        connectButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                String username = usernameField.getText().trim();
                if (!username.isEmpty()) {
                    dialog.hide();
                    GameClient.getInstance().updateRadioConnection(username);
                }
            }
        });

        cancelButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                dialog.hide();
            }
        });

        content.add(label).padBottom(10).left().row();
        content.add(usernameField).width(250).row();
        content.add(connectButton).padTop(20).left().padRight(10);
        content.add(cancelButton).padTop(20).left();

        dialog.getContentTable().add(content).pad(20);
        dialog.show(stage);
    }

    private File openFileChooser() {
        if (!Gdx.app.getType().equals(Application.ApplicationType.Desktop)) {
            return null;
        }

        FileDialog dialog = new FileDialog((java.awt.Frame) null, "Select Audio File");
        dialog.setMode(FileDialog.LOAD);
        dialog.setFilenameFilter((dir, name) ->
            name.endsWith(".wav")
        );
        dialog.setVisible(true);

        if (dialog.getFile() != null) {
            File selectedFile = new File(dialog.getDirectory(), dialog.getFile());
            File destFile = new File(AUDIO_DIR, selectedFile.getName());

            try {
                Files.copy(selectedFile.toPath(), destFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                return destFile;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    private void addMusicRow(String filename, Skin skin) {
        Table row = new Table();
        row.left();

        Label label = new Label("🎵 " + filename, skin);

        if (currentMusic != null && currentMusic.isPlaying() && currentMusicName.equals(filename)) {
            if (isMuted) {
                label.setText(label.getText() + "-> Playing... (Muted)");
            } else {
                label.setText(label.getText() + "-> Playing...");
            }
        }

        Table buttonRow = new Table();

        TextButton play = new TextButton("Play", skin);
        TextButton stop = new TextButton("Stop", skin);
        TextButton remove = new TextButton("Remove", skin);

        play.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                playMusic(filename);
                updateMusics.run();
            }
        });

        stop.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                stopMusic();
                updateMusics.run();
            }
        });

        remove.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                File file = new File(AUDIO_DIR, filename);
                if (file.exists() && file.delete()) {
                    updateMusics.run();
                }
            }
        });


        buttonRow.add(play).padRight(5);
        buttonRow.add(stop).padRight(5);
        buttonRow.add(remove).padRight(5);

        row.add(label).left().padBottom(5).padLeft(10).row();
        row.add(buttonRow).left().padBottom(10).padLeft(10).row();

        musics.add(row).left().expandX().fillX().row();
    }

    private void refreshMusicList() {
        musics.clear();

        File[] files = AUDIO_DIR.listFiles((dir, name) ->
            name.endsWith(".mp3") || name.endsWith(".wav") || name.endsWith(".ogg")
        );

        if (files != null) {
            for (File file : files) {
                String filename = file.getName();
                String displayName = filename.substring(filename.lastIndexOf("/") + 1);
                addMusicRow(displayName, skin);
            }
        }
    }

    private void playMusic(String filename) {
        try {
            if (currentMusic != null) {
                return;
            }
            if (App.getInstance().getCurrentGame().getCurrentPlayer().getAudioReceiver() != null &&
                App.getInstance().getCurrentGame().getCurrentPlayer().getAudioReceiver().isAlive()) {
                App.getInstance().getCurrentGame().getCurrentPlayer().getAudioReceiver().stopReceiver();
            }
            File file = new File(AUDIO_DIR, filename);
            currentMusic = Gdx.audio.newMusic(Gdx.files.absolute(file.getAbsolutePath()));
            currentMusic.play();
            currentMusicName = filename;
            App.getInstance().getCurrentGame().getCurrentPlayer().getAudioStreamer().changeTrack(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void stopMusic() {
        if (currentMusic != null) {
            currentMusic.stop();
            currentMusic.dispose();
            currentMusic = null;
            App.getInstance().getCurrentGame().getCurrentPlayer().getAudioStreamer().stopTrack();
        }
    }

    private void handleMuteMusic() {
        try {
            if (currentMusic != null) {
                currentMusic.setVolume(isMuted ? 0f : 1f);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void handleDragRelease(InputEvent event, float x, float y, int pointer, Image itemImage, Salable item, Image dragImage, Boolean flag) {
    }
}
