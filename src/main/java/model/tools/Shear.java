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
public enum Shear implements Tool {
	SHEAR("shear", 4, 1000 / 2);

	private final String name;
	private final Integer energyCost;
	private final Integer price;

	Shear(String name, Integer energyCost, Integer price) {
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
		return this.price;
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
		int minus = 0;
		if (player.getAbilityLevel(Ability.FARMING)==4) {
			minus += 1;
		}
		if (player.getBuffAbility()!=null && player.getBuffAbility().equals(Ability.FARMING)) {
			minus += 1;
		}
		return (int) (App.getInstance().getCurrentGame().getWeatherCoefficient() * energyCost - minus);
	}

	@Override
	public String useTool(Player player, Tile tile) {
		player.changeEnergy(-this.getEnergy(player));
		List<Structure> structures = App.getInstance().getCurrentGame().getVillage().findStructuresByTile(tile);
		for (Structure structure : structures) {
			if (structure!=null) {
				if (structure instanceof Animal currentAnimal) {
					if (currentAnimal.getAnimalType().equals(AnimalType.SHEEP)) {
						if (currentAnimal.getTodayProduct()==null) {
							return "this animal does not have product";
						}
						AnimalProduct animalProduct = currentAnimal.getTodayProduct();
						if (player.getInventory().isInventoryHaveCapacity(animalProduct)) {
							player.getInventory().addProductToBackPack(animalProduct, 1);
							currentAnimal.setTodayProduct(null);
							currentAnimal.changeFriendShip(5);
							return "you collect produce of " + currentAnimal.getName() + ": " + animalProduct.getName() +
									" with quality: " + animalProduct.getProductQuality();
						}
						return "your inventory is full so you can not shear animal";
					}
				}
			}
		}
		return "you use this tool in a wrong way";
	}

	@Override
	public Integer getContainingEnergy() {
		return 0;
	}
}
