package io.github.some_example_name.common.model;

import io.github.some_example_name.server.model.GameServer;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class Lobby {
    private final List<String> members = new ArrayList<>();
    private String admin;
    private long id;
    private final String name;
    private final boolean _public;
    private final String password;
    private final boolean visible;
    private long lastTimeNoPlayer;
    private GameServer gameServer;
    private boolean gameStart;
    private boolean gameServerSaved;

    public Lobby(String admin, String name, boolean _public, String password, boolean visible) {
        this.admin = admin;
        this.name = name;
        this._public = _public;
        this.password = password;
        this.visible = visible;
        this.id = generateID();
        this.lastTimeNoPlayer = System.currentTimeMillis();
        this.members.add(admin);
    }

    private long generateID() {
        long timestamp = System.currentTimeMillis();
        int random = (int) (Math.random() * 1000);
        return Long.parseLong(timestamp + String.format("%03d", random));
    }
}
