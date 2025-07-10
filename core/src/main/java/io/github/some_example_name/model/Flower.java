package io.github.some_example_name.model;

import com.badlogic.gdx.graphics.Texture;
import io.github.some_example_name.utils.GameAsset;

public class Flower implements Salable {
    private String name = "flower";

    @Override
    public Texture getTexture() {
        return GameAsset.FLOWER;
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

    @Override
    public Salable copy() {
        return new Flower();
    }
}
