package io.github.some_example_name.client.view;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import io.github.some_example_name.common.model.structure.farmInitialElements.Lake;
import io.github.some_example_name.common.utils.App;
import io.github.some_example_name.common.utils.GameAsset;

public class FishActor extends Actor {
    private final Texture texture = GameAsset.CARP;
    private final float speed;
    private boolean movingRight;
    private final Lake lake;

    public FishActor(Lake lake, float startX, float startY) {
        this.lake = lake;
        setPosition(startX, startY);
        setSize(App.tileWidth, App.tileHeight);
        speed = 30 + (float) Math.random() * 50;
        movingRight = Math.random() > 0.5;
    }

    @Override
    public void act(float delta) {
        float dx = delta * speed * (movingRight ? 1 : -1);
        moveBy(dx, 0);

        if (getX() < lake.getTiles().get(0).getX() * App.tileWidth || getX() + getWidth() > lake.getTiles().get(0).getX() * App.tileWidth + lake.getWidth() * App.tileWidth) {
            movingRight = !movingRight;
        }
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        if (movingRight) {
            batch.draw(texture, getX(), getY(), getWidth(), getHeight());
        } else {
            batch.draw(texture, getX() + getWidth(), getY(), -getWidth(), getHeight());
        }
    }
}
