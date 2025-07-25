package io.github.some_example_name.server.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ServerPlayer {
    public String username;
    public String character;
    public Integer farmId;

    public ServerPlayer(String username) {
        this.username = username;
    }
}
