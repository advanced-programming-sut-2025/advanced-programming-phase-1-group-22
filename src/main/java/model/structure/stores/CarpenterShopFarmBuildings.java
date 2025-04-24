package model.structure.stores;

import lombok.Getter;
import model.products.Product;
import model.records.Response;
import model.shelter.FarmBuildingType;
import model.source.MineralType;

import java.util.Map;
import java.util.Objects;

@Getter
public enum CarpenterShopFarmBuildings implements Shop {
	BARN(FarmBuildingType.BARN,6000,Map.of(MineralType.WOOD,350,MineralType.STONE,150),1),
	BIG_BARN(FarmBuildingType.BIG_BARN,12000,Map.of(MineralType.WOOD,450,MineralType.STONE,200),1),
	DELUXE_BARN(FarmBuildingType.DELUXE_BARN,25000,Map.of(MineralType.WOOD,550,MineralType.STONE,300),1),
	COOP(FarmBuildingType.COOP,4000,Map.of(MineralType.WOOD,300,MineralType.STONE,100),1),
	BIG_COOP(FarmBuildingType.BIG_COOP,10000,Map.of(MineralType.WOOD,400,MineralType.STONE,150),1),
	DELUXE_COOP(FarmBuildingType.DELUXE_COOP,20000,Map.of(MineralType.WOOD,500,MineralType.STONE,200),1),
	WELL(FarmBuildingType.WELL,1000,Map.of(MineralType.STONE,75),1),
	SHIPPING_BIN(FarmBuildingType.SHIPPING_BIN,250,Map.of(MineralType.WOOD,150),-1);

	private final FarmBuildingType farmBuildingType;
	private final Integer price;
	private final Map<Product,Integer> cost;
	private final Integer dailyLimit;
	private Integer dailySold = 0;

	CarpenterShopFarmBuildings(FarmBuildingType farmBuildingType,Integer price, Map<Product, Integer> cost, Integer dailyLimit) {
		this.price = price;
		this.farmBuildingType = farmBuildingType;
		this.cost = cost;
		this.dailyLimit = dailyLimit;
	}
	public static String showAllProducts() {
		StringBuilder res = new StringBuilder("Carpenter Shop:\n");
		for (CarpenterShopFarmBuildings value : CarpenterShopFarmBuildings.values()) {
			res.append(value.toString()).append(" ").append(value.getPrice()).append("$\n");
		}
		return res.toString();
	}
	public static String showAvailableProducts() {
		StringBuilder res = new StringBuilder("Carpenter Shop:\n");
		for (CarpenterShopFarmBuildings value : CarpenterShopFarmBuildings.values()) {
			if (value.dailyLimit != value.dailySold) {
				res.append(value.toString()).append(" ").append(value.getCost()).append("$\n");
			}
		}
		return res.toString();
	}
	public static Response purchase(String name, Integer count) {
		return new Response("Invalid command.");
	}
}
