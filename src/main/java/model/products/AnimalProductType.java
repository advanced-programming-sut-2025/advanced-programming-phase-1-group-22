package model.products;

import java.io.Serializable;

public enum AnimalProductType implements Product, Serializable {
    HEN_EGG("hen egg",50),
    HEN_BIG_EGG("big hen egg",95),
    DUCK_EGG("duck egg",95),
    DUCK_FEATHER("duck feather",250),
    RABBIT_WOOL("rabbit wool",340),
    RABBIT_LEG("rabbit leg",565),
    DINOSAUR_EGG("dinosaur egg",350),
    MILK("milk",125),
    BIG_MILK("big milk",190),
    GOAT_MILK("goat milk",225),
    BIG_GOAT_MILK("big goat milk",345),
    SHEEP_WOOL("sheep wool",340),
    TRUFFLE("truffle",625);

    private String name;
    private Integer price;

    AnimalProductType() {
    }

    AnimalProductType(String name, Integer price) {
        this.name = name;
        this.price = price;
    }

    @Override
    public int getSellPrice() {
        return price;
    }

    @Override
    public int getEnergy() {
        return 0;
    }

    @Override
    public String getName() {
        return this.name.toLowerCase();
    }

    @Override
    public Integer getContainingEnergy() {return getEnergy();}
}
