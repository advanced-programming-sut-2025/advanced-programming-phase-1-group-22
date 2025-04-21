package model.tools;

import lombok.Getter;
import model.Player;
import model.Tile;
import model.TileType;
import model.abilitiy.Ability;
import model.exception.InvalidInputException;

@Getter
public class Scythe implements Tool {
    private final Integer energyCost = 2;

    @Override
    public void addToolEfficiency(double efficiency) {

    }

    @Override
    public String getName() {
        return "scythe";
    }

    @Override
    public int getSellPrice() {
        return 0;
    }

    @Override
    public Tool getToolByLevel(int level) {
        throw new InvalidInputException("this tool does not have level");
    }

    @Override
    public int getLevel() {
        return 0;
    }

    @Override
    public int getEnergy(Player player) {
        if(player.getAbilityLevel(Ability.FARMING) == 4){
            return energyCost - 1;
        }
        return energyCost;
    }

    @Override
    public String useTool(Player player, Tile tile) {
        boolean success = false;
        if (tile.getTileType().equals(TileType.GRASS) && !tile.getIsFilled()){
            tile.setTileType(TileType.FLAT);
            success = true;
        }

        //برداشت محصولات
        player.changeEnergy(-this.getEnergy(player));
        if (success){
            return "you successfully use this tool";
        }
        return "you use this tool in a wrong way";
    }
}