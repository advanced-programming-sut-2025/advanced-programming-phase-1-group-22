package io.github.some_example_name.model.dto;

import com.badlogic.gdx.graphics.g2d.Sprite;
import io.github.some_example_name.model.AnimatedSprite;
import io.github.some_example_name.model.Tuple;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class SpriteComponent {
    private ArrayList<SpriteHolder> sprites = new ArrayList<>();
    private boolean changed;
    private Tuple<Float> offset = new Tuple<>(0f, 0f);
    private float width, height;

    public SpriteComponent(SpriteHolder... sprites){
        this.sprites.addAll(List.of(sprites));
    }

    public ArrayList<SpriteHolder> getSprites(float delta){
        for (SpriteHolder sprite : sprites) {
            if (sprite.getSprite() instanceof AnimatedSprite animatedSprite) {
                animatedSprite.update(delta);
            }
        }
        return sprites;
    }
}
