package io.github.some_example_name.model.structure;

import com.badlogic.gdx.graphics.g2d.Sprite;
import io.github.some_example_name.utils.App;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class Stone extends Structure{
    private StoneType stoneType;
    private Sprite sprite;

    public Stone(StoneType stoneType) {
        this.stoneType = stoneType;
        this.sprite = new Sprite(stoneType.getTexture());
        this.sprite.setSize(App.tileWidth * stoneType.getWidth(),App.tileHeight * stoneType.getLength());
    }
}
