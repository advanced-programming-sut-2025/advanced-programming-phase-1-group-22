package model.structure.stores;

import model.Salable;

public enum PierreShop implements Salable {
    WEDDING_RING("weddingRing",5_000);
    private String name;
    private Integer price;

    PierreShop(String name,int price) {
        this.name = name;
        this.price = price;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public int getSellPrice() {
        return price;
    }
}
