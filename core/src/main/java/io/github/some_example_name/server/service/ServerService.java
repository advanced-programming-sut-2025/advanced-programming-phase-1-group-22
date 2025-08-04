package io.github.some_example_name.server.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.github.some_example_name.common.model.Lobby;
import io.github.some_example_name.common.utils.App;
import io.github.some_example_name.common.variables.Session;
import io.github.some_example_name.server.ClientHandler;
import io.github.some_example_name.server.model.GameThread;
import io.github.some_example_name.server.model.Message;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class ServerService {
    private final ClientHandler clientHandler;
    private final Gson GSON = new GsonBuilder().serializeNulls().create();

    public ServerService(ClientHandler clientHandler) {
        this.clientHandler = clientHandler;
    }

    public void DCReconnect() {
        clientHandler.getGameServer().getServerToClientsMessages().stream()
            .sorted(Comparator.comparingLong(Message::getTime))
            .forEach(message -> {
                try {
                    JsonObject obj = JsonParser.parseString(message.getMessage()).getAsJsonObject();
                    if (!(obj.get("action").getAsString().equals("DC_player") ||
                        obj.get("action").getAsString().equals("DC_termination") ||
                        obj.get("action").getAsString().equals("update_player_connection") ||
                        obj.get("action").getAsString().equals("finish_reconnect") ||
                        obj.get("action").getAsString().equals("save_game"))) {
                        clientHandler.send(message.getMessage());
                    }
                } catch (Exception ignored) {
                }
            });
    }

    public void createLobby(String admin, String lobbyName, boolean isPrivate, String password, boolean isVisible, long id) {
        Lobby lobby = new Lobby(admin, lobbyName, !isPrivate, password, isVisible);
        lobby.setId(id);
        App.getInstance().getLobbies().add(lobby);
    }

    public void joinLobby(long id, String member) {
        synchronized (App.getInstance().getLobbies()) {
            for (Lobby lobby : App.getInstance().getLobbies()) {
                if (lobby.getId() == id) {
                    lobby.getMembers().add(member);
                }
            }
        }
    }

    public void leftLobby(long id, String member) {
        synchronized (App.getInstance().getLobbies()) {
            Iterator<Lobby> iterator = App.getInstance().getLobbies().iterator();

            while (iterator.hasNext()) {
                Lobby lobby = iterator.next();

                if (lobby.getId() == id) {
                    lobby.getMembers().remove(member);
                    if (lobby.getAdmin().equals(member)) {
                        if (!lobby.getMembers().isEmpty()) {
                            lobby.setAdmin(lobby.getMembers().get(0));
                        } else {
                            iterator.remove();
                        }
                    }
                }
            }
        }
    }

    public void startGame(long id) {
        synchronized (App.getInstance().getLobbies()) {
            for (Lobby lobby : App.getInstance().getLobbies()) {
                if (lobby.getId() == id) {
                    lobby.setGameStart(true);
                }
            }
        }
    }

    public boolean handleReadyForLoad(long id) {
        synchronized (App.getInstance().getLobbies()) {
            for (Lobby lobby : App.getInstance().getLobbies()) {
                if (lobby.getId() == id) {
                    for (String member : lobby.getMembers()) {
                        boolean found = false;
                        for (Map.Entry<String, Long> stringLongEntry : GameThread.getInstance().getReadyPlayersForLoad().entrySet()) {
                            if (stringLongEntry.getValue() == id) {
                                if (stringLongEntry.getKey().equals(member)) found = true;
                            }
                        }
                        if (!found) {
                            return false;
                        }
                    }
                }
                return true;
            }
            return false;
        }
    }

    public void sendReadyMessage(long id) {
        Map<String, Object> msg = new HashMap<>();
        msg.put("action", "load_game");
        msg.put("lobby_id", id);
        for (Map.Entry<String, Long> stringLongEntry : GameThread.getInstance().getReadyPlayersForLoad().entrySet()) {
            if (stringLongEntry.getValue() == id) {
                GameThread.getInstance().sentTo(GSON.toJson(msg), stringLongEntry.getKey());
            }
        }
        Iterator<Map.Entry<String, Long>> iterator = GameThread.getInstance().getReadyPlayersForLoad().entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, Long> entry = iterator.next();
            if (entry.getValue() == id) {
                iterator.remove();
            }
        }
    }
}
