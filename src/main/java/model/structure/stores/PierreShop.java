package model.structure.stores;

import lombok.Getter;
import model.Salable;
import model.records.Response;

@Getter
public enum PierreShop implements Shop {
    WEDDING_RING("weddingRing",5_000);
    private String name;
    private Integer price;
    private Integer dailySold = 0;
    private Integer dailyLimit;

    PierreShop(String name,int price) {
        this.name = name;
        this.price = price;
    }
    public static String showAllProducts() {
        StringBuilder res = new StringBuilder();
        for (PierreShop value : PierreShop.values()) {
            res.append(value.toString()).append(" ").append(value.getPrice()).append("$\n");
        }
        return res.toString();
    }

    public static String showAvailableProducts() {
        StringBuilder res = new StringBuilder();
        for (PierreShop value : PierreShop.values()) {
            if (value.dailyLimit != value.dailySold) {
                res.append(value.toString()).append(" ").append(value.getPrice()).append("$\n");
            }
        }
        return res.toString();
    }
    public static Response purchase(String name, Integer count) {
        return new Response("TODO"); //TODO
    }


}
