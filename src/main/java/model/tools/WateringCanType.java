package model.tools;

import lombok.Getter;
import model.Player;
import model.Tile;
import model.abilitiy.Ability;
import model.exception.InvalidInputException;
import model.structure.Structure;
import model.structure.farmInitialElements.Lake;
import utils.App;

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
    private Boolean isFullOfWater;
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
        throw new InvalidInputException("this tool does not have level");
    }

    @Override
    public int getEnergy(Player player) {
        if (player.getAbilityLevel(Ability.FARMING) == 4){
            return energyCost - 1;
        }
        return energyCost;
    }

    @Override
    public int getLevel(){
         return level;
    }

    @Override
    public String useTool(Player player, Tile tile) {
        Structure structure = App.getInstance().getCurrentGame().getVillage().getStructureInTile(tile);
        boolean success = false;
        if (structure != null){
            if (structure instanceof Lake){ // or GreenHouse
                isFullOfWater = true;
                success = true;
            }
        }
        // آب دادن به محصولات
        player.changeEnergy(-this.getEnergy(player));
        if (success){
            return "you successfully use this tool";
        }
        return "you use this tool in a wrong way";
    }
}