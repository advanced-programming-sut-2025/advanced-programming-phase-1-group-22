package model.structure.stores;

import lombok.Getter;
import model.records.Response;
import model.relations.Player;
import model.source.MineralType;
import utils.App;

@Getter
public enum CarpenterShopMineralStuff implements Shop {
	WOOD(MineralType.WOOD,10,-1),
	STONE(MineralType.STONE,20,-1);

	private final MineralType mineralType;
	private final Integer price;
	private final Integer dailyLimit;
	private Integer dailySold = 0;

	CarpenterShopMineralStuff(MineralType mineralType, Integer price, Integer dailyLimit) {
		this.mineralType = mineralType;
		this.price = price;
		this.dailyLimit = dailyLimit;
	}
	public static String showAllProducts() {
		StringBuilder res = new StringBuilder("Black Smith:\n");
		for (CarpenterShopMineralStuff value : CarpenterShopMineralStuff.values()) {
			res.append(value.toString()).append(" ").append(value.getPrice()).append("$\n");
		}
		return res.toString();
	}
	public static String showAvailableProducts() {
		StringBuilder res = new StringBuilder("Black Smith:\n");
		for (CarpenterShopMineralStuff value : CarpenterShopMineralStuff.values()) {
			if (value.dailyLimit != value.dailySold) {
				res.append(value.toString()).append(" ").append(value.getPrice()).append("$\n");
			}
		}
		return res.toString();
	}
	public static Response purchase(String name, Integer count) {
		CarpenterShopMineralStuff salable = null;
		for (CarpenterShopMineralStuff value : CarpenterShopMineralStuff.values()) {
			if(value.getMineralType().getName().equals(name)) {
				salable = value;
			}
		}
		if (salable == null) return null;
		Player player = App.getInstance().getCurrentGame().getCurrentPlayer();
		if (!player.getInventory().isInventoryHaveCapacity(salable.getMineralType())) {
			return new Response("Not enough space in your backpack.");
		}
		if (player.getAccount().getGolds() < salable.getPrice()) {
			return new Response("Not enough golds");
		}
		player.getAccount().removeGolds(salable.getPrice());
		salable.dailySold += count;
		player.getInventory().addProductToBackPack(salable.mineralType, count);
		return new Response("Bought successfully", true);
	}

	public void resetDailySold() {
		dailySold = 0;
	}
}
