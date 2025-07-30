package io.github.some_example_name.server.model;

import lombok.Getter;

@Getter
public class Message {
    private final String message;
    private final long time;

    public Message(String message){
        this.message = message;
        this.time = System.currentTimeMillis();
    }
}
