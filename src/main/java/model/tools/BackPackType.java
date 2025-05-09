package model.tools;

import lombok.Getter;
import model.relations.Player;
import model.Tile;

@Getter
public enum BackPackType implements Tool {
    NORMAL_BACKPACK("normal backpack",0,12),
    BIG_BACKPACK("big backpack",1,24),
    DELUXE_BACKPACK("deluxe backpack",2,true);

    private final String name;
    private Integer capacity;
    private Integer level;
    private Boolean isInfinite = false;

    BackPackType(String name,int level,int capacity) {
        this.name = name;
        this.level = level;
        this.capacity = capacity;
    }

    BackPackType(String name,int level,boolean isInfinite) {
        this.name = name;
        this.level = level;
        this.isInfinite = isInfinite;
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
