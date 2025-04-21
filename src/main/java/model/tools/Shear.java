package model.tools;

import lombok.Getter;
import model.Player;
import model.Tile;
import model.abilitiy.Ability;
import model.animal.Animal;
import model.animal.AnimalType;
import model.products.AnimalProduct;
import model.structure.Structure;
import utils.App;

import java.util.List;

@Getter
public enum Shear implements Tool {
    SHEAR("shear",4, 1000);

    private final String name;
    private final Integer energyCost;
    private final Integer price;

    Shear(String name,Integer energyCost, Integer price) {
        this.name = name;
        this.energyCost = energyCost;
        this.price = price;
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
        return price;
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
				if (currentAnimal.getAnimalType().equals(AnimalType.SHEEP)){
                    currentAnimal.setShaving(true);
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

    private void afterUseTool(AnimalProduct animalProduct, Player player, Tile tile){
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
