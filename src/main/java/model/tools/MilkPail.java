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
            return energyCost - 1;
        }
        return energyCost;
    }

    @Override
    public String useTool(Player player, Tile tile) {
        boolean success = false;
        List<Structure> structures = App.getInstance().getCurrentGame().getVillage().findStructuresByTile(tile);
        for (Structure structure : structures) {
            if (structure != null){
                if (structure instanceof Animal currentAnimal){
                    if (currentAnimal.getAnimalType().equals(AnimalType.COW) ||
                            currentAnimal.getAnimalType().equals(AnimalType.GOAT)){
                        if (currentAnimal.getTodayProduct() == null){
                            player.changeEnergy(-this.getEnergy(player));
                            return "this animal do not have product";
                        }
                        afterUseTool(currentAnimal.getTodayProduct(),player,tile);
                        currentAnimal.setTodayProduct(null);
                        int oldFriendShip = currentAnimal.getRelationShipQuality();
                        currentAnimal.setRelationShipQuality(oldFriendShip + 5);
                        success = true;
                        break;
                    }
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
            animalProduct.setIsPickable(true);
            App.getInstance().getCurrentGame().getVillage().addStructureToPlayerFarmByPlayerTile(player,animalProduct);
        }
    }
}