package io.github.some_example_name.model.tools;

import com.badlogic.gdx.graphics.g2d.Sprite;
import io.github.some_example_name.utils.GameAsset;
import lombok.Getter;
import io.github.some_example_name.model.TileType;
import io.github.some_example_name.model.relations.Player;
import io.github.some_example_name.model.Salable;
import io.github.some_example_name.model.Tile;
import io.github.some_example_name.model.abilitiy.Ability;
import io.github.some_example_name.model.products.TreesAndFruitsAndSeeds.Tree;
import io.github.some_example_name.model.source.*;
import io.github.some_example_name.model.structure.Structure;
import io.github.some_example_name.model.structure.Trunk;
import io.github.some_example_name.model.structure.TrunkType;
import io.github.some_example_name.utils.App;

import java.util.List;

@Getter
public enum Axe implements Tool {
    NORMAL("normal axe", 0, 5, 0,new Sprite(GameAsset.AXE)),
    COPPER("copper axe", 1, 4, 2000 / 2,new Sprite(GameAsset.COPPER_AXE)),
    IRON("iron axe", 2, 3, 5000 / 2,new Sprite(GameAsset.STEEL_AXE)),
    GOLD("gold axe", 3, 2, 10000 / 2,new Sprite(GameAsset.GOLD_AXE)),
    IRIDIUM("iridium axe", 4, 1, 25000 / 2,new Sprite(GameAsset.IRIDIUM_AXE));

    private final String name;
    private final int level;
    private final int energyCost;
    private final int price;
    private final Sprite sprite;

    Axe(String name, int level1, int energyUse1, int price,Sprite sprite) {
        this.name = name;
        this.level = level1;
        this.energyCost = energyUse1;
        this.price = price;
        this.sprite = sprite;
        this.sprite.setSize(App.tileWidth,App.tileHeight);
    }

	@Override
	public void addToolEfficiency(double efficiency) {

	}

	@Override
	public Tool getToolByLevel(int level) {
		for (Axe value : Axe.values()) {
			if (value.getLevel()==level) {
				return value;
			}
		}
		return null;
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
	public int getLevel() {
		return this.level;
	}

	@Override
	public int getEnergy(Player player) {
		int minus = 0;
		if (player.getAbilityLevel(Ability.FORAGING)==4) {
			minus += 1;
		}
		if (player.getBuffAbility()!=null && player.getBuffAbility().equals(Ability.FORAGING)) {
			minus += 1;
		}
		return (int) (App.getInstance().getCurrentGame().getWeatherCoefficient() * energyCost - minus);
	}

	@Override
	public String useTool(Player player, Tile tile) {
		boolean success = false;
		StringBuilder message = new StringBuilder();
		List<Structure> structures = App.getInstance().getCurrentGame().getVillage().findStructuresByTile(tile);
		for (Structure structure : structures) {
			if (structure!=null) {
				if (structure instanceof Trunk) {
					if (((Trunk) structure).getIsBurn()) {
						Mineral mineral = new Mineral(MineralType.COAL);
						if (player.getInventory().isInventoryHaveCapacity(mineral)) {
							addToInventoryAndDeleteStructure(mineral, player, structure);
							message.append("you break a burned trunk and get coal");
							success = true;
							break;
						}
						message.append("your inventory is full so you can not break this burned trunk");
						break;
					}
					if (((Trunk) structure).getTrunkType().equals(TrunkType.SMALL_TRUNK)) {
						Mineral mineral = new Mineral(MineralType.WOOD);
						if (player.getInventory().isInventoryHaveCapacity(mineral)) {
							addToInventoryAndDeleteStructure(mineral, player, structure);
							message.append("you break a trunk and get wood");
							success = true;
							break;
						}
						message.append("your inventory is full so you can not break trunk");
						break;
					} else {
						message.append("you only can break small trunk");
					}
				}
				if (structure instanceof Crop) {
					if (((Crop) structure).getBurn()) {
						Mineral mineral = new Mineral(MineralType.COAL);
						if (player.getInventory().isInventoryHaveCapacity(mineral)) {
							addToInventoryAndDeleteStructure(mineral, player, structure);
							message.append("you break a burned crop and get coal");
							success = true;
							break;
						}
						message.append("your inventory is full so you can not break this burned crop");
						break;
					}
				}
				if (structure instanceof Tree) {
					if (((Tree) structure).getBurn()) {
						Mineral mineral = new Mineral(MineralType.COAL);
						if (player.getInventory().isInventoryHaveCapacity(mineral)) {
							addToInventoryAndDeleteStructure(mineral, player, structure);
							message.append("you break a burned tree and get coal");
							success = true;
							break;
						}
						message.append("your inventory is full so you can not break this burned tree");
						break;
					}
					if (((Tree) structure).getTreeType().getIsForaging()) {
						Seed seed = new Seed((SeedType) ((Tree) structure).getTreeType().getSource());
						Mineral mineral = new Mineral(MineralType.WOOD);
						if (player.getInventory().isInventoryHaveCapacity(seed) &&
								player.getInventory().isInventoryHaveCapacity(mineral)) {
							addToInventoryAndDeleteStructure(mineral, player, structure);
							addToInventoryAndDeleteStructure(seed, player, structure);
							message.append("you break a foraging tree and get wood and seed");
							success = true;
							break;
						}
						message.append("your inventory is full so you can not break tree");
						break;
					}
					Mineral mineral = new Mineral(MineralType.WOOD);
					if (player.getInventory().isInventoryHaveCapacity(mineral)) {
						addToInventoryAndDeleteStructure(mineral, player, structure);
						message.append("you break a tree and get wood");
						success = true;
						break;
					}
					message.append("your inventory is full so you can not break tree");
					break;
				}
			}
		}

		if (success) {
			player.upgradeAbility(Ability.FORAGING);
			player.changeEnergy(-this.getEnergy(player));
			return message.toString();
		}
		player.changeEnergy(-Math.max(0, this.getEnergy(player) - 1));
		if (message.isEmpty()) {
			return "you use this tool in a wrong way";
		}
		return message.toString();
	}

	private void addToInventoryAndDeleteStructure(Salable mineralOrSeed, Player player, Structure structure) {
		player.getInventory().addProductToBackPack(mineralOrSeed, 1);
		for (Tile tile : structure.getTiles()) {
			tile.setIsFilled(false);
			tile.setTileType(TileType.FLAT);
		}
		App.getInstance().getCurrentGame().getVillage().removeStructure(structure);
	}

	@Override
	public Integer getContainingEnergy() {
		return 0;
	}
}
