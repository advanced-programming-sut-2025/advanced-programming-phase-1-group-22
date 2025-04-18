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
    COW(1_500, List.of(AnimalProductType.BIG_MILK, AnimalProductType.MILK), 1, true),
    DINOSAUR(1_400, List.of(AnimalProductType.DINOSAUR_EGG), 7, false),
    DUCK(1_200, List.of(AnimalProductType.DUCK_EGG, AnimalProductType.DUCK_FEATHER), 2, false),
    GOAT(4_000, List.of(AnimalProductType.BIG_GOAT_MILK, AnimalProductType.GOAT_MILK), 2, true),
    HEN(800, List.of(AnimalProductType.HEN_EGG, AnimalProductType.HEN_BIG_EGG), 1, false),
    PIG(16_000, List.of(AnimalProductType.TRUFFLE), 0, true),
    RABBIT(8_000, List.of(AnimalProductType.RABBIT_LEG, AnimalProductType.RABBIT_WOOL), 4, false),
    SHEEP(8_000, List.of(AnimalProductType.SHEEP_WOOL), 3, true);
    private final Integer productPeriod;
    private final Integer price;
    private final List<AnimalProductType> productList;
    private final Hay food = new Hay();
    private final Boolean isBarnAnimal;

    AnimalType(int price, List<AnimalProductType> productList, int productPeriod, boolean isBarnAnimal) {
        this.price = price;
        this.productList = productList;
        this.productPeriod = productPeriod;
        this.isBarnAnimal = isBarnAnimal;
    }

    @Override
    public int getSellPrice() {
        return this.price;
    }
}

