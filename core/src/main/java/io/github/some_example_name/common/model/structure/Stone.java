package io.github.some_example_name.common.model.structure;

import com.badlogic.gdx.graphics.g2d.Sprite;
import io.github.some_example_name.common.utils.App;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class Stone extends Structure{
    private StoneType stoneType;
    private transient Sprite sprite;

    public Stone(StoneType stoneType) {
        this.stoneType = stoneType;
    }

    public Sprite getSprite() {
        if (sprite == null) {
            this.sprite = new Sprite(stoneType.getTexture());
            this.sprite.setSize(App.tileWidth * stoneType.getWidth(),App.tileHeight * stoneType.getLength());
        }
        return sprite;
    }
}
