package io.github.some_example_name.model.tools;

import com.badlogic.gdx.graphics.g2d.Sprite;
import io.github.some_example_name.utils.GameAsset;
import lombok.Getter;
import io.github.some_example_name.model.relations.Player;
import io.github.some_example_name.model.Tile;
import io.github.some_example_name.model.abilitiy.Ability;
import io.github.some_example_name.model.animal.Animal;
import io.github.some_example_name.model.animal.AnimalType;
import io.github.some_example_name.model.products.AnimalProduct;
import io.github.some_example_name.model.structure.Structure;
import io.github.some_example_name.utils.App;

import java.util.List;

@Getter
public class MilkPail implements Tool {
	private static MilkPail instance;
    private Sprite sprite;

	private MilkPail() {
        this.sprite = new Sprite(GameAsset.MILK_PAIL);
        this.sprite.setSize(App.tileWidth,App.tileHeight);
	}

	public static MilkPail getInstance() {
		if (instance==null) {
			instance = new MilkPail();
		}
		return instance;
	}

	private final Integer energyCost = 4;
	private final Integer price = 1000 / 2;

	@Override
	public void addToolEfficiency(double efficiency) {

	}

	@Override
	public String getName() {
		return "milkpail";
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
		List<Structure> structures = App.getInstance().getCurrentGame().getVillage().findStructuresByTile(tile);
		for (Structure structure : structures) {
			if (structure!=null) {
				if (structure instanceof Animal currentAnimal) {
					if (currentAnimal.getAnimalType().equals(AnimalType.COW) ||
							currentAnimal.getAnimalType().equals(AnimalType.GOAT)) {
						player.changeEnergy(-this.getEnergy(player));
						if (currentAnimal.getTodayProduct()==null) {
							return "this animal do not have product";
						}
						if (player.getInventory().isInventoryHaveCapacity(currentAnimal.getTodayProduct())) {
							AnimalProduct animalProduct = currentAnimal.getTodayProduct();
							player.getInventory().addProductToBackPack(animalProduct, 1);
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
	public Integer getContainingEnergy() {
		return 0;
	}
}
