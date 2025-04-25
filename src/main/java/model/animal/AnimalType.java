package model.animal;

import lombok.*;
import model.products.AnimalProductType;
import model.products.Hay;
import model.products.Product;
import model.structure.Structure;
import model.Salable;

import java.util.List;

@Getter
@ToString
public enum AnimalType implements Product {
    COW("cow",1_500, List.of(AnimalProductType.BIG_MILK, AnimalProductType.MILK), 1, true),
    DINOSAUR("dinosaur",1_400, List.of(AnimalProductType.DINOSAUR_EGG), 7, false),
    DUCK("duck",1_200, List.of(AnimalProductType.DUCK_EGG, AnimalProductType.DUCK_FEATHER), 2, false),
    GOAT("goat",4_000, List.of(AnimalProductType.BIG_GOAT_MILK, AnimalProductType.GOAT_MILK), 2, true),
    HEN("hen",800, List.of(AnimalProductType.HEN_EGG, AnimalProductType.HEN_BIG_EGG), 1, false),
    PIG("pig",16_000, List.of(AnimalProductType.TRUFFLE), 0, true),
    RABBIT("rabbit",8_000, List.of(AnimalProductType.RABBIT_LEG, AnimalProductType.RABBIT_WOOL), 4, false),
    SHEEP("sheep",8_000, List.of(AnimalProductType.SHEEP_WOOL), 3, true);

    private final String name;
    private final Integer productPeriod;
    private final Integer price;
    private final List<AnimalProductType> productList;
    private final Hay food = new Hay();
    private final Boolean isBarnAnimal;

    AnimalType(String name,int price, List<AnimalProductType> productList, int productPeriod, boolean isBarnAnimal) {
        this.name = name;
        this.price = price;
        this.productList = productList;
        this.productPeriod = productPeriod;
        this.isBarnAnimal = isBarnAnimal;
    }

    @Override
    public int getSellPrice() {
        return this.price;
    }

    @Override
    public int getEnergy() {
        return 0;
    }

    @Override
    public String getName() {
        return this.name.toLowerCase();
    }
}

