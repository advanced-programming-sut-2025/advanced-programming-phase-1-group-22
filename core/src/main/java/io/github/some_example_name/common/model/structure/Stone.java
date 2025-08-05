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
            this.sprite.setSize(App.tileWidth * stoneType.getLength(),App.tileHeight * stoneType.getWidth());
        }
        return sprite;
    }

    public void updateSprite() {
        if (sprite == null) {
            this.sprite = new Sprite(stoneType.getTexture());
            this.sprite.setSize(App.tileWidth * stoneType.getLength(),App.tileHeight * stoneType.getWidth());
        } else {
            sprite.setRegion(stoneType.getTexture());
        }
    }

    @Override
    public Integer getHeight() {
        return stoneType.getWidth();
    }

    @Override
    public Integer getWidth() {
        return stoneType.getLength();
    }
}
