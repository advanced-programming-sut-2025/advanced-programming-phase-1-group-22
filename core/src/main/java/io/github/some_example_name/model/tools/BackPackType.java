package io.github.some_example_name.model.tools;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import io.github.some_example_name.utils.GameAsset;
import lombok.Getter;
import io.github.some_example_name.model.relations.Player;
import io.github.some_example_name.model.Tile;

@Getter
public enum BackPackType implements Tool {
    NORMAL_BACKPACK("normal backpack",0,12, GameAsset.BACKPACK),
    BIG_BACKPACK("big backpack",1,24,GameAsset.BACKPACK),
    DELUXE_BACKPACK("deluxe backpack",2,true,GameAsset.BACKPACK_36);

    private final String name;
    private Integer capacity;
    private Integer level;
    private Boolean isInfinite = false;
    private final Texture texture;

    BackPackType(String name,int level,int capacity,Texture texture) {
        this.name = name;
        this.level = level;
        this.capacity = capacity;
        this.texture = texture;
    }

    BackPackType(String name,int level,boolean isInfinite,Texture texture) {
        this.name = name;
        this.level = level;
        this.isInfinite = isInfinite;
        this.texture = texture;
    }

    @Override
    public void addToolEfficiency(double efficiency) {
    }

    @Override
    public Tool getToolByLevel(int level) {
        for (BackPackType value : BackPackType.values()) {
            if (value.getLevel() == level){
                return value;
            }
        }
        return null;
    }

    @Override
    public int getLevel() {
        return this.level;
    }

    @Override
    public int getEnergy(Player player) {
        return 0;
    }

    @Override
    public String useTool(Player player, Tile tile) {
        return "";
    }

    @Override
    public Sprite getSprite() {
        return null;
    }

    @Override
    public String getName() {
        return this.name.toLowerCase();
    }

    @Override
    public int getSellPrice() {
        return 0;
    }


    @Override
    public Integer getContainingEnergy() {return 0;}
}
