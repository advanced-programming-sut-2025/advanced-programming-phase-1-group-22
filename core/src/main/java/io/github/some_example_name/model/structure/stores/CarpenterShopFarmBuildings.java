package io.github.some_example_name.model.structure.stores;

import io.github.some_example_name.model.Salable;
import lombok.Getter;
import io.github.some_example_name.model.products.Product;
import io.github.some_example_name.model.records.Response;
import io.github.some_example_name.model.shelter.FarmBuildingType;
import io.github.some_example_name.model.source.MineralType;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Getter
public enum CarpenterShopFarmBuildings {
	BARN("barn",FarmBuildingType.BARN,6000,Map.of(MineralType.WOOD,350,MineralType.STONE,150),1),
	BIG_BARN("big barn",FarmBuildingType.BIG_BARN,12000,Map.of(MineralType.WOOD,450,MineralType.STONE,200),1),
	DELUXE_BARN("deluxe barn",FarmBuildingType.DELUXE_BARN,25000,Map.of(MineralType.WOOD,550,MineralType.STONE,300),1),
	COOP("coop",FarmBuildingType.COOP,4000,Map.of(MineralType.WOOD,300,MineralType.STONE,100),1),
	BIG_COOP("big coop",FarmBuildingType.BIG_COOP,10000,Map.of(MineralType.WOOD,400,MineralType.STONE,150),1),
	DELUXE_COOP("deluxe coop",FarmBuildingType.DELUXE_COOP,20000,Map.of(MineralType.WOOD,500,MineralType.STONE,200),1),
	WELL("well",FarmBuildingType.WELL,1000,Map.of(MineralType.STONE,75),1),
	SHIPPING_BIN("shipping bin",FarmBuildingType.SHIPPING_BIN,250,Map.of(MineralType.WOOD,150),-1);

	private final String name;
	private final FarmBuildingType farmBuildingType;
	private final Integer price;
	private final Map<Salable,Integer> cost;
	private final Integer dailyLimit;
	private Integer dailySold = 0;

	CarpenterShopFarmBuildings(String name,FarmBuildingType farmBuildingType,Integer price, Map<Salable, Integer> cost, Integer dailyLimit) {
		this.name = name;
		this.price = price;
		this.farmBuildingType = farmBuildingType;
		this.cost = cost;
		this.dailyLimit = dailyLimit;
	}

    public static List<Item> getItems(){
        List<Item> items = new ArrayList<>();
        for (CarpenterShopFarmBuildings value : CarpenterShopFarmBuildings.values()) {
            boolean available = !Objects.equals(value.dailyLimit, value.dailySold);
            items.add(new Item(value.farmBuildingType,value.price, value.dailyLimit,available,value.cost));
        }
        return items;
    }

	public static CarpenterShopFarmBuildings getFromName(String name){
		for (CarpenterShopFarmBuildings value : CarpenterShopFarmBuildings.values()) {
			if (value.name.equalsIgnoreCase(name)){
				return value;
			}
		}
		return null;
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
		return null;
	}

	public void resetDailySold() {
		dailySold = 0;
	}
}
