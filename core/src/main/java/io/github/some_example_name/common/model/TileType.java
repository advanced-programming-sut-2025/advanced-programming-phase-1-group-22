package io.github.some_example_name.common.model;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.github.some_example_name.common.model.enums.Season;
import io.github.some_example_name.common.utils.GameAsset;
import lombok.Getter;

@Getter
public enum TileType {
    GRASS(GameAsset.DARK_GREEN_FLOOR) {
        @Override
        public void updateTexture(Season season) {
            Texture outdoors = GameAsset.getOutdoors(season);
            texture = new TextureRegion[4][4];
            texture[0][0] = new TextureRegion(outdoors, 0, 208, 48, 48);
            texture[0][1] = new TextureRegion(outdoors, 0, 208, 16, 16);
            texture[0][2] = new TextureRegion(outdoors, 16, 208, 16, 16);
            texture[0][3] = new TextureRegion(outdoors, 32, 208, 16, 16);
            texture[1][0] = new TextureRegion(outdoors, 0, 208, 48, 16);
            texture[1][1] = new TextureRegion(outdoors, 0, 224, 16, 16);
            texture[1][2] = new TextureRegion(outdoors, 16, 224, 16, 16);
            texture[1][3] = new TextureRegion(outdoors, 32, 224, 16, 16);
            texture[2][0] = new TextureRegion(outdoors, 96, 224, 48, 16);
            texture[2][1] = new TextureRegion(outdoors, 0, 240, 16, 16);
            texture[2][2] = new TextureRegion(outdoors, 16, 240, 16, 16);
            texture[2][3] = new TextureRegion(outdoors, 32, 240, 16, 16);
            texture[3][0] = new TextureRegion(outdoors, 0, 240, 48, 16);
            texture[3][1] = new TextureRegion(outdoors, 0, 208, 16, 48);
            texture[3][2] = new TextureRegion(outdoors, 16, 208, 16, 48);
            texture[3][3] = new TextureRegion(outdoors, 32, 208, 16, 48);
        }
    }, FLOWER(GameAsset.FLOWER), SNOW(GameAsset.SNOWED_FLOOR), FLAT(GameAsset.LIGHT_GREEN_FLOOR) {
        @Override
        public void updateTexture(Season season) {
            Texture outdoors = GameAsset.getOutdoors(season);
            texture[0][0] = new TextureRegion(outdoors, 0, 112, 16, 16);
//            texture[0][0] = new TextureRegion(outdoors, 96, 112, 16, 16);
        }
    },
    PATH(GameAsset.PATH_FLOOR), FENCE(GameAsset.STONE_FENCE), DOOR(GameAsset.GATE),PLOWED(GameAsset.PLOWED_FLOORING), THUNDERED(GameAsset.THUNDERED_FLOOR), FERTILIZED(GameAsset.FLOORING_59);

    @JsonIgnore
    protected transient TextureRegion[][] texture;

    TileType(Texture texture) {
        this.texture = new TextureRegion[][]{{new TextureRegion(texture)}};
        updateTexture(Season.SPRING);
    }

    TileType(TextureRegion[][] texture) {
        this.texture = texture;
        updateTexture(Season.SPRING);
    }

    public void updateTexture(Season season) {

    }

    public static void mapBooleansToPair(boolean a, boolean b, boolean c, boolean d, Pair result) {
        int index = ((a ? 1 : 0) << 3) |
            ((b ? 1 : 0) << 2) |
            ((c ? 1 : 0) << 1) |
            ((d ? 1 : 0));

        switch (index) {
            case 0b0000:
                result.setX(1);
                result.setY(2);
                break;
            case 0b0001:
                result.setX(1);
                result.setY(1);
                break;
            case 0b0010:
                result.setX(2);
                result.setY(2);
                break;
            case 0b0011:
                result.setX(2);
                result.setY(1);
                break;
            case 0b0100:
                result.setX(1);
                result.setY(3);
                break;
            case 0b0101:
                result.setX(2);
                result.setY(0);
                break;
            case 0b0110:
                result.setX(2);
                result.setY(3);
                break;
            case 0b0111:
                result.setX(3);
                result.setY(0);
                break;
            case 0b1000:
                result.setX(0);
                result.setY(2);
                break;
            case 0b1001:
                result.setX(0);
                result.setY(1);
                break;
            case 0b1010:
                result.setX(3);
                result.setY(2);
                break;
            case 0b1011:
                result.setX(3);
                result.setY(1);
                break;
            case 0b1100:
                result.setX(0);
                result.setY(3);
                break;
            case 0b1101:
                result.setX(1);
                result.setY(0);
                break;
            case 0b1110:
                result.setX(3);
                result.setY(3);
                break;
            case 0b1111:
                result.setX(0);
                result.setY(0);
                break;
            default:
                result.setX(-1);
                result.setY(-1);
                break; // Should never happen
        }
    }
}

