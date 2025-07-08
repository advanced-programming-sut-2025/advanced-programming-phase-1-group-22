package io.github.some_example_name.model;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import lombok.Getter;
import lombok.Setter;


@Setter
public class AnimatedSprite extends Sprite {
    private Animation<TextureRegion> animation;
    private Animation<Float> rotationAnimation;
    private Animation<Float> scaleAnimation;
    private boolean scaleLooping = true;
    private float stateTime = 0f;
    @Getter
    private boolean looping = false;

    public AnimatedSprite(Animation<TextureRegion> animation) {
        super(animation.getKeyFrame(0));
        this.animation = animation;
    }

    public void update(float delta) {
        stateTime += delta;
        this.setRegion(animation.getKeyFrame(stateTime, looping));
        if (rotationAnimation != null) {
            this.setRotation(rotationAnimation.getKeyFrame(stateTime, looping));
        }
        if (scaleAnimation != null) {
            this.setScale(scaleAnimation.getKeyFrame(stateTime, scaleLooping && looping));
        }
    }
}
