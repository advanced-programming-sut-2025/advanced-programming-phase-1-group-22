package model.products;

import lombok.Getter;
import model.Salable;
@Getter
public class Hay implements Salable {
    private final Integer price = 50 / 2;

    @Override
    public String getName() {
        return "hay";
    }

    @Override
    public int getSellPrice() {
        return price;
    }

    @Override
    public Integer getContainingEnergy() {return 0;}
}