package io.github.some_example_name.model;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import lombok.Getter;
import lombok.Setter;


public class AnimatedSprite extends Sprite {
    private Animation<TextureRegion> animation;
    private float stateTime = 0f;
    @Setter
    @Getter
    private boolean looping = false;

    public AnimatedSprite(Animation<TextureRegion> animation) {
        super(animation.getKeyFrame(0));
        this.animation = animation;
    }

    public void update(float delta) {
        stateTime += delta;
        this.setRegion(animation.getKeyFrame(stateTime, looping));
    }
}
