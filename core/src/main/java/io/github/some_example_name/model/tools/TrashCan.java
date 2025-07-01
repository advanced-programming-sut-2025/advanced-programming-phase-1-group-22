package io.github.some_example_name.model.tools;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import io.github.some_example_name.utils.App;
import io.github.some_example_name.utils.GameAsset;
import lombok.Getter;
import io.github.some_example_name.model.relations.Player;
import io.github.some_example_name.model.Salable;
import io.github.some_example_name.model.Tile;
import io.github.some_example_name.model.products.Product;

@Getter
public enum TrashCan implements Tool {
    NORMAL("normal trashcan", 0, 0, 0,
        new Sprite(GameAsset.WORM_BIN),GameAsset.WORM_BIN),
    COPPER("copper trashcan", 1, 15, 2000 / 2,
        new Sprite(GameAsset.TRASH_CAN_COPPER),GameAsset.TRASH_CAN_COPPER),
    IRON("iron trashcan", 2, 30, 5000 / 2,
        new Sprite(GameAsset.TRASH_CAN_STEEL),GameAsset.TRASH_CAN_STEEL),
    GOLD("gold trashcan", 3, 45, 10000 / 2,
        new Sprite(GameAsset.TRASH_CAN_GOLD),GameAsset.TRASH_CAN_GOLD),
    IRIDIUM("iridium trashcan", 4, 60, 25000 / 2,
        new Sprite(GameAsset.TRASH_CAN_IRIDIUM),GameAsset.TRASH_CAN_IRIDIUM);

    private final String name;
    private final Integer level;
    private final Integer prunedValue;
    private final Integer price;
    private final Texture texture;
    private final Sprite sprite;

    TrashCan(String name, int level, int prunedValue, int price,Sprite sprite,Texture texture) {
        this.name = name;
        this.level = level;
        this.prunedValue = prunedValue;
        this.price = price;
        this.sprite = sprite;
        this.sprite.setSize(App.tileWidth,App.tileHeight);
        this.texture = texture;
    }

	public void addSalableToTrashCan(Product product) {

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
		for (TrashCan value : TrashCan.values()) {
			if (value.getLevel()==level) {
				return value;
			}
		}
		return null;
	}

	@Override
	public int getEnergy(Player player) {
		return 0;
	}

	@Override
	public int getLevel() {
		return this.level;
	}

	@Override
	public String useTool(Player player, Tile tile) {
		return "";
	}

	public void givePlayerProductPrice(Player player, Salable salable, int itemNumber) {
		int price = salable.getSellPrice() * itemNumber * (this.prunedValue / 100);
		int oldGold = player.getAccount().getGolds();
		player.getAccount().setGolds(oldGold + price);
	}


	@Override
	public Integer getContainingEnergy() {
		return 0;
	}
}
