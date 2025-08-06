package io.github.some_example_name.common.model.structure;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import io.github.some_example_name.common.utils.App;
import io.github.some_example_name.common.utils.GameAsset;
import lombok.Getter;
import org.w3c.dom.Text;

@Getter
public enum StoneType {
    SMALL_STONE(1, 1, 368, 565, 32, 32),
    NORMAL_STONE(3, 1, 336, 672, 48, 16),
    LARGE_STONE(2, 2, 368, 535, 32, 31);
    private final Integer length;
    private final Integer width;
    private final Integer offsetX;
    private final Integer offsetY;
    private final Integer length1;
    private final Integer width1;

    StoneType(int length, int width, int offsetX, int offsetY, int length1, int width1) {
        this.length = length;
        this.width = width;
        this.offsetX = offsetX;
        this.offsetY = offsetY;
        this.length1 = length1;
        this.width1 = width1;
    }

    public TextureRegion getTexture() {
        return new TextureRegion(
            GameAsset.getOutdoors(App.getInstance().getCurrentGame().getTimeAndDate().getSeason()),
            offsetX, offsetY, length1, width1
        );
    }
}
