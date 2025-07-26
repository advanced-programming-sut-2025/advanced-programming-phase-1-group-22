package io.github.some_example_name.common.model.animal;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import io.github.some_example_name.common.utils.App;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import io.github.some_example_name.common.model.Salable;
import io.github.some_example_name.common.model.products.ProductQuality;
import io.github.some_example_name.common.model.structure.Structure;

import java.util.Random;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class Fish extends Structure implements Salable {
    private FishType fishType;
    private transient Texture texture;
    private transient Sprite sprite;
    private Integer containingEnergy = 20; //TODO Energy not found
    private ProductQuality productQuality;
    private int lastDy;
    private final int speed = 200;
    private int coefficientSpeed;
    private float lastMove;

    public int getSellPrice() {
        return (int) (this.fishType.getSellPrice() * productQuality.getPriceCoefficient());
    }

    public Fish(FishType fishType) {
        this.fishType = fishType;
    }

    public Fish(FishType fishType, ProductQuality productQuality) {
        this.fishType = fishType;
        this.productQuality = productQuality;
    }

	@Override
	public String getName() {
		return this.fishType.getName();
	}

    @Override
    public Fish copy() {
        return new Fish(this.fishType, this.productQuality);
    }

    public float updatePositionY(float positionY) {
        this.coefficientSpeed = 1;
        switch (this.fishType.getBehavior()) {
            case MIXED -> {
                return mixedMovement(positionY);
            }
            case SMOOTH -> {
                return smoothMovement(positionY);
            }
            case SINKER -> {
                return sinkerMovement(positionY);
            }
            case FLOATER -> {
                return floaterMovement(positionY);
            }
        }
        return dartMovement(positionY);
    }

    private float mixedMovement(float positionY) {
        Random random = new Random();
        int witchState = random.nextInt(0, 2);
        int dy = random.nextInt(1, 128);
        if (witchState == 0) {
            this.lastDy = dy;
            return positionY + dy;
        } else if (witchState == 1) {
            this.lastDy = -dy;
            return positionY - dy;
        } else {
            this.lastDy = 0;
            return positionY;
        }
    }

    private float smoothMovement(float positionY) {
        Random random = new Random();
        int doLastMove = random.nextInt(0, 10);
        if (doLastMove > 7) return mixedMovement(positionY);
        else return positionY + this.lastDy;
    }

    private float sinkerMovement(float positionY) {
        Random random = new Random();
        int witchState = random.nextInt(0, 2);
        int dy = random.nextInt(1, 128);
        if (witchState == 0) {
            this.lastDy = dy;
            return positionY + dy;
        } else if (witchState == 1) {
            if (this.lastDy == 0) {
                this.coefficientSpeed = 2;
            }
            this.lastDy = -dy;
            return positionY - dy;
        } else {
            this.lastDy = 0;
            return positionY;
        }
    }

    private float floaterMovement(float positionY) {
        Random random = new Random();
        int witchState = random.nextInt(0, 2);
        int dy = random.nextInt(1, 128);
        if (witchState == 0) {
            if (this.lastDy == 0) {
                this.coefficientSpeed = 2;
            }
            this.lastDy = dy;
            return positionY + dy;
        } else if (witchState == 1) {
            this.lastDy = -dy;
            return positionY - dy;
        } else {
            this.lastDy = 0;
            return positionY;
        }
    }

    private float dartMovement(float positionY) {
        Random random = new Random();
        int witchState = random.nextInt(0, 2);
        int dy = random.nextInt(1, 256);
        if (witchState == 0) {
            this.lastDy = dy;
            return positionY + dy;
        } else if (witchState == 1) {
            this.lastDy = -dy;
            return positionY - dy;
        } else {
            this.lastDy = 0;
            return positionY;
        }
    }

    public Texture getTexture() {
        if (texture == null) init();
        return texture;
    }

    public Sprite getSprite() {
        if (sprite == null) init();
        return sprite;
    }

    private void init() {
        this.texture = fishType.getTexture();
        this.sprite = new Sprite(fishType.getTexture());
        this.sprite.setSize(App.tileWidth, App.tileHeight);
    }
}
