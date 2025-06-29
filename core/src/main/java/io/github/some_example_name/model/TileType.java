package io.github.some_example_name.model;

import com.badlogic.gdx.graphics.Texture;
import io.github.some_example_name.utils.GameAsset;
import lombok.Getter;

@Getter
public enum TileType {
    GRASS(GameAsset.DARK_GREEN_FLOOR), FLOWER(GameAsset.FLOWER), SNOW(GameAsset.SNOWED_FLOOR), FLAT(GameAsset.LIGHT_GREEN_FLOOR),
    PATH(GameAsset.PATH_FLOOR), FENCE(GameAsset.STONE_FENCE), DOOR(GameAsset.GATE),PLOWED(GameAsset.PLOWED_FLOOR), THUNDERED(GameAsset.THUNDERED_FLOOR);

    private final Texture texture;

    TileType(Texture texture) {
        this.texture = texture;
    }
}

