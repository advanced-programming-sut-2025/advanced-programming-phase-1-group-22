package io.github.some_example_name.client;

import com.badlogic.gdx.Gdx;
import com.google.gson.*;
import io.github.some_example_name.client.controller.mainMenu.StartGameMenuController;
import io.github.some_example_name.common.model.Farm;
import io.github.some_example_name.common.model.Game;
import io.github.some_example_name.common.model.User;
import io.github.some_example_name.common.model.enums.Gender;
import io.github.some_example_name.common.model.enums.SecurityQuestion;
import io.github.some_example_name.common.utils.App;
import io.github.some_example_name.common.variables.Session;
import io.github.some_example_name.server.service.GameService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Map;
import java.util.Random;

public class GameClient {
    private static final String SERVER_ADDRESS = "localhost";
    private static final int SERVER_PORT = 5000;
    private static final Gson GSON = new Gson();
    private static GameClient instance;
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;

    public static GameClient getInstance() {
        if (instance == null) {instance = new GameClient();}
        return instance;
    }

    public void connectToServer() {
        try {
            socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            Map<String, Object> msg = Map.of(
                "action", "connected",
//                "id", Session.getCurrentUser().getUsername(),
                "body", Map.of()
            );

            out.println(GSON.toJson(msg));
            startListening();
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println(e.getMessage());
        }
    }

    public void readyForGame() {
        try {
            out = new PrintWriter(socket.getOutputStream(), true);
            in  = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            Map<String, Object> msg = Map.of(
                "action", "ready_for_game",
                "id", Session.getCurrentUser().getUsername(),
                "body", Map.of()
            );

            out.println(GSON.toJson(msg));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void startListening() {
        new Thread(() -> {
            try {
                String serverMessage;
                while ((serverMessage = in.readLine()) != null) {
                    try {
                        JsonObject obj = JsonParser.parseString(serverMessage).getAsJsonObject();

                        if (obj.get("action").getAsString().equals("init_game")) {
                            JsonObject bodyArray = obj.getAsJsonObject("body");
                            App.getInstance().getCurrentGame().getVillage().addStoresAndNpcs(bodyArray.getAsJsonArray("stores"));
                            StartGameMenuController.getInstance().startGame(
                                bodyArray.getAsJsonArray("players"),
                                bodyArray.getAsJsonArray("farms"),
                                bodyArray.getAsJsonArray("characters")
                            );
                            for (int i = 0; i < App.getInstance().getCurrentGame().getVillage().getFarms().size(); i++) {
                                Farm farm = App.getInstance().getCurrentGame().getVillage().getFarms().get(i);
                                farm.setDoor(
                                    bodyArray.getAsJsonArray("doors").get(2 * i).getAsInt(),
                                    bodyArray.getAsJsonArray("doors").get(2 * i + 1).getAsInt()
                                );
                            }
                        } else if (obj.get("action").getAsString().equals("response_choose_farm")) {
                            StartGameMenuController.getInstance().responseToChooseFarm(
                                obj.getAsJsonObject("body").get("response").getAsString()
                            );
                        } else if (obj.get("action").getAsString().equals("skip_time")) {
                            Gdx.app.postRunnable(() -> GameService.getInstance().skipTimeByServer(
                                obj.getAsJsonObject("body").get("minutes").getAsInt()
                            ));
                        } else if (obj.get("action").getAsString().equals("ready_for_sleep")) {
                            Gdx.app.postRunnable(() -> App.getInstance().getCurrentGame().startDayEvents());
                        }
                    } catch (JsonParseException e) {
                        System.out.println("Received non-JSON: " + serverMessage);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    public void sendGameStateToServer(String gameState) {
        out.println(gameState);
    }

    public void enterRoom(int id) {
        try {
            out = new PrintWriter(socket.getOutputStream(), true);
            in  = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            Map<String, Object> msg = Map.of(
                "action", "enter_room",
                "id", Session.getCurrentUser().getUsername(),
                "body", Map.of("id", id)
            );

            out.println(GSON.toJson(msg));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void chooseFarm(int farmId, String character) {
        try {
            out = new PrintWriter(socket.getOutputStream(), true);
            in  = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            Map<String, Object> msg = Map.of(
                "action", "choose_farm",
                "id", Session.getCurrentUser().getUsername(),
                "body", Map.of("farmId", farmId, "character", character)
            );

            out.println(GSON.toJson(msg));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void skipTime(int minutes) {
        try {
            out = new PrintWriter(socket.getOutputStream(), true);
            in  = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            Map<String, Object> msg = Map.of(
                "action", "skip_time",
                "id", Session.getCurrentUser().getUsername(),
                "body", Map.of("minutes", minutes)
            );

            out.println(GSON.toJson(msg));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void readyForSleep() {
        try {
            out = new PrintWriter(socket.getOutputStream(), true);
            in  = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            Map<String, Object> msg = Map.of(
                "action", "ready_for_sleep",
                "id", Session.getCurrentUser().getUsername(),
                "body", Map.of()
            );

            out.println(GSON.toJson(msg));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

