package io.github.some_example_name.common.model;

import io.github.some_example_name.common.model.relations.NPCHouse;
import io.github.some_example_name.common.utils.App;
import lombok.Getter;

import java.util.*;

public class NpcWalkingStrategy {
    @Getter
    private final Map<Pair, Integer> distances = new HashMap<>();
    private int distance = 0;
    private final App app = App.getInstance();

    public ArrayList<Pair> getPath(Pair origin, Pair dest, NPCHouse house) {
        distances.clear();
        distances.put(origin, 0);
        if (!calculateTile(origin, dest, 0, house)) {
            distances.clear();
            return null;
        }
        return calculatePath(distances, dest);
    }


    public ArrayList<Pair> calculatePath(Map<Pair, Integer> distances, Pair dest) {
        boolean flag = true;
        for (Pair pair : distances.keySet()) {
            if (pair.getX() == dest.getX() && pair.getY() == dest.getY()) {
                dest = pair;
                flag = false;
                break;
            }
        }
        if (flag) return null;
        ArrayList<Pair> path = new ArrayList<>(Collections.nCopies(distances.get(dest) + 1, null));
        path.set(distances.get(dest), dest);
        Integer length = distances.get(dest);
        int[] xs = {1, 0, -1, 0};
        int[] ys = {0, 1, 0, -1};
        while (length >= 1) {
            length--;
            boolean flag1 = true;
            for (int i = 0; flag1 && i < 8; i++) {
                for (Pair pair : distances.keySet()) {
                    if (pair.getX() == dest.getX() + xs[i] && pair.getY() == dest.getY() + ys[i] &&
                        Objects.equals(distances.get(pair), length)) {
                        path.set(length, pair);
                        dest = pair;
                        flag1 = false;
                        break;
                    }
                }
            }
        }
        return path;
    }

    public boolean calculateTile(Pair origin, Pair dest, int distance, NPCHouse npcHouse) {
        if (dest.getX() < 0 || dest.getX() >= 160 || dest.getY() < 0 || dest.getY() >= 120) return false;
        Tile tile = app.getCurrentGame().getTiles()[dest.getX()][dest.getY()];
        if (!tile.isPassable() && !npcHouse.getTiles().contains(tile)) return false;
        if (Objects.equals(origin.getX(), dest.getX()) && Objects.equals(origin.getY(), dest.getY())) {
            this.distance = distance;
            return true;
        }
        int[] xs = {1, 0, -1, 0};
        int[] ys = {0, 1, 0, -1};
        Pair newPair;
        boolean flag = true;
        while (flag) {
            flag = false;
            Set<Pair> set = new HashSet<>(distances.keySet());
            for (Pair pair : set) {
                if (distances.get(pair) == distance) {
                    for (int i = 0; i < 4; i++) {
                        newPair = new Pair(pair.getX() + xs[i], pair.getY() + ys[i]);

                        if (newPair.getX() < 0 || newPair.getX() >= 160 || newPair.getY() < 0 || newPair.getY() >= 120)
                            continue;
                        tile = app.getCurrentGame().getTiles()[newPair.getX()][newPair.getY()];
                        if (!tile.isPassable() && !npcHouse.getTiles().contains(tile)) continue;
                        boolean flag2 = false;
                        for (Pair pair1 : distances.keySet()) {
                            if (Objects.equals(pair1.getX(), newPair.getX()) && Objects.equals(pair1.getY(), newPair.getY())) {
                                flag2 = true;
                                break;
                            }
                        }
                        if (flag2) continue;
                        distances.put(newPair, distance + 1);
                        flag = true;
                        if (Objects.equals(newPair.getX(), dest.getX()) && Objects.equals(newPair.getY(), dest.getY())) {
                            this.distance = distance;
                            return true;
                        }
                    }
                }
            }
            distance++;
        }
        return false;
    }
 }
