package io.github.some_example_name.common.model.tools;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import io.github.some_example_name.client.GameClient;
import io.github.some_example_name.common.model.StructureUpdateState;
import io.github.some_example_name.common.utils.GameAsset;
import lombok.Getter;
import io.github.some_example_name.common.model.relations.Player;
import io.github.some_example_name.common.model.Tile;
import io.github.some_example_name.common.model.TileType;
import io.github.some_example_name.common.model.abilitiy.Ability;
import io.github.some_example_name.common.model.source.Mineral;
import io.github.some_example_name.common.model.source.MineralType;
import io.github.some_example_name.common.model.structure.Stone;
import io.github.some_example_name.common.model.structure.StoneType;
import io.github.some_example_name.common.model.structure.Structure;
import io.github.some_example_name.common.utils.App;

import java.util.List;

@Getter
public enum Pickaxe implements Tool {
    NORMAL("normal pickaxe", 0, 5, 0,
        new Sprite(GameAsset.PICKAXE),GameAsset.PICKAXE),
    COPPER("copper pickaxe", 1, 4, 2000 / 2,
        new Sprite(GameAsset.COPPER_PICKAXE),GameAsset.COPPER_PICKAXE),
    IRON("iron pickaxe", 2, 3, 5000 / 2,
        new Sprite(GameAsset.STEEL_PICKAXE),GameAsset.STEEL_PICKAXE),
    GOLD("gold pickaxe", 3, 2, 10000 / 2,
        new Sprite(GameAsset.GOLD_PICKAXE),GameAsset.GOLD_PICKAXE),
    IRIDIUM("iridium pickaxe", 4, 1, 25000 / 2,
        new Sprite(GameAsset.IRIDIUM_PICKAXE),GameAsset.IRIDIUM_PICKAXE);

    private final String name;
    private final int level;
    private final int energyCost;
    private final int price;
    private final Texture texture;
    private final Sprite sprite;

    Pickaxe(String name, int level1, int energyUse1, int price,Sprite sprite,Texture texture) {
        this.name = name;
        this.level = level1;
        this.energyCost = energyUse1;
        this.price = price;
        this.sprite = sprite;
        this.sprite.setSize(App.tileWidth,App.tileHeight);
        this.texture = texture;
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
	public int getLevel() {
		return this.level;
	}

	@Override
	public Tool getToolByLevel(int level) {
		for (Pickaxe value : Pickaxe.values()) {
			if (value.getLevel()==level) {
				return value;
			}
		}
		return null;
	}

	@Override
	public int getEnergy(Player player) {
		int minus = 0;
		if (player.getAbilityLevel(Ability.MINING)==4) {
			minus += 1;
		}
		if (player.getBuffAbility()!=null && player.getBuffAbility().equals(Ability.MINING)) {
			minus += 1;
		}
		return (int) (App.getInstance().getCurrentGame().getWeatherCoefficient() * energyCost - minus);
	}

	@Override
	public String useTool(Player player, Tile tile) {
		if (tile.getTileType().equals(TileType.PLOWED) && !tile.getIsFilled()) {
			tile.setTileType(TileType.FLAT);
            GameClient.getInstance().updateTileState(tile);
			player.upgradeAbility(Ability.MINING);
			player.upgradeAbility(Ability.FORAGING);
			player.changeEnergy(-this.getEnergy(player));
			return "you set tile type from plowed to flat";
		}
		List<Structure> structures = App.getInstance().getCurrentGame().getVillage().findStructuresByTile(tile);
		for (Structure structure : structures) {
			if (structure!=null) {
				if (structure instanceof Stone) {
					if (((Stone) structure).getStoneType().equals(StoneType.SMALL_STONE)) {
						Mineral mineral = new Mineral(MineralType.STONE);
						return breakStone(mineral, player, structure);
					}
				} else if (structure instanceof Mineral) {
					if (((Mineral) structure).getForagingMineralType().equals(MineralType.COPPER_ORE)) {
						Mineral mineral = new Mineral(MineralType.COPPER_ORE);
						return breakStone(mineral, player, structure);
					} else if (((Mineral) structure).getForagingMineralType().equals(MineralType.IRON_ORE)) {
						if (this.level >= 1) {
							Mineral mineral = new Mineral(MineralType.IRON_ORE);
							return breakStone(mineral, player, structure);
						}
						player.changeEnergy(-Math.max(0, this.getEnergy(player) - 1));
						return "your tool have to be at least level 1 to break iron ore";
					} else if (((Mineral) structure).getForagingMineralType().equals(MineralType.IRIDIUM_ORE)) {
						if (this.level >= 3) {
							Mineral mineral = new Mineral(MineralType.IRIDIUM_ORE);
							return breakStone(mineral, player, structure);
						}
						player.changeEnergy(-Math.max(0, this.getEnergy(player) - 1));
						return "your tool have to be at least level 3 to break iridium ore";
					} else if (((Mineral) structure).getForagingMineralType().equals(MineralType.GOLD_ORE)) {
						if (this.level >= 2) {
							Mineral mineral = new Mineral(MineralType.GOLD_ORE);
							return breakStone(mineral, player, structure);
						}
						player.changeEnergy(-Math.max(0, this.getEnergy(player) - 1));
						return "your tool have to be at least level 2 to break gold ore";
					} else {
						if (this.level >= 2) {
							Mineral mineral = new Mineral(((Mineral) structure).getForagingMineralType());
							return breakStone(mineral, player, structure);
						}
						player.changeEnergy(-Math.max(0, this.getEnergy(player) - 1));
						return "your tool have to be at least level 2 to break jewelry ore";
					}
				} else if (structure.getIsPickable()) {
					App.getInstance().getCurrentGame().getVillage().removeStructure(structure);
					return "you remove a item";
				}
			}
		}
		player.changeEnergy(-Math.max(0, this.getEnergy(player) - 1));
		return "you use this tool in a wrong way";
	}

	private String breakStone(Mineral mineral, Player player, Structure structure) {
		if (player.getInventory().isInventoryHaveCapacity(mineral)) {
			player.getInventory().addProductToBackPack(mineral, 1);
            GameClient.getInstance().updatePlayerAddToInventory(player,mineral,1);
            player.upgradeAbility(Ability.MINING);
			player.upgradeAbility(Ability.FORAGING);
			player.changeEnergy(-this.getEnergy(player));
			App.getInstance().getCurrentGame().getVillage().removeStructure(structure);
            GameClient.getInstance().updateStructureState(structure, StructureUpdateState.DELETE,true,null);
			return "you break a stone and get " + mineral.getName();
		}
		player.changeEnergy(-Math.max(0, this.getEnergy(player) - 1));
		return "your inventory is full so you can not break " + mineral.getName();
	}

	@Override
	public Integer getContainingEnergy() {
		return 0;
	}
}
