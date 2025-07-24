package io.github.some_example_name.common.model.structure.stores;

import lombok.Getter;
import io.github.some_example_name.common.model.records.Response;
import io.github.some_example_name.common.model.relations.Player;
import io.github.some_example_name.common.model.source.Mineral;
import io.github.some_example_name.common.model.source.MineralType;
import io.github.some_example_name.common.utils.App;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
public enum BlackSmithStuff implements Shop {
    COAL(MineralType.COAL, 150, -1),
    IRON_ORE(MineralType.IRON_ORE, 150, -1),
    COPPER_ORE(MineralType.COPPER_ORE, 75, -1),
    GOLD_ORE(MineralType.GOLD_ORE, 400, -1);
    private final MineralType mineralType;
    private final Integer price;
    private final Integer dailyLimit;
    private Integer dailySold = 0;

    BlackSmithStuff(MineralType mineralType, Integer price, Integer dailyLimit) {
        this.mineralType = mineralType;
        this.price = price;
        this.dailyLimit = dailyLimit;
    }

    public static List<Item> getItems(){
        List<Item> items = new ArrayList<>();
        for (BlackSmithStuff value : BlackSmithStuff.values()) {
            boolean available = !Objects.equals(value.dailyLimit, value.dailySold);
            items.add(new Item(value.mineralType,value.price, value.dailyLimit,available,null));
        }
        return items;
    }

    public static String showAllProducts() {
        StringBuilder res = new StringBuilder("Black Smith:\n");
        for (BlackSmithStuff value : BlackSmithStuff.values()) {
            res.append(value.toString()).append(" ").append(value.getPrice()).append("$\n");
        }
        return res.toString();
    }
    public static String showAvailableProducts() {
        StringBuilder res = new StringBuilder("Black Smith:\n");
        for (BlackSmithStuff value : BlackSmithStuff.values()) {
            if (value.dailyLimit != value.dailySold) {
                res.append(value.toString()).append(" ").append(value.getPrice()).append("$\n");
            }
        }
        return res.toString();
    }
    public static Response purchase(String name, Integer count) {
        BlackSmithStuff salable = null;
        for (BlackSmithStuff value : BlackSmithStuff.values()) {
            if(value.getMineralType().getName().equalsIgnoreCase(name)) {
                salable = value;
            }
        }
        if (salable == null) return new Response("Item not found");
        if (salable.dailyLimit != -1 && salable.dailyLimit < salable.dailySold + count) {
            return new Response("Not enough in stock");
        }
        Player player = App.getInstance().getCurrentGame().getCurrentPlayer();
        if (!player.getInventory().isInventoryHaveCapacity(new Mineral(salable.getMineralType()))) {
            return new Response("Not enough space in your backpack.");
        }
        if (player.getAccount().getGolds() < salable.getPrice()) {
            return new Response("Not enough golds");
        }
        player.getAccount().removeGolds(salable.getPrice());
        salable.dailySold += count;
        player.getInventory().addProductToBackPack(new Mineral(salable.mineralType), count);
        return new Response("Bought successfully", true);
    }

    public void resetDailySold() {
        dailySold = 0;
    }
}
