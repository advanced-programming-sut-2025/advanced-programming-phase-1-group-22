package model.tools;

import model.Player;
import model.Salable;
import model.Tile;

public interface Tool extends Salable {
    void addToolEfficiency(double efficiency);
    Tool getToolByLevel(int level);
    int getLevel();
    int getEnergy(Player player);
    String useTool(Player player, Tile tile);
}