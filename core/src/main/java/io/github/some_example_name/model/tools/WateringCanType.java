package io.github.some_example_name.model.tools;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import io.github.some_example_name.utils.GameAsset;
import lombok.Getter;
import io.github.some_example_name.model.relations.Player;
import io.github.some_example_name.model.Tile;
import io.github.some_example_name.model.abilitiy.Ability;
import io.github.some_example_name.utils.App;

@Getter

public enum WateringCanType implements Tool {
    NORMAL("normal wateringCan", 0, 40, 5, 0,
        new Sprite(GameAsset.WATERING_CAN),GameAsset.WATERING_CAN),
    COPPER("copper wateringCan", 1, 55, 4, 2000 / 2,
        new Sprite(GameAsset.COPPER_WATERING_CAN),GameAsset.COPPER_WATERING_CAN),
    IRON("iron wateringCan", 2, 70, 3, 5000 / 2,
        new Sprite(GameAsset.STEEL_WATERING_CAN),GameAsset.STEEL_WATERING_CAN),
    GOLD("gold wateringCan", 3, 85, 2, 10000 / 2,
        new Sprite(GameAsset.GOLD_WATERING_CAN),GameAsset.GOLD_WATERING_CAN),
    IRIDIUM("iridium wateringCan", 4, 100, 1, 25000 / 2,
        new Sprite(GameAsset.IRIDIUM_WATERING_CAN),GameAsset.IRIDIUM_WATERING_CAN);

    private final String name;
    private final Integer level;
    private final Integer capacity;
    private final Integer energyCost;
    private final Integer price;
    private final Texture texture;
    private final Sprite sprite;

    WateringCanType(String name, Integer level, Integer capacity, Integer energyCost, int price, Sprite sprite,Texture texture) {
        this.name = name;
        this.level = level;
        this.capacity = capacity;
        this.energyCost = energyCost;
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
	public Tool getToolByLevel(int level) {
		for (WateringCanType value : WateringCanType.values()) {
			if (value.getLevel()==level) {
				return value;
			}
		}
		return null;
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
	public int getLevel() {
		return this.level;
	}

	@Override
	public String useTool(Player player, Tile tile) {
		return "";
	}


	@Override
	public Integer getContainingEnergy() {
		return 0;
	}
}
