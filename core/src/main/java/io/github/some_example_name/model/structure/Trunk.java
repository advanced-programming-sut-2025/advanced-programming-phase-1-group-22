package io.github.some_example_name.model.structure;

import com.badlogic.gdx.graphics.g2d.Sprite;
import io.github.some_example_name.utils.App;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class Trunk extends Structure {
    private  TrunkType trunkType;
    private Boolean isBurn = false;
    private Sprite sprite;

    public Trunk(TrunkType trunkType) {
        this.trunkType = trunkType;
        this.sprite = new Sprite(trunkType.getTexture());
        this.sprite.setSize(App.tileWidth * trunkType.getWidth(), App.tileHeight * trunkType.getLength());

    }

    public void burn() {
        this.isBurn = true;
    }
}
