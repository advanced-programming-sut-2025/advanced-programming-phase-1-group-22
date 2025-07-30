package io.github.some_example_name.common.model;

import io.github.some_example_name.common.model.relations.Player;
import io.github.some_example_name.common.utils.App;
import lombok.Getter;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Getter
public class MultiMission {
    private final int id;
    private final Salable request;
    private final int numberOfRequest;
    private final int rewardGold;
    private final int numberOfRequirePlayer;
    private final int deadline;
    private int startedDay;
    private boolean isActive = false;
    private final Map<Player, Integer> players = Collections.synchronizedMap(new HashMap<>());
    private boolean finished = false;

    public MultiMission(int id, Salable request, int numberOfRequest, int rewardGold, int numberOfRequirePlayer, int deadline) {
        this.id = id;
        this.request = request;
        this.numberOfRequest = numberOfRequest;
        this.rewardGold = rewardGold;
        this.numberOfRequirePlayer = numberOfRequirePlayer;
        this.deadline = deadline;
    }

    public boolean canAdd(int amount) {
        int progress = getMissionProgress();
        return progress + amount <= numberOfRequest;
    }

    public void updateTimeStatus(int today) {
        if (isActive) {
            if (today - startedDay > deadline) {
                finished = true;
                handleFinishMission();
            }
        }
    }

    public void addPlayer(Player player, int today) {
        if (isActive) return;
        if (!players.containsKey(player)) {
            players.put(player, 0);
            updateActiveStatus(today);
        }
    }

    private void updateActiveStatus(int today) {
        if (players.size() == numberOfRequirePlayer) {
            isActive = true;
            startedDay = today;
        }
    }

    public void addProduct(Player player, int amount) {
        if (players.containsKey(player))
            players.put(player, players.get(player) + amount);
        updateFinishStatus();
    }

    private void updateFinishStatus() {
        if (getMissionProgress() >= numberOfRequest) {
            finished = true;
            handleFinishMission();
        }
    }

    public int getMissionProgress() {
        int sum = 0;
        synchronized (players) {
            for (Map.Entry<Player, Integer> playerIntegerEntry : players.entrySet()) {
                sum += playerIntegerEntry.getValue();
            }
        }
        return sum;
    }

    public void endMission() {
        App.getInstance().getCurrentGame().removeMission(this);
        synchronized (players) {
            for (Map.Entry<Player, Integer> playerIntegerEntry : players.entrySet()) {
                Player player = playerIntegerEntry.getKey();
                player.getActiveMissions().remove(this);
            }
        }
    }

    private void handleFinishMission() {
        if (getMissionProgress() == numberOfRequest) {
            synchronized (players) {
                for (Map.Entry<Player, Integer> playerIntegerEntry : players.entrySet()) {
                    Player player = playerIntegerEntry.getKey();
                    player.getAccount().setGolds(player.getAccount().getGolds() + rewardGold);
                }
            }
        }
        endMission();
    }
}
