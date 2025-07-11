package io.github.some_example_name.model;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import io.github.some_example_name.utils.App;
import io.github.some_example_name.utils.GameAsset;
import lombok.Getter;

@Getter
public enum TileType {
    GRASS(GameAsset.DARK_GREEN_FLOOR), FLOWER(GameAsset.FLOWER), SNOW(GameAsset.SNOWED_FLOOR), FLAT(GameAsset.LIGHT_GREEN_FLOOR),
    PATH(GameAsset.PATH_FLOOR), FENCE(GameAsset.STONE_FENCE), DOOR(GameAsset.GATE),PLOWED(GameAsset.PLOWED_FLOORING), THUNDERED(GameAsset.THUNDERED_FLOOR);

    private final TextureRegion[][] texture;

    TileType(Texture texture) {
        this.texture = new TextureRegion[][]{{new TextureRegion(texture)}};
    }

    TileType(TextureRegion[][] texture) {
        this.texture = texture;
    }

    public static void mapBooleansToPair(boolean a, boolean b, boolean c, boolean d, Pair result) {
        int index = ((a ? 1 : 0) << 3) |
            ((b ? 1 : 0) << 2) |
            ((c ? 1 : 0) << 1) |
            ((d ? 1 : 0));

        switch (index) {
            case 0b0000: result.setX(1); result.setY(2); break;
            case 0b0001: result.setX(1); result.setY(1); break;
            case 0b0010: result.setX(2); result.setY(2); break;
            case 0b0011: result.setX(2); result.setY(1); break;
            case 0b0100: result.setX(1); result.setY(3); break;
            case 0b0101: result.setX(2); result.setY(0); break;
            case 0b0110: result.setX(2); result.setY(3); break;
            case 0b0111: result.setX(3); result.setY(0); break;
            case 0b1000: result.setX(0); result.setY(2); break;
            case 0b1001: result.setX(0); result.setY(1); break;
            case 0b1010: result.setX(3); result.setY(2); break;
            case 0b1011: result.setX(3); result.setY(1); break;
            case 0b1100: result.setX(0); result.setY(3); break;
            case 0b1101: result.setX(1); result.setY(0); break;
            case 0b1110: result.setX(3); result.setY(3); break;
            case 0b1111: result.setX(0); result.setY(0); break;
            default:     result.setX(-1); result.setY(-1); break; // Should never happen
        }
    }
}

