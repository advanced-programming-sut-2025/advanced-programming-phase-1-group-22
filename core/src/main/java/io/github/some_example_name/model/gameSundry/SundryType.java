package io.github.some_example_name.model.gameSundry;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import io.github.some_example_name.utils.GameAsset;
import lombok.Getter;
import io.github.some_example_name.model.Salable;
import io.github.some_example_name.model.products.Product;

@Getter
public enum SundryType implements SalableIntegration {
    RICE("Rice",200 / 2,null),
    WHEAT_FLOUR("Wheat Flour",100 / 2,null),
    BOUQUET("Bouquet",1000 / 2,null),
    WEDDING_RING("Wedding Ring",10000 / 2,null),
    SUGAR("Sugar",100 / 2,null),
    VINEGAR("Vinegar",200 / 2,null),
    DELUXE_RETAINING_SOIL("Deluxe Retaining Soil",150 / 2, GameAsset.DELUXE_RETAINING_SOIL),
    QUALITY_RETAINING_SOIL("Quality Retaining Soil",150 / 2,GameAsset.QUALITY_RETAINING_SOIL),
    BASIC_RETAINING_SOIL("Basic Retaining Soil",100 / 2,GameAsset.BASIC_RETAINING_SOIL),
    GRASS_STARTER("Grass Starter",100 / 2,null),
    SPEED_GROW("Speed Grow",100 / 2,null),
    JOJA_COLA("Joja Cola", 75/2 ,null);

    private final String name;
    private final Integer price;
    private final Texture texture;

    SundryType(String name, Integer price,Texture texture) {
        this.name = name;
        this.price = price;
        this.texture = texture;
    }

    @Override
    public int getSellPrice() {
        return this.price;
    }

    @Override
    public Integer getContainingEnergy() {return 0;}
}
