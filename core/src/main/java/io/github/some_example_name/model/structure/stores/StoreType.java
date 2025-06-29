package io.github.some_example_name.model.structure.stores;

import lombok.Getter;
import io.github.some_example_name.model.records.Response;

import java.lang.ref.PhantomReference;
import java.lang.reflect.InvocationTargetException;

@Getter
public enum StoreType {
    BLACK_SMITH("Clint",9,16, BlackSmithStuff.class, BlackSmithUpgrade.class),
    CARPENTER_SHOP("Robin",9,20, CarpenterShopFarmBuildings.class, CarpenterShopMineralStuff.class),
    FISH_SHOP("Willy",9,17, FishShopStuff.class, null),
    JOJA_MART("Morris",9,23, JojaMartShopSeed.class, null),
    PIERRE_SHOP("Pierre",9,17, PierreShop.class, null),
    STARDROPSALON("Gus",12,24, TheStardropSaloonStuff.class, null),
    MARNIE_SHOP("Marnie",9,16, MarnieShopAnimal.class, MarnieShopAnimalRequierment.class);
    private final String shopperName;
    private final Integer openDoorTime;
    private final Integer closeDoorTime;
    private final Class<? extends Enum<?>> shop1;
    private final Class<? extends Enum<?>> shop2;


    StoreType(String shopperName, Integer openDoorTime, Integer closeDoorTime, Class<? extends Enum<?>> shop1, Class<? extends Enum<?>> shop2) {
        this.shopperName = shopperName;
        this.openDoorTime = openDoorTime;
        this.closeDoorTime = closeDoorTime;
        this.shop1 = shop1;
        this.shop2 = shop2;
    }

    public Response showAllProducts() {
        String res = "";
        try {
            res += shop1.getMethod("showAllProducts").invoke(null);
        } catch (Exception ignored) {}
        if (shop2 != null) {
            try {
                res += shop2.getMethod("showAllProducts").invoke(null);
            } catch (Exception ignored) {}
        }
        return new Response(res, true);
    }
    public Response showAvailableProducts() {
        String res = "";
        try {
            res += shop1.getMethod("showAvailableProducts").invoke(null);
        } catch (Exception ignored) {}
        if (shop2 != null) {
            try {
                res += shop2.getMethod("showAvailableProducts").invoke(null);
            } catch (Exception ignored) {}
        }
        return new Response(res, true);
    }
    public Response purchase(String name, Integer count) {
        Response response = null;
        try {
            response = (Response) shop1.getMethod("purchase", String.class, Integer.class).invoke(null, name, count);
        } catch (Exception e) {
            int b = 5;
        }
        if (response != null) return response;
        if (shop2 != null) {
            try {
                response = (Response) shop2.getMethod("purchase", String.class, Integer.class).invoke(null, name, count);
            } catch (Exception ignored) {}
        }
        return response;
    }
}
