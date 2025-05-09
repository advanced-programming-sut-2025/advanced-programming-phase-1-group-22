package model.products;

import lombok.Getter;
import model.Salable;

import java.io.Serializable;

@Getter
public class Hay implements Salable, Serializable {
    private final Integer price = 50 / 2;

    public Hay() {
    }

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