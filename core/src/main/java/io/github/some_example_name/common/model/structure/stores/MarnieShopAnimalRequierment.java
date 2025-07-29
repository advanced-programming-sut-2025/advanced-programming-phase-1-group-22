package io.github.some_example_name.common.model.structure.stores;

import io.github.some_example_name.client.GameClient;
import lombok.Getter;
import io.github.some_example_name.common.model.Salable;
import io.github.some_example_name.common.model.products.Hay;
import io.github.some_example_name.common.model.records.Response;
import io.github.some_example_name.common.model.relations.Player;
import io.github.some_example_name.common.model.tools.MilkPail;
import io.github.some_example_name.common.model.tools.Shear;
import io.github.some_example_name.common.utils.App;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
public enum MarnieShopAnimalRequierment  implements Shop{
    HAY(new Hay(),50,-1),
    MILK_pAIL(MilkPail.getInstance(),1000,1),
    SHEARS(Shear.SHEAR,1000,1);

    private final Salable product;
    private final Integer price;
    private final Integer dailyLimit;
    private Integer dailySold = 0;

    MarnieShopAnimalRequierment(Salable product, Integer price, Integer dailyLimit) {
        this.product = product;
        this.price = price;
        this.dailyLimit = dailyLimit;
    }

    public static List<Item> getItems(){
        List<Item> items = new ArrayList<>();
        for (MarnieShopAnimalRequierment value : MarnieShopAnimalRequierment.values()) {
            boolean available = value.isAvailable();
            items.add(new Item(value.product,value.price, value.dailyLimit,value.dailySold,available,null,value));
        }
        return items;
    }

    public boolean isAvailable() {
        return !Objects.equals(this.dailyLimit, this.dailySold);
    }
    public static String showAllProducts() {
        StringBuilder res = new StringBuilder();
        for (MarnieShopAnimalRequierment value : MarnieShopAnimalRequierment.values()) {
            res.append(value.toString()).append(" ").append(value.getPrice()).append("$\n");
        }
        return res.toString();
    }
    public static String showAvailableProducts() {
        StringBuilder res = new StringBuilder();
        for (MarnieShopAnimalRequierment value : MarnieShopAnimalRequierment.values()) {
            if (value.isAvailable()) {
                res.append(value.toString()).append(" ").append(value.getPrice()).append("$\n");
            }
        }
        return res.toString();
    }
    public static Response purchase(String name, Integer count) {
        MarnieShopAnimalRequierment salable = null;
        for (MarnieShopAnimalRequierment value : MarnieShopAnimalRequierment.values()) {
            if(value.getProduct().getName().equalsIgnoreCase(name)) {
                salable = value;
            }
        }
        if (salable == null) return new Response("Item not found");
        if (!salable.isAvailable()) {
            return new Response("This product isn't available now. Come back later.");
        }
        if (salable.dailyLimit != -1 && salable.dailyLimit < salable.dailySold + count) {
            return new Response("Not enough in stock");
        }
        Player player = App.getInstance().getCurrentGame().getCurrentPlayer();
        if (!player.getInventory().isInventoryHaveCapacity(salable.getProduct())) {
            return new Response("Not enough space in your backpack.");
        }
        if (player.getAccount().getGolds() < salable.getPrice()) {
            return new Response("Not enough golds");
        }
        player.getAccount().removeGolds(salable.getPrice());
        GameClient.getInstance().updatePlayerGold(player);
        salable.dailySold += count;
        player.getInventory().addProductToBackPack(salable.getProduct(), count);
        return new Response("Bought successfully", true);
    }

    public void resetDailySold() {
        dailySold = 0;
    }

    public void increaseDailySold(int amount){
        dailySold += amount;
    }
}
