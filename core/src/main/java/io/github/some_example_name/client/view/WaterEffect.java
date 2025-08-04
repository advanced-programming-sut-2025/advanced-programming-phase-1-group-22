package io.github.some_example_name.client.view;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import io.github.some_example_name.common.utils.GameAsset;

public class WaterEffect extends Actor {
    private static final int FRAME_WIDTH = 41;
    private static final int FRAME_HEIGHT = 130;
    private static final float FRAME_DURATION = 0.06f;

    private final Animation<TextureRegion> animation;
    private float stateTime;
    private final Vector2 position;

    public WaterEffect(float x, float y) {
        this.position = new Vector2(x, y);

        TextureRegion[] frames = new TextureRegion[GameAsset.RAIN[0].length];
        for (int i = 0; i < GameAsset.RAIN[0].length; i++) {
            frames[i] = GameAsset.RAIN[0][i];
        }

        animation = new Animation<>(FRAME_DURATION, frames);
        stateTime = 0f;

        setBounds(x, y, FRAME_WIDTH, FRAME_HEIGHT);
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        stateTime += delta;

        if (animation.isAnimationFinished(stateTime)) {
            remove();
        }
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        TextureRegion frame = animation.getKeyFrame(stateTime, false);
        batch.draw(frame, position.x, position.y);
    }
}
