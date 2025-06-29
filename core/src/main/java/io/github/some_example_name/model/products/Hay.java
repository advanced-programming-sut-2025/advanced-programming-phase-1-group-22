package io.github.some_example_name.model.products;

import lombok.Getter;
import io.github.some_example_name.model.Salable;
@Getter
public class Hay implements Salable {
    private final Integer price = 50 / 2;

    @Override
    public String getName() {
        return "hay";
    }

    @Override
    public int getSellPrice() {
        return this.price;
    }

    @Override
    public Integer getContainingEnergy() {return 0;}
}
