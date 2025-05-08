package model.tools;

import lombok.Getter;
import model.relations.Player;
import model.Tile;
import model.abilitiy.Ability;
import model.structure.Structure;
import model.structure.farmInitialElements.Lake;
import utils.App;

import java.util.List;

@Getter

public enum WateringCanType implements Tool {
    NORMAL("normal wateringCan",0,40,5),
    CUPPER("cupper wateringCan",1,55,4),
    IRON("iron wateringCan",2,70,3),
    GOLD("gold wateringCan",3,85,2),
    IRIDIUM("iridium wateringCan",4,100,1);

    private final String name;
    private final Integer level;
    private final Integer capacity;
    private final Integer energyCost;
     WateringCanType(String name, Integer level, Integer capacity, Integer energyCost) {
        this.name = name;
         this.level = level;
        this.capacity = capacity;
        this.energyCost = energyCost;
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
        for (WateringCanType value : WateringCanType.values()) {
            if (value.level == level){
                return value;
            }
        }
        return null;
    }

    @Override
    public int getEnergy(Player player) {
        if (player.getAbilityLevel(Ability.FARMING) == 4){
            return (int) (App.getInstance().getCurrentGame().getWeatherCoefficient() * energyCost - 1);
        }
        return (int) (App.getInstance().getCurrentGame().getWeatherCoefficient() * energyCost);
    }

    @Override
    public int getLevel(){
         return level;
    }

    @Override
    public String useTool(Player player, Tile tile) {
        return "";
    }


    @Override
    public Integer getContainingEnergy() {return 0;}
}