package io.github.some_example_name.common.model;

import io.github.some_example_name.common.model.relations.Player;
import io.github.some_example_name.server.model.GameServer;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class Lobby {
    private final List<User> members = new ArrayList<>();
    private User admin;
    private final long id;
    private final String name;
    private final boolean _public;
    private final String password;
    private final boolean visible;
    private long lastTimeNoPlayer;
    private GameServer gameServer;

    public Lobby(User admin, String name, boolean _public, String password, boolean visible) {
        this.admin = admin;
        this.name = name;
        this._public = _public;
        this.password = password;
        this.visible = visible;
        this.id = generateID();
        this.lastTimeNoPlayer = System.currentTimeMillis();
    }

    private long generateID() {
        long timestamp = System.currentTimeMillis();
        int random = (int) (Math.random() * 1000);
        return Long.parseLong(timestamp + String.format("%03d", random));
    }
}
