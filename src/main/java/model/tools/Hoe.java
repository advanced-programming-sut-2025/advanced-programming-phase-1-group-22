package model.tools;

import lombok.Getter;
import model.relations.Player;
import model.Tile;
import model.TileType;
import model.abilitiy.Ability;
import utils.App;

@Getter
public enum Hoe implements Tool {
    NORMAL("normal hoe",0, 5),
    COPPER("copper hoe",1, 4),
    IRON("iron hoe",2, 3),
    GOLD("gold hoe",3, 2),
    IRIDIUM("iridium hoe",4, 1);

    private final String name;
    private final int level;
    private final int energyCost;

    Hoe(String name,int level1, int energyUse1) {
        this.name = name;
        this.level = level1;
        this.energyCost = energyUse1;
    }

    @Override
    public void addToolEfficiency(double efficiency) {

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
    public Tool getToolByLevel(int level) {
        for (Hoe value : Hoe.values()) {
            if (value.level == level){
                return value;
            }
        }
        return null;
    }

    @Override
    public int getLevel(){
        return 0;
    }

    @Override
    public int getEnergy(Player player) {
        if (player.getAbilityLevel(Ability.FARMING) == 4){
            return (int) (App.getInstance().getCurrentGame().getWeatherCoefficient() *  energyCost - 1);
        }
        return (int) (App.getInstance().getCurrentGame().getWeatherCoefficient() * energyCost);
    }

    @Override
    public String useTool(Player player, Tile tile) {
        if (tile.getTileType().equals(TileType.PATH) ||
                tile.getTileType().equals(TileType.FENCE) ||
                tile.getTileType().equals(TileType.DOOR)){
            player.changeEnergy(-this.getEnergy(player));
            return "you can not hoe this kind of tile";
        }
        if (!tile.getIsFilled()){
            tile.setTileType(TileType.PLOWED);
            player.changeEnergy(-this.getEnergy(player));
            return "you plowed this tile";
        }
        player.changeEnergy(-this.getEnergy(player));
        return "you use this tool in a wrong way";
    }
}