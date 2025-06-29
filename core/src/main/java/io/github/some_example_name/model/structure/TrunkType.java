package io.github.some_example_name.model.structure;

import com.badlogic.gdx.graphics.Texture;
import io.github.some_example_name.utils.GameAsset;
import lombok.Getter;
import lombok.Setter;

@Getter
public enum TrunkType {
    SMALL_TRUNK(1, 1, GameAsset.DRIFTWOOD),
    NORMAL_TRUNK(2, 1,GameAsset.WOOD),
    LARGE_TRUNK(2, 2,GameAsset.HARDWOOD);
    private final Integer length;
    private final Integer width;
    private final Texture texture;

    TrunkType(Integer length, Integer width,Texture texture) {
        this.length = length;
        this.width = width;
        this.texture = texture;
    }
}
