package io.github.some_example_name.model.animal;

import com.badlogic.gdx.graphics.Texture;
import io.github.some_example_name.utils.GameAsset;
import lombok.*;
import io.github.some_example_name.model.products.AnimalProductType;
import io.github.some_example_name.model.products.Hay;
import io.github.some_example_name.model.products.Product;

import java.util.List;

@Getter
@ToString
public enum AnimalType implements Product {
    COW("cow",1_500, List.of(AnimalProductType.BIG_MILK, AnimalProductType.MILK), 1, true, GameAsset.WHITE_COW),
    DINOSAUR("dinosaur",1_400, List.of(AnimalProductType.DINOSAUR_EGG), 7, false,GameAsset.DINOSAUR),
    DUCK("duck",1_200, List.of(AnimalProductType.DUCK_EGG, AnimalProductType.DUCK_FEATHER), 2, false,GameAsset.DUCK),
    GOAT("goat",4_000, List.of(AnimalProductType.BIG_GOAT_MILK, AnimalProductType.GOAT_MILK), 2, true,GameAsset.GOAT),
    HEN("hen",800, List.of(AnimalProductType.HEN_EGG, AnimalProductType.HEN_BIG_EGG), 1, false,GameAsset.WHITE_CHICKEN),
    PIG("pig",16_000, List.of(AnimalProductType.TRUFFLE), 0, true,GameAsset.PIG),
    RABBIT("rabbit",8_000, List.of(AnimalProductType.RABBIT_LEG, AnimalProductType.RABBIT_WOOL), 4, false,GameAsset.RABBIT),
    SHEEP("sheep",8_000, List.of(AnimalProductType.SHEEP_WOOL), 3, true,GameAsset.SHEEP);

    private final String name;
    private final Integer productPeriod;
    private final Integer price;
    private final List<AnimalProductType> productList;
    private final Hay food = new Hay();
    private final Boolean isBarnAnimal;
    private final Texture texture;

    AnimalType(String name,int price, List<AnimalProductType> productList, int productPeriod, boolean isBarnAnimal,Texture texture) {
        this.name = name;
        this.price = price;
        this.productList = productList;
        this.productPeriod = productPeriod;
        this.isBarnAnimal = isBarnAnimal;
        this.texture = texture;
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

    @Override
    public Integer getContainingEnergy() {return 0;}
}

