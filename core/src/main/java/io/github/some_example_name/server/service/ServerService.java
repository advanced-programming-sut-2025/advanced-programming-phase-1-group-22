package io.github.some_example_name.server.service;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.github.some_example_name.client.GameClient;
import io.github.some_example_name.common.model.Lobby;
import io.github.some_example_name.common.utils.App;
import io.github.some_example_name.server.ClientHandler;
import io.github.some_example_name.server.model.Message;

import java.util.Comparator;
import java.util.Iterator;

public class ServerService {
    private final ClientHandler clientHandler;

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
                        obj.get("action").getAsString().equals("finish_reconnect"))) {
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
}
