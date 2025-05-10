package model.tools;

import lombok.Getter;
import model.TileType;
import model.relations.Player;
import model.Salable;
import model.Tile;
import model.abilitiy.Ability;
import model.products.TreesAndFruitsAndSeeds.Tree;
import model.source.*;
import model.structure.Structure;
import model.structure.Trunk;
import model.structure.TrunkType;
import utils.App;

import java.util.List;

@Getter
public enum Axe implements Tool {
	NORMAL("normal axe", 0, 5, 0),
	COPPER("copper axe", 1, 4, 2000 / 2),
	IRON("iron axe", 2, 3, 5000 / 2),
	GOLD("gold axe", 3, 2, 10000 / 2),
	IRIDIUM("iridium axe", 4, 1, 25000 / 2);

	private final String name;
	private final int level;
	private final int energyCost;
	private final int price;

	Axe(String name, int level1, int energyUse1, int price) {
		this.name = name;
		this.level = level1;
		this.energyCost = energyUse1;
		this.price = price;
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