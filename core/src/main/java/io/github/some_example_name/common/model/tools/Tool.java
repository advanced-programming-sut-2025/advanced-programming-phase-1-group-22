package io.github.some_example_name.common.model.tools;

import com.badlogic.gdx.graphics.g2d.Sprite;
import io.github.some_example_name.common.model.relations.Player;
import io.github.some_example_name.common.model.Salable;
import io.github.some_example_name.common.model.Tile;

public interface Tool extends Salable {
    void addToolEfficiency(double efficiency);
    Tool getToolByLevel(int level);
    int getLevel();
    int getEnergy(Player player);
    String useTool(Player player, Tile tile);
    Sprite getSprite();
}
