package model;

import utils.App;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class WalkingStrategy {
    private Map<Pair, Integer> distances = new HashMap<>();
    private int distance = 0;
    private App app = App.getInstance();
    public int calculateEnergy(Pair origin, Pair dest) {
        if (!calculateTile(origin, dest, 0)) {
            return -1;
        }
        return distance/20;
    }

    public boolean calculateTile(Pair origin, Pair dest, int distance) {
        if (Objects.equals(origin.getX(), dest.getX()) && Objects.equals(origin.getY(), dest.getY())) {
            this.distance = distance;
            return true;
        }
        int[] xs = {1,1,0,-1,-1,-1,0,1};
        int[] ys = {0,1,1,1,0,-1,-1,-1};
        Pair newPair;
        for (Pair pair : distances.keySet()) {
            if (distances.get(pair) == distance) {
                for (int i = 0; i < 8; i++) {
                    newPair = new Pair(origin.getX() + xs[i], origin.getY() + ys[i]);
                    if (!app.getCurrentGame().getTiles()[newPair.getX()][newPair.getY()].isPassible()) continue;
                    if (distances.containsKey(newPair)) continue;
                    distances.put(newPair, distance + 1);
                    if (calculateTile(newPair, dest, distance + 1)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
