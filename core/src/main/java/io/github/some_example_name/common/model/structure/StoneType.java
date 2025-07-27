package io.github.some_example_name.common.model.structure;

import com.badlogic.gdx.graphics.Texture;
import io.github.some_example_name.common.utils.GameAsset;
import lombok.Getter;

@Getter
public enum StoneType {
    SMALL_STONE(1, 1, GameAsset.STONE_INDEX668),
    NORMAL_STONE(2, 1,GameAsset.STONE_INDEX343),
    LARGE_STONE(2, 2,GameAsset.FARM_BOULDER);
    private final Integer length;
    private final Integer width;
    private final transient Texture texture;

    StoneType(int length, int width,Texture texture) {
        this.length = length;
        this.width = width;
        this.texture = texture;
    }
}
