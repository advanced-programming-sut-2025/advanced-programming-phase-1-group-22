package io.github.some_example_name.model.products;

import com.badlogic.gdx.graphics.Texture;
import io.github.some_example_name.utils.GameAsset;
import lombok.Getter;

@Getter
public enum AnimalProductType implements Product {
    HEN_EGG("hen egg",50, GameAsset.EGG),
    HEN_BIG_EGG("big hen egg",95,GameAsset.LARGE_EGG),
    DUCK_EGG("duck egg",95,GameAsset.DUCK_EGG),
    DUCK_FEATHER("duck feather",250,GameAsset.DUCK_FEATHER),
    RABBIT_WOOL("rabbit wool",340,GameAsset.WOOL),
    RABBIT_LEG("rabbit leg",565,GameAsset.RABBIT_S_FOOT),
    DINOSAUR_EGG("dinosaur egg",350,GameAsset.DINOSAUR_EGG),
    MILK("milk",125,GameAsset.MILK),
    BIG_MILK("big milk",190,GameAsset.LARGE_MILK),
    GOAT_MILK("goat milk",225,GameAsset.GOAT_MILK),
    BIG_GOAT_MILK("big goat milk",345,GameAsset.LARGE_GOAT_MILK),
    SHEEP_WOOL("sheep wool",340,GameAsset.WOOL),
    TRUFFLE("truffle",625,GameAsset.TRUFFLE);

    private final String name;
    private final Integer price;
    private final Texture texture;

    AnimalProductType(String name,Integer price,Texture texture) {
        this.name = name;
        this.texture = texture;
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
