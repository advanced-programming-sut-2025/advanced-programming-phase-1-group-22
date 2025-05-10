package model;

public class Flower implements Salable {
    private String name = "flower";

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
