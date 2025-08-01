package io.github.some_example_name.server.model;

import lombok.Getter;

import java.util.ArrayList;
import java.util.HashMap;

@Getter
public class NpcGift {
    private final HashMap<String, ArrayList<String>> gifts = new HashMap<>();

    public void addGift(String player, String gift) {
        if (!gifts.containsKey(player)) {
            gifts.put(player, new ArrayList<>());
        }
        gifts.get(player).add(gift);
    }
}
