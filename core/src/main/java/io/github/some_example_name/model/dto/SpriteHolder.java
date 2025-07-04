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
    private Tuple<Float> offset;

    public  SpriteHolder(Sprite sprite, Tuple<Float> offset){
        this.sprite = sprite;
        this.offset = offset;
    }
}
