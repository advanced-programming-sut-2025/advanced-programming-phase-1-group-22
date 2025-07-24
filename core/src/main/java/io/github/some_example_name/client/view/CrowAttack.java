package io.github.some_example_name.client.view;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import io.github.some_example_name.common.model.products.HarvestAbleProduct;
import io.github.some_example_name.common.utils.GameAsset;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class CrowAttack {
    private final HarvestAbleProduct harvestAbleProduct;
    private final float duration;
    private float timer;
    private final List<Sprite> crows;
    private boolean finished = false;
    private final List<Float> angles = new ArrayList<>(); // زاویه فعلی هر کلاغ
    private final List<Float> radii = new ArrayList<>();  // شعاع حرکت کلاغ

    private final Vector2 center;

    public CrowAttack(Vector2 targetPosition, int numberOfCrows, float durationSeconds, HarvestAbleProduct harvestAbleProduct) {
        this.duration = durationSeconds;
        this.timer = 0f;
        this.crows = new ArrayList<>();
        this.center = targetPosition;
        this.harvestAbleProduct = harvestAbleProduct;

        for (int i = 0; i < numberOfCrows; i++) {
            Sprite crow = new Sprite(GameAsset.CROW);
            crow.setSize(32, 32);
            crows.add(crow);
            float initialAngle = (float) (Math.random() * 360);
            float radius = 25 + (float) (Math.random() * 15);
            angles.add(initialAngle);
            radii.add(radius);
        }
    }

    public void update(float delta) {
        if (finished) return;
        timer += delta;
        if (timer >= duration) {
            finished = true;
        }
        for (int i = 0; i < crows.size(); i++) {
            float angle = angles.get(i);
            float radius = radii.get(i);
            angle += delta * 90;
            angles.set(i, angle);
            float radians = (float) Math.toRadians(angle);
            float x = center.x + (float) Math.cos(radians) * radius;
            float y = center.y + (float) Math.sin(radians) * radius;
            crows.get(i).setPosition(x, y);
        }
    }

    public void draw(SpriteBatch batch) {
        if (finished) return;
        for (Sprite crow : crows) {
            crow.draw(batch);
        }
    }
}

