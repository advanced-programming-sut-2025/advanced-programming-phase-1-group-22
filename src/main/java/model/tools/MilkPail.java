package model.tools;

import lombok.Getter;
import model.relations.Player;
import model.Tile;
import model.abilitiy.Ability;
import model.animal.Animal;
import model.animal.AnimalType;
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
            return (int) (App.getInstance().getCurrentGame().getWeatherCoefficient() * energyCost - 1);
        }
        return (int) (App.getInstance().getCurrentGame().getWeatherCoefficient() * energyCost);
    }

    @Override
    public String useTool(Player player, Tile tile) {
        List<Structure> structures = App.getInstance().getCurrentGame().getVillage().findStructuresByTile(tile);
        for (Structure structure : structures) {
            if (structure != null){
                if (structure instanceof Animal currentAnimal){
                    if (currentAnimal.getAnimalType().equals(AnimalType.COW) ||
                            currentAnimal.getAnimalType().equals(AnimalType.GOAT)){
                        player.changeEnergy(-this.getEnergy(player));
                        if (currentAnimal.getTodayProduct() == null){
                            return "this animal do not have product";
                        }
                        if (player.getInventory().isInventoryHaveCapacity(currentAnimal.getTodayProduct())){
                            AnimalProduct animalProduct = currentAnimal.getTodayProduct();
                            player.getInventory().addProductToBackPack(animalProduct,1);
                            currentAnimal.setTodayProduct(null);
                            currentAnimal.changeFriendShip(5);
                            return "you collect produce of " + currentAnimal.getName() + ": " + animalProduct.getName() +
                                    " with quality: " + animalProduct.getProductQuality();
                        }
                        return "your inventory is full so you can not milk animal";
                    }
                }
            }
        }
        player.changeEnergy(-this.getEnergy(player));
        return "you use this tool in a wrong way";
    }

    @Override
    public Integer getContainingEnergy() {return 0;}
}