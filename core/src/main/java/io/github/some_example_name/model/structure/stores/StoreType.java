package io.github.some_example_name.model.structure.stores;

import com.badlogic.gdx.graphics.Texture;
import io.github.some_example_name.utils.GameAsset;
import lombok.Getter;
import io.github.some_example_name.model.records.Response;

import java.lang.ref.PhantomReference;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

@Getter
public enum StoreType {
    BLACK_SMITH("BlackSmith","Clint",9,16,
        BlackSmithStuff.class, BlackSmithUpgrade.class, GameAsset.BLACKSMITH,GameAsset.BLACKSMITH_INTERIOR,GameAsset.CLINT),
    CARPENTER_SHOP("Carpenter Shop","Robin",9,20,
        CarpenterShopFarmBuildings.class, CarpenterShopMineralStuff.class,GameAsset.CARPENTER,GameAsset.CARPENTER_INTERIOR,GameAsset.ROBIN),
    FISH_SHOP("Fish Shop","Willy",9,17,
        FishShopStuff.class, null,GameAsset.FISH_SHOP,GameAsset.FISH_SHOP_INTERIOR,GameAsset.WILLY),
    JOJA_MART("JojaMart","Morris",9,23,
        JojaMartShopSeed.class, null,GameAsset.JOJAMART,GameAsset.JOJAMART_INTERIOR,GameAsset.MORRIS),
    PIERRE_SHOP("Pierre Shop","Pierre",9,17,
        PierreShop.class, null,GameAsset.PIERRE_SHOP,GameAsset.PIERRE_SHOP_INTERIOR,GameAsset.PIERRE),
    STARDROPSALON("Star Drop Saloon","Gus",12,24,
        TheStardropSaloonStuff.class, null,GameAsset.SALOON_SHOP,GameAsset.SALOON_SHOP_INTERIOR,GameAsset.GUS),
    MARNIE_SHOP("Marnie Shop","Marnie",9,16,
        MarnieShopAnimal.class, MarnieShopAnimalRequierment.class,GameAsset.MARINE_SHOP,GameAsset.MARINE_SHOP_INTERIOR,GameAsset.MARNIE);

    private final String storeName;
    private final String shopperName;
    private final Integer openDoorTime;
    private final Integer closeDoorTime;
    private final Class<? extends Enum<?>> shop1;
    private final Class<? extends Enum<?>> shop2;
    private final Texture texture;
    private final Texture textureInterior;
    private final Texture shopperTexture;


    StoreType(String storeName,String shopperName, Integer openDoorTime, Integer closeDoorTime,
              Class<? extends Enum<?>> shop1, Class<? extends Enum<?>> shop2,Texture texture,Texture textureInterior,Texture shopperTexture) {
        this.storeName = storeName;
        this.shopperName = shopperName;
        this.openDoorTime = openDoorTime;
        this.closeDoorTime = closeDoorTime;
        this.shop1 = shop1;
        this.shop2 = shop2;
        this.texture = texture;
        this.textureInterior = textureInterior;
        this.shopperTexture = shopperTexture;
    }

    public List<Item> getItems(){
        List<Item> items = new ArrayList<>();
        try {
            items = (List<Item>) shop1.getMethod("getItems").invoke(null);
        } catch (Exception ignored) {}
        if (shop2 != null){
           try {
               List<Item> moreItems = (List<Item>) shop2.getMethod("getItems").invoke(null);
               items.addAll(moreItems);
           } catch (Exception ignored) {}
        }
        return items;
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
