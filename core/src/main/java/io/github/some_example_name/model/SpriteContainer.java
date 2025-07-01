package io.github.some_example_name.model;

import com.badlogic.gdx.graphics.g2d.Sprite;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SpriteContainer {
    private Sprite sprite;
    private boolean moving = true;

    public SpriteContainer(Sprite sprite) {
        this.sprite = sprite;
    }
}
