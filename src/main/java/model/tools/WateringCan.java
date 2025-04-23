package model.tools;

import lombok.Getter;
import lombok.Setter;
import model.relations.Player;
import model.Tile;

@Getter
@Setter
public class WateringCan implements Tool{
	private WateringCanType wateringCanType;
	private Boolean isFullOfWater;

	public WateringCan(WateringCanType wateringCanType) {
		this.wateringCanType = wateringCanType;
	}

	@Override
	public void addToolEfficiency(double efficiency) {

	}

	@Override
	public Tool getToolByLevel(int level) {
		return wateringCanType.getToolByLevel(level);
	}

	@Override
	public int getLevel() {
		return wateringCanType.getLevel();
	}

	@Override
	public int getEnergy(Player player) {
		return wateringCanType.getEnergy(player);
	}

	@Override
	public String useTool(Player player, Tile tile) {
		//Green house //TODO
		String message = wateringCanType.useTool(player,tile);
		isFullOfWater = wateringCanType.getIsFullOfWater();
		return message;
	}

	@Override
	public String getName() {
		return wateringCanType.getName();
	}

	@Override
	public int getSellPrice() {
		return wateringCanType.getSellPrice();
	}


}
