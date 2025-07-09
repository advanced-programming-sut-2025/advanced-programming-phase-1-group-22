package io.github.some_example_name.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import io.github.some_example_name.model.animal.Fish;
import io.github.some_example_name.model.animal.FishType;
import io.github.some_example_name.model.tools.FishingPole;
import io.github.some_example_name.utils.GameAsset;
import lombok.Getter;

@Getter
public class MiniGame {
    private final FishingPole fishingPole;
    private final OrthographicCamera camera;
    private final float originX;
    private final float originY;
    private final Fish fish;
    private float positionY;
    private float targetY;
    private final Sprite barSprite;
    private final Sprite backgroundSprite;
    private final Sprite fishSprite;
    private final Rectangle fishRectangle;
    private final Rectangle barRectangle;
    private final float barSpeed = 200;
    private final float BAR_WIDTH = 48;
    private final float BAR_HEIGHT = 128;
    private final float FISH_WIDTH = 48;
    private final float FISH_HEIGHT = 48;
    private final float BACKGROUND_WIDTH = 128;
    private final float BACKGROUND_HEIGHT = 512;
    private float progress = 0f;
    private final float PROGRESS_SPEED_INCREASE = 0.10f;
    private final float PROGRESS_SPEED_DECREASE = 0.30f;
    private boolean isFishingPerfect = true;
    private boolean gameOver = false;
    private boolean gameWin = false;
    private boolean showWinFish = false;
    private float winFishTimer = 0;
    private final float WIN_FISH_DURATION = 4f;

    public MiniGame(Fish fish, FishingPole fishingPole, OrthographicCamera camera) {
        this.fish = fish;
        this.fishingPole = fishingPole;
        this.backgroundSprite = new Sprite(GameAsset.MINI_GAME_BACKGROUND);
        this.barSprite = new Sprite(GameAsset.MINI_GAME_BAR);
        this.fishSprite = new Sprite(fish.getTexture());
        this.camera = camera;
        originX = camera.position.x - BACKGROUND_WIDTH / 2f;
        originY = camera.position.y - BACKGROUND_HEIGHT / 2f;
        this.fishRectangle = new Rectangle(originX + 16 + BAR_WIDTH * 0.75f, originY + BACKGROUND_HEIGHT / 2f - FISH_HEIGHT / 2f + 50, FISH_WIDTH, FISH_HEIGHT);
        this.barRectangle = new Rectangle(originX + 16 + BAR_WIDTH * 0.75f, originY + BACKGROUND_HEIGHT / 2f - BAR_HEIGHT / 2f, BAR_WIDTH, BAR_HEIGHT);
        this.positionY = this.fishRectangle.getY();
        backgroundSprite.setPosition(originX, originY);
        backgroundSprite.setSize(BACKGROUND_WIDTH, BACKGROUND_HEIGHT);
        barSprite.setSize(BAR_WIDTH, BAR_HEIGHT);
        barSprite.setPosition(barRectangle.getX(), barRectangle.getY());
        fishSprite.setSize(FISH_WIDTH, FISH_HEIGHT);
        fishSprite.setPosition(fishRectangle.getX(), fishRectangle.getY());
    }

    public void updateFish(float delta) {
        fish.setLastMove(fish.getLastMove() + delta);
        if (fish.getLastMove() > 0.5f) {
            fish.setLastMove(0);
            targetY = fish.updatePositionY(positionY);
            targetY = Math.min(Math.max(targetY, originY), originY + BACKGROUND_HEIGHT - FISH_HEIGHT);
        }
        float speed = fish.getSpeed() * delta * fish.getCoefficientSpeed();
        if (Math.abs(targetY - positionY) < speed) {
            positionY = targetY;
        } else if (targetY > positionY) {
            positionY += speed;
        } else if (targetY < positionY) {
            positionY -= speed;
        }
        fishRectangle.setY(positionY);
    }

    public void updateBar(float delta) {
        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            barRectangle.setY(barRectangle.getY() + barSpeed * delta);
        } else if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            barRectangle.setY(barRectangle.getY() - barSpeed * delta);
        }
        barRectangle.setY(Math.min(originY + BACKGROUND_HEIGHT - BAR_HEIGHT, Math.max(barRectangle.getY(), originY)));
    }

    private void drawProgressBar(SpriteBatch batch) {
        float barWidth = 12f;
        float barHeight = BACKGROUND_HEIGHT * progress;
        float barX = originX + BACKGROUND_WIDTH - barWidth - 4;
        float barY = originY;

        if (progress > 0.5f) {
            batch.setColor(0f, 1f, 0f, 1f);
        } else {
            batch.setColor(1f, 0.6f, 0f, 1f);
        }

        batch.draw(GameAsset.PIXEL, barX, barY, barWidth, barHeight);
        batch.setColor(1f, 1f, 1f, 1f);
    }

    private void updateProgress(float delta) {
        if (barRectangle.collision(fishRectangle)) {
            progress += PROGRESS_SPEED_INCREASE * delta;
        } else {
            this.isFishingPerfect = false;
            progress -= PROGRESS_SPEED_DECREASE * delta * 0.5f;
        }
        progress = Math.max(0f, Math.min(1f, progress));
        if (progress >= 1f) {
            gameOver = true;
            gameWin = true;
            if (fish.getFishType().equals(FishType.Legend)) {
                showWinFish = true;
                winFishTimer = WIN_FISH_DURATION;
            }
        } else if (progress <= 0f) {
            gameOver = true;
            gameWin = false;
        }
    }

    public void draw(SpriteBatch batch, float delta) {
        if (showWinFish) {
            winFishTimer -= delta;
            float w = FISH_WIDTH * 4;
            float h = FISH_HEIGHT * 4;
            float x = camera.position.x - w / 2;
            float y = camera.position.y - h / 2;
            batch.draw(fish.getTexture(), x, y, w, h);
            float crownW = w * 0.6f;
            float crownH = h * 0.4f;
            float crownX = x + w / 2 - crownW / 2;
            float crownY = y + h - crownH / 2;
            batch.draw(GameAsset.EMOJIS096, crownX, crownY, crownW, crownH);

            if (winFishTimer <= 0) {
                showWinFish = false;
            }

            return;
        }
        barSprite.setPosition(barRectangle.getX(), barRectangle.getY());
        fishSprite.setPosition(fishRectangle.getX(), fishRectangle.getY());
        backgroundSprite.draw(batch);
        barSprite.draw(batch);
        fishSprite.draw(batch);
        drawProgressBar(batch);
        updateBar(delta);
        updateFish(delta);
        updateProgress(delta);
    }
}
