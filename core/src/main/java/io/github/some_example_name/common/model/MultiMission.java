package io.github.some_example_name.common.model;

import io.github.some_example_name.common.model.relations.Player;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public class MultiMission {
    private final Salable request;
    private final int numberOfRequest;
    private final int rewardGold;
    private final int numberOfRequirePlayer;
    private final int deadline;
    private int startedDay;
    private boolean isActive = false;
    private final Map<Player, Integer> players = new HashMap<>();
    private boolean finished = false;

    public MultiMission(Salable request, int numberOfRequest, int rewardGold, int numberOfRequirePlayer, int deadline) {
        this.request = request;
        this.numberOfRequest = numberOfRequest;
        this.rewardGold = rewardGold;
        this.numberOfRequirePlayer = numberOfRequirePlayer;
        this.deadline = deadline;
    }

    public boolean canAdd(int amount){
        int progress = getMissionProgress();
        return progress + amount <= numberOfRequest;
    }

    public void updateTimeStatus(int today) {
        if (isActive) {
            if (today - startedDay > deadline) {
                finished = true;
            }
        }
    }

    public void addPlayer(Player player, int today) {
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
        if (getMissionProgress() >= numberOfRequest) finished = true;
    }

    public int getMissionProgress(){
        int sum = 0;
        for (Map.Entry<Player, Integer> playerIntegerEntry : players.entrySet()) {
            sum += playerIntegerEntry.getValue();
        }
        return sum;
    }

    public void endMission() {
        players.clear();
        finished = false;
        isActive = false;
    }
}
