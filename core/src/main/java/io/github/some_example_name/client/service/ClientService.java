package io.github.some_example_name.client.service;

import io.github.some_example_name.common.model.relations.Player;
import io.github.some_example_name.common.utils.App;

public class ClientService {

    public void handleUpdatePosition(String username, int positionX, int positionY) {
        Player player = getPlayerByUsername(username);
        if (player == null) return;
        player.getTiles().clear();
        player.getTiles().add(App.getInstance().getCurrentGame().tiles[positionX][positionY]);
    }

    private Player getPlayerByUsername(String username) {
        for (Player player : App.getInstance().getCurrentGame().getPlayers()) {
            if (player.getUser().getUsername().equals(username)) {
                return player;
            }
        }
        return null;
    }
}
