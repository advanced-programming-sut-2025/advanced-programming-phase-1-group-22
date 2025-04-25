package model.structure.stores;

import lombok.Getter;
import model.products.Product;
import model.products.TreesAndFruitsAndSeeds.MadeProductType;
import model.tools.*;

import java.util.Map;
@Getter
public enum BlackSmithUpgrade {
	COPPER_AXE(Axe.COPPER,Map.of(MadeProductType.COPPER_BAR,5),2000,1),
	COPPER_HOE(Hoe.COPPER,Map.of(MadeProductType.COPPER_BAR,5),2000,1),
	COPPER_PICKAXE(Pickaxe.CUPPER,Map.of(MadeProductType.COPPER_BAR,5),2000,1),
	COPPER_TRASHCAN(TrashCan.COPPER,Map.of(MadeProductType.COPPER_BAR,5),2000,1),
	COPPER_WATER_CAN(WateringCanType.CUPPER,Map.of(MadeProductType.COPPER_BAR,5),2000,1),
	COPPER_TRASH_CAN(TrashCan.COPPER,Map.of(MadeProductType.COPPER_BAR,5),1000,1),
	STEEL_AXE(Axe.IRON,Map.of(MadeProductType.IRON_BAR,5),5000,1),
	STEEL_HOE(Hoe.IRON,Map.of(MadeProductType.IRON_BAR,5),5000,1),
	STEEL_PICKAXE(Pickaxe.IRON,Map.of(MadeProductType.IRON_BAR,5),5000,1),
	STEEL_TRASHCAN(TrashCan.IRON,Map.of(MadeProductType.IRON_BAR,5),5000,1),
	STEEL_WATER_CAN(WateringCanType.IRON,Map.of(MadeProductType.IRON_BAR,5),5000,1),
	STEEL_TRASH_CAN(TrashCan.IRON,Map.of(MadeProductType.IRON_BAR,5),2500,1),
	GOLD_AXE(Axe.GOLD,Map.of(MadeProductType.GOLD_BAR,5),10000,1),
	GOLD_HOE(Hoe.GOLD,Map.of(MadeProductType.GOLD_BAR,5),10000,1),
	GOLD_PICKAXE(Pickaxe.GOLD,Map.of(MadeProductType.GOLD_BAR,5),10000,1),
	GOLD_TRASHCAN(TrashCan.GOLD,Map.of(MadeProductType.GOLD_BAR,5),10000,1),
	GOLD_WATER_CAN(WateringCanType.GOLD,Map.of(MadeProductType.GOLD_BAR,5),10000,1),
	GOLD_TRASH_CAN(TrashCan.GOLD,Map.of(MadeProductType.GOLD_BAR,5),5000,1),
	IRIDIUM_AXE(Axe.IRIDIUM,Map.of(MadeProductType.IRIDIUM_BAR,5),25000,1),
	IRIDIUM_HOE(Hoe.IRIDIUM,Map.of(MadeProductType.IRIDIUM_BAR,5),25000,1),
	IRIDIUM_PICKAXE(Pickaxe.IRIDIUM,Map.of(MadeProductType.IRIDIUM_BAR,5),25000,1),
	IRIDIUM_TRASHCAN(TrashCan.IRIDIUM,Map.of(MadeProductType.IRIDIUM_BAR,5),25000,1),
	IRIDIUM_WATER_CAN(WateringCanType.IRIDIUM,Map.of(MadeProductType.IRIDIUM_BAR,5),25000,1),
	IRIDIUM_TRASH_CAN(TrashCan.IRIDIUM,Map.of(MadeProductType.IRIDIUM_BAR,5),12500,1),
	;
	private final Tool tool;
	private final Map<Product,Integer> ingredient;
	private final Integer cost;
	private final Integer dailyLimit;

	BlackSmithUpgrade(Tool tool, Map<Product, Integer> ingredient, Integer cost, Integer dailyLimit) {
		this.tool = tool;
		this.ingredient = ingredient;
		this.cost = cost;
		this.dailyLimit = dailyLimit;
	}

	public static BlackSmithUpgrade getUpgradeByTool(Tool tool){
		for (BlackSmithUpgrade value : BlackSmithUpgrade.values()) {
			if (value.tool.equals(tool)){
				return value;
			}
		}
		return null;
	}
}
