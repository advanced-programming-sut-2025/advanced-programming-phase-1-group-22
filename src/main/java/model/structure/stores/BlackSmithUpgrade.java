package model.structure.stores;

import lombok.Getter;
import model.Salable;
import model.products.Product;
import model.products.TreesAndFruitsAndSeeds.MadeProductType;
import model.records.Response;
import model.relations.Player;
import model.tools.*;
import utils.App;

import java.util.Map;

@Getter
public enum BlackSmithUpgrade implements Shop {
    COPPER_AXE(Axe.COPPER, ()->Map.of(MadeProductType.COPPER_BAR, 5), 2000, 1),
    COPPER_HOE(Hoe.COPPER, ()->Map.of(MadeProductType.COPPER_BAR, 5), 2000, 1),
    COPPER_PICKAXE(Pickaxe.COPPER, ()->Map.of(MadeProductType.COPPER_BAR, 5), 2000, 1),
    COPPER_TRASHCAN(TrashCan.COPPER, ()->Map.of(MadeProductType.COPPER_BAR, 5), 2000, 1),
    COPPER_WATER_CAN(WateringCanType.CUPPER, ()->Map.of(MadeProductType.COPPER_BAR, 5), 2000, 1),
    COPPER_TRASH_CAN(TrashCan.COPPER, ()->Map.of(MadeProductType.COPPER_BAR, 5), 1000, 1),
    STEEL_AXE(Axe.IRON, ()->Map.of(MadeProductType.IRON_BAR, 5), 5000, 1),
    STEEL_HOE(Hoe.IRON, ()->Map.of(MadeProductType.IRON_BAR, 5), 5000, 1),
    STEEL_PICKAXE(Pickaxe.IRON, ()->Map.of(MadeProductType.IRON_BAR, 5), 5000, 1),
    STEEL_TRASHCAN(TrashCan.IRON, ()->Map.of(MadeProductType.IRON_BAR, 5), 5000, 1),
    STEEL_WATER_CAN(WateringCanType.IRON, ()->Map.of(MadeProductType.IRON_BAR, 5), 5000, 1),
    STEEL_TRASH_CAN(TrashCan.IRON, ()->Map.of(MadeProductType.IRON_BAR, 5), 2500, 1),
    GOLD_AXE(Axe.GOLD, ()->Map.of(MadeProductType.GOLD_BAR, 5), 10000, 1),
    GOLD_HOE(Hoe.GOLD, ()->Map.of(MadeProductType.GOLD_BAR, 5), 10000, 1),
    GOLD_PICKAXE(Pickaxe.GOLD, ()->Map.of(MadeProductType.GOLD_BAR, 5), 10000, 1),
    GOLD_TRASHCAN(TrashCan.GOLD, ()->Map.of(MadeProductType.GOLD_BAR, 5), 10000, 1),
    GOLD_WATER_CAN(WateringCanType.GOLD, ()->Map.of(MadeProductType.GOLD_BAR, 5), 10000, 1),
    GOLD_TRASH_CAN(TrashCan.GOLD, ()->Map.of(MadeProductType.GOLD_BAR, 5), 5000, 1),
    IRIDIUM_AXE(Axe.IRIDIUM, ()->Map.of(MadeProductType.IRIDIUM_BAR, 5), 25000, 1),
    IRIDIUM_HOE(Hoe.IRIDIUM, ()->Map.of(MadeProductType.IRIDIUM_BAR, 5), 25000, 1),
    IRIDIUM_PICKAXE(Pickaxe.IRIDIUM, ()->Map.of(MadeProductType.IRIDIUM_BAR, 5), 25000, 1),
    IRIDIUM_TRASHCAN(TrashCan.IRIDIUM, ()->Map.of(MadeProductType.IRIDIUM_BAR, 5), 25000, 1),
    IRIDIUM_WATER_CAN(WateringCanType.IRIDIUM, ()->Map.of(MadeProductType.IRIDIUM_BAR, 5), 25000, 1),
    IRIDIUM_TRASH_CAN(TrashCan.IRIDIUM, ()->Map.of(MadeProductType.IRIDIUM_BAR, 5), 12500, 1);
    private final Tool tool;
    private Integer dailySold = 0;
    private final Integer cost;
    private final Integer dailyLimit;

    @FunctionalInterface
    private interface IngredientsSupplier {
        Map<Salable, Integer> get();
    }

    private transient volatile Map<Salable, Integer> ingredient;
    private final BlackSmithUpgrade.IngredientsSupplier ingredientsSupplier;

    public Map<Salable, Integer> getIngredients() {
        if (ingredient == null) {
            ingredient = ingredientsSupplier.get();
        }
        return ingredient;
    }

    BlackSmithUpgrade(Tool tool, IngredientsSupplier ingredient, Integer cost, Integer dailyLimit) {
        this.tool = tool;
        this.ingredientsSupplier = ingredient;
        this.cost = cost;
        this.dailyLimit = dailyLimit;
    }

    public static BlackSmithUpgrade getUpgradeByTool(Tool tool) {
        for (BlackSmithUpgrade value : BlackSmithUpgrade.values()) {
            if (value.tool.equals(tool)) {
                return value;
            }
        }
        return null;
    }

    public static String showAllProducts() {
        StringBuilder res = new StringBuilder();
        for (BlackSmithUpgrade value : BlackSmithUpgrade.values()) {
            res.append(value.toString()).append(" ").append(value.getCost()).append("$\n");
        }
        return res.toString();
    }

    public static String showAvailableProducts() {
        StringBuilder res = new StringBuilder();
        for (BlackSmithUpgrade value : BlackSmithUpgrade.values()) {
            if (value.dailyLimit != value.dailySold) {
                res.append(value.toString()).append(" ").append(value.getCost()).append("$\n");
            }
        }
        return res.toString();
    }

    public static Response purchase(String name, Integer count) { return null;
//        BlackSmithUpgrade salable = null;
//        for (BlackSmithUpgrade value : BlackSmithUpgrade.values()) {
//            if (value.getTool().getName().equalsIgnoreCase(name)) {
//                salable = value;
//            }
//        }
//        if (salable == null) return new Response("Item not found");
//        if (salable.dailyLimit != -1 && salable.dailyLimit < salable.dailySold + count) {
//            return new Response("Not enough in stock");
//        }
//        Player player = App.getInstance().getCurrentGame().getCurrentPlayer();
//        for (Map.Entry<Salable,Integer> productIntegerEntry : salable.getIngredients().entrySet()) {
//            if (!player.getInventory().checkProductAvailabilityInBackPack(productIntegerEntry.getKey().getName(),
//                    productIntegerEntry.getValue())) {
//                return new Response("Ingredients not found.");
//            }
//        }
//
//        if (!player.getInventory().checkProductAvailabilityInBackPack(null, 1)) {
//            return new Response("Not enough space in your backpack."); //TODO checking the last level tool
//        }
//        if (player.getAccount().getGolds() < salable.getCost()) {
//            return new Response("Not enough golds");
//        }
//        player.getAccount().removeGolds(salable.getCost());
//        salable.dailySold += count;
//        player.getInventory().addProductToBackPack(salable.tool, count);
//        return new Response("Bought successfully", true);
    }

    public void resetDailySold() {
        dailySold = 0;
    }
}
