package io.github.some_example_name.common.model.structure;

import com.badlogic.gdx.graphics.g2d.Sprite;
import io.github.some_example_name.common.utils.App;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class Trunk extends Structure {
    private TrunkType trunkType;
    private Boolean isBurn = false;
    private transient Sprite sprite;

    public Trunk(TrunkType trunkType) {
        this.trunkType = trunkType;
    }

    public Sprite getSprite() {
        if(sprite == null) {
            this.sprite = new Sprite(trunkType.getTexture());
            this.sprite.setSize(App.tileWidth * trunkType.getWidth(), App.tileHeight * trunkType.getLength());
        }
        return sprite;
    }

    public void burn() {
        this.isBurn = true;
    }

    @Override
    public Integer getHeight() {
        return trunkType.getWidth();
    }

    @Override
    public Integer getWidth() {
        return trunkType.getLength();
    }
}
