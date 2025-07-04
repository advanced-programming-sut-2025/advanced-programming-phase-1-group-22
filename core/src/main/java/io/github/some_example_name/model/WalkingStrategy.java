package io.github.some_example_name.model;

import lombok.Getter;
import io.github.some_example_name.utils.App;

import java.util.*;

public class WalkingStrategy {
    @Getter
    private final Map<Pair, Integer> distances = new HashMap<>();
    private int distance = 0;
    private final App app = App.getInstance();

    public int calculateEnergy(Pair origin, Pair dest) {
        distances.clear();
        distances.put(origin, 0);
        if (!calculateTile(origin, dest, 0)) {
            distances.clear();
            return -1;
        }
        return (int) Math.ceil(((double) distance) / 20) + new Random().nextInt(10)/9;
    }

    public boolean calculateTile(Pair origin, Pair dest, int distance) {
        if (dest.getX() < 0 || dest.getX() >= 160 || dest.getY() < 0 || dest.getY() >= 120) return false;
        if (!app.getCurrentGame().getTiles()[dest.getX()][dest.getY()].isPassable()) return false;
        if (Objects.equals(origin.getX(), dest.getX()) && Objects.equals(origin.getY(), dest.getY())) {
            this.distance = distance;
            return true;
        }
        int[] xs = {1, 0, -1, 0, 1, 1, -1, -1};
        int[] ys = {0, 1, 0, -1, 1, -1, 1, -1};
        Pair newPair;
        boolean flag = true;
        while (flag) {
//            if (distance % 10 == 0) printMap();
            flag = false;
            Set<Pair> set = new HashSet<>(distances.keySet());
            for (Pair pair : set) {
                if (distances.get(pair) == distance) {
                    for (int i = 0; i < 8; i++) {
                        newPair = new Pair(pair.getX() + xs[i], pair.getY() + ys[i]);

                        if (newPair.getX() < 0 || newPair.getX() >= 160 || newPair.getY() < 0 || newPair.getY() >= 120)
                            continue;
                        if (!app.getCurrentGame().getTiles()[newPair.getX()][newPair.getY()].isPassable()) continue;
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

    private void printMap() {
        Character[][] str = new Character[160][120];
        for (int i = 0; i < 120; i++) {
            for (int j = 0; j < 160; j++) {
                str[j][i] = ' ';
            }
        }
        for (Pair pair : distances.keySet()) {
            str[pair.getX()][pair.getY()] = 'A';
        }


        for (int i = 0; i < 120; i++) {
            for (int j1 = 0; j1 < 160; j1++) {
                System.out.print(str[j1][120 - 1 - i]);
            }
            System.out.println();
        }
        System.out.println("==================================================================================================================");
    }
}
