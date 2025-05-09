package model.tools;

import model.relations.Player;
import model.Salable;
import model.Tile;

import java.io.Serializable;

public interface Tool extends Salable, Serializable {
    void addToolEfficiency(double efficiency);
    Tool getToolByLevel(int level);
    int getLevel();
    int getEnergy(Player player);
    String useTool(Player player, Tile tile);
}