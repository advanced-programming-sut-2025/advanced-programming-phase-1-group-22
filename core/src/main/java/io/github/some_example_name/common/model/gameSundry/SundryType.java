package io.github.some_example_name.common.model.gameSundry;

import com.badlogic.gdx.graphics.Texture;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.github.some_example_name.common.utils.GameAsset;
import lombok.Getter;

@Getter
public enum SundryType implements SalableIntegration {
    RICE("Rice", 200 / 2, GameAsset.RICE),
    WHEAT_FLOUR("Wheat Flour", 100 / 2, GameAsset.WHEAT_FLOUR),
    BOUQUET("Bouquet", 1000 / 2, GameAsset.BOUQUET),
    WEDDING_RING("Wedding Ring", 10000 / 2, GameAsset.WEDDING_RING),
    SUGAR("Sugar", 100 / 2, GameAsset.SUGAR),
    VINEGAR("Vinegar", 200 / 2, GameAsset.VINEGAR),
    DELUXE_RETAINING_SOIL("Deluxe Retaining Soil", 150 / 2, GameAsset.DELUXE_RETAINING_SOIL),
    QUALITY_RETAINING_SOIL("Quality Retaining Soil", 150 / 2, GameAsset.QUALITY_RETAINING_SOIL),
    BASIC_RETAINING_SOIL("Basic Retaining Soil", 100 / 2, GameAsset.BASIC_RETAINING_SOIL),
//    GRASS_STARTER("Grass Starter", 100 / 2, GameAsset.GRASS_STARTER),
    SPEED_GROW("Speed Grow", 100 / 2, GameAsset.SPEED_GRO),
    JOJA_COLA("Joja Cola", 75 / 2, GameAsset.JOJA_COLA),
    SONAR_BOBBER("sonar bobber", 100, GameAsset.SONAR_BOBBER);

    private final String name;
    private final Integer price;
    @JsonIgnore
    private final Texture texture;

    SundryType(String name, Integer price, Texture texture) {
        this.name = name;
        this.price = price;
        this.texture = texture;
    }

    @Override
    public int getSellPrice() {
        return this.price;
    }

    @Override
    public Integer getContainingEnergy() {
        return 0;
    }
}
