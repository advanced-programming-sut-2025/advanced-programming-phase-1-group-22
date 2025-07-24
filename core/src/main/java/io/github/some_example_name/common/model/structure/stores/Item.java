package io.github.some_example_name.common.model.structure.stores;

import io.github.some_example_name.common.model.Salable;
import lombok.Getter;

import java.util.Map;

@Getter
public class Item {
    private final Salable product;
    private final int price;
    private final int dailyLimit;
    private final boolean available;
    private final Map<Salable,Integer> ingredient;

    public Item(Salable product, int price, int dailyLimit,boolean available,Map<Salable,Integer> ingredient) {
        this.product = product;
        this.price = price;
        this.dailyLimit = dailyLimit;
        this.available = available;
        this.ingredient = ingredient;
    }
}
