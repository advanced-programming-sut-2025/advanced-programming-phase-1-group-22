package io.github.some_example_name.model.tools;

import com.badlogic.gdx.graphics.g2d.Sprite;
import io.github.some_example_name.utils.GameAsset;
import lombok.Getter;
import io.github.some_example_name.model.relations.Player;
import io.github.some_example_name.model.Tile;
import io.github.some_example_name.model.TileType;
import io.github.some_example_name.model.abilitiy.Ability;
import io.github.some_example_name.utils.App;

@Getter
public enum Hoe implements Tool {
    NORMAL("normal hoe", 0, 5, 0,new Sprite(GameAsset.HOE)),
    COPPER("copper hoe", 1, 4, 2000 / 2,new Sprite(GameAsset.COPPER_HOE)),
    IRON("iron hoe", 2, 3, 5000 / 2,new Sprite(GameAsset.STEEL_HOE)),
    GOLD("gold hoe", 3, 2, 10000 / 2,new Sprite(GameAsset.GOLD_HOE)),
    IRIDIUM("iridium hoe", 4, 1, 25000 / 2,new Sprite(GameAsset.IRIDIUM_HOE));

    private final String name;
    private final int level;
    private final int energyCost;
    private final int price;
    private final Sprite sprite;

    Hoe(String name, int level1, int energyUse1, int price,Sprite sprite) {
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
	public String getName() {
		return this.name.toLowerCase();
	}

	@Override
	public int getSellPrice() {
		return this.price;
	}

	@Override
	public Tool getToolByLevel(int level) {
		for (Hoe value : Hoe.values()) {
			if (value.getLevel()==level) {
				return value;
			}
		}
		return null;
	}

	@Override
	public int getLevel() {
		return this.level;
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
		if (tile.getTileType().equals(TileType.PATH) ||
				tile.getTileType().equals(TileType.FENCE) ||
				tile.getTileType().equals(TileType.DOOR)) {
			player.changeEnergy(-this.getEnergy(player));
			return "you can not hoe this kind of tile";
		}
		if (!tile.getIsFilled()) {
			tile.setTileType(TileType.PLOWED);
			player.changeEnergy(-this.getEnergy(player));
			return "you plowed this tile";
		}
		player.changeEnergy(-this.getEnergy(player));
		return "you use this tool in a wrong way";
	}

	@Override
	public Integer getContainingEnergy() {
		return 0;
	}
}
