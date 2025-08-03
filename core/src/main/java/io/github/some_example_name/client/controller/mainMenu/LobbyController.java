package io.github.some_example_name.client.controller.mainMenu;

import io.github.some_example_name.client.GameClient;
import io.github.some_example_name.client.view.mainMenu.LobbyMenu;
import io.github.some_example_name.common.model.Lobby;
import io.github.some_example_name.common.model.User;
import io.github.some_example_name.common.model.records.Response;
import io.github.some_example_name.common.utils.App;
import io.github.some_example_name.common.variables.Session;

public class LobbyController {
    private final LobbyMenu view;


    public LobbyController(LobbyMenu view) {
        this.view = view;
    }

    public Response createLobby(User admin, String lobbyName, boolean isPrivate, String password, boolean isVisible) {
        if (lobbyName.isEmpty()) {
            return new Response("you should fill all fields");
        } else {
            if (isPrivate && password.isEmpty()) {
                return new Response("you should fill all fields");
            }
        }
        Lobby lobby = new Lobby(admin.getUsername(), lobbyName, !isPrivate, password, isVisible);
        App.getInstance().getLobbies().add(lobby);
        GameClient.getInstance().createLobbyMessage(lobbyName, isPrivate, password, isVisible, lobby.getId());
        return new Response("", true);
    }

    public Response joinLobby(Lobby lobby, String member, String password) {
        if (password.isEmpty()) {
            return new Response("you should fill all fields");
        }
        if (!lobby.getPassword().equals(password)) {
            return new Response("password is wrong");
        }
        if (lobby.getMembers().size() <= 3 && !lobby.getMembers().contains(member)) {
            lobby.getMembers().add(member);
            GameClient.getInstance().joinLobbyMessage(lobby.getId());
            return new Response("", true);
        }
        return new Response("lobby is full or you already joined");
    }
}
