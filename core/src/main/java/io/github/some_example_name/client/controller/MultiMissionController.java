package io.github.some_example_name.client.controller;

import io.github.some_example_name.client.GameClient;
import io.github.some_example_name.common.model.MultiMission;
import io.github.some_example_name.common.model.Salable;
import io.github.some_example_name.common.model.records.Response;
import io.github.some_example_name.common.model.relations.Player;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class MultiMissionController {

    public Response addItemToMission(MultiMission mission, Player player, int amount) {
        if (!haveEnough(player, mission.getRequest(), amount)) {
            ;
            return new Response("you don't have enough " + mission.getRequest().getClass().getSimpleName() + " in your inventory");
        }
        deleteProduct(player, mission.getRequest(), amount);
        mission.addProduct(player, amount);
        GameClient.getInstance().updateMultiMissionAdd(mission, amount);
        return new Response("done successfully", true);
    }

    private boolean haveEnough(Player player, Salable salable, int amount) {
        int sum = 0;
        for (Map.Entry<Salable, Integer> salableIntegerEntry : player.getInventory().getProducts().entrySet()) {
            if (salableIntegerEntry.getKey().getClass().equals(salable.getClass())) {
                sum += salableIntegerEntry.getValue();
            }
        }
        return sum >= amount;
    }

    private void deleteProduct(Player player, Salable salable, int amount) {
        List<Map.Entry<Salable, Integer>> entries =
            new ArrayList<>(player.getInventory().getProducts().entrySet());

        for (Map.Entry<Salable, Integer> entry : entries) {
            if (entry.getKey().getClass().equals(salable.getClass())) {
                if (amount == 0) break;

                int currentAmount = entry.getValue();
                if (amount <= currentAmount) {
                    player.getInventory().deleteProductFromBackPack(entry.getKey(), player, amount);
                    break;
                } else {
                    player.getInventory().deleteProductFromBackPack(entry.getKey(), player, currentAmount);
                    amount -= currentAmount;
                }
            }
        }
    }
}
