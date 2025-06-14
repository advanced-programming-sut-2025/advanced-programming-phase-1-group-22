package model.tools;

import lombok.Getter;
import model.relations.Player;
import model.Salable;
import model.Tile;
import model.products.Product;

@Getter
public enum TrashCan implements Tool {
	NORMAL("normal trashcan", 0, 0, 0),
	COPPER("copper trashcan", 1, 15, 2000 / 2),
	IRON("iron trashcan", 2, 30, 5000 / 2),
	GOLD("gold trashcan", 3, 45, 10000 / 2),
	IRIDIUM("iridium trashcan", 4, 60, 25000 / 2);

	private final String name;
	private final Integer level;
	private final Integer prunedValue;
	private final Integer price;

	TrashCan(String name, int level, int prunedValue, int price) {
		this.name = name;
		this.level = level;
		this.prunedValue = prunedValue;
		this.price = price;
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