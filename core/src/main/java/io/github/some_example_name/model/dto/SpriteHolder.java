package io.github.some_example_name.model.dto;

import com.badlogic.gdx.graphics.g2d.Sprite;
import io.github.some_example_name.model.Pair;
import io.github.some_example_name.model.Tuple;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SpriteHolder {
    private Sprite sprite;
    private boolean changed;
    private Tuple<Float> offset;
    private float width, height;

    public  SpriteHolder(Sprite sprite, Tuple<Float> offset){
        this.sprite = sprite;
        this.offset = offset;
        width = sprite.getWidth();
        height = sprite.getHeight();
    }

    public void setSprite(Sprite sprite) {
        this.sprite = sprite;
        changed = true;
    }

    public Sprite getSprite() {
        if (changed) {
            sprite.setSize(width, height);
            changed = false;
        }
        return sprite;
    }

    public void setSize(float width, float height) {
        this.width = width;
        this.height = height;
    }
}
