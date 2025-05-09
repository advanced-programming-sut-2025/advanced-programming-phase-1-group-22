package model;

import java.io.Serializable;

public class Flower implements Salable, Serializable {
    private String name = "flower";

    public Flower() {
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public int getSellPrice() {
        return 10;
    }

    @Override
    public Integer getContainingEnergy() {return 0;}
}
