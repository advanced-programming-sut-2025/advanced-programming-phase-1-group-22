package model.tools;

import lombok.Getter;
import model.Player;
import model.Tile;
import model.abilitiy.Ability;
import model.animal.Animal;
import model.animal.AnimalType;
import model.exception.InvalidInputException;
import model.products.AnimalProduct;
import model.structure.Structure;
import utils.App;

import java.util.List;

@Getter
public class MilkPail implements Tool {
    private static MilkPail instance;

    private MilkPail() {
    }

    public static MilkPail getInstance() {
        if (instance == null) {
            instance = new MilkPail();
        }
        return instance;
    }

    private final Integer energyCost = 4;
    private final Integer price = 100;

    @Override
    public void addToolEfficiency(double efficiency) {

    }

    @Override
    public String getName() {
        return "milkpail";
    }

    @Override
    public int getSellPrice() {
        return 0;
    }

    @Override
    public Tool getToolByLevel(int level) {
        return null;
    }

    @Override
    public int getLevel() {
        return 0;
    }

    @Override
    public int getEnergy(Player player) {
        if (player.getAbilityLevel(Ability.FARMING) == 4){
            return energyCost - 1;
        }
        return energyCost;
    }

    @Override
    public String useTool(Player player, Tile tile) {
        Structure structure = App.getInstance().getCurrentGame().getVillage().getStructureInTile(tile);
        boolean success = false;
        if (structure != null){
            if (structure instanceof Animal currentAnimal){
				if (currentAnimal.getAnimalType().equals(AnimalType.COW) ||
                        currentAnimal.getAnimalType().equals(AnimalType.GOAT)){
                    currentAnimal.setMilk(true);
                    afterUseTool(currentAnimal.getAnimalProduct(),player,tile);
                    success = true;
                }
            }
        }
        player.changeEnergy(-this.getEnergy(player));
        if (success){
            return "you successfully use this tool";
        }
        return "you use this tool in a wrong way";
    }

    private void afterUseTool(AnimalProduct animalProduct, Player player,Tile tile){
        if (player.getInventory().isInventoryHaveCapacity(animalProduct)){
            player.getInventory().addProductToBackPack(animalProduct,1);
        }
        else {
            animalProduct.setTiles(List.of(tile));
            animalProduct.setIsDropped(true);
            App.getInstance().getCurrentGame().getVillage().addStructureToPlayerFarmByPlayerTile(player,animalProduct);
        }
    }
}