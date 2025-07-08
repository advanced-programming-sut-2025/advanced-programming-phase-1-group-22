package io.github.some_example_name.model;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import io.github.some_example_name.model.enums.Gender;
import io.github.some_example_name.utils.GameAsset;
import lombok.Getter;

import java.util.HashMap;

@Getter
public enum PlayerType {
    ABIGAIL("Abigail", GameAsset.ABIGAIL_FULL, Gender.FEMALE, 66, 128, 56, 65,
        0),
    ALEX("Alex", GameAsset.ALEX_FULL, Gender.MALE, 66, 128, 56, 65,
        0),
    EMILY("Emily", GameAsset.EMILY_FULL, Gender.FEMALE, 66, 128, 56, 65,
        0),
    ELLIOT("Elliot", GameAsset.ELLIOT_FULL, Gender.MALE, 66, 128, 56, 65,
        0),
    HALEY("Haley", GameAsset.HALEY_FULL, Gender.FEMALE, 66, 128, 56, 65,
        1),
    HARVEY("Harvey", GameAsset.HARVEY_FULL, Gender.MALE, 66, 128, 56, 65,
        0),
    LEAH("Leah", GameAsset.LEAH_FULL, Gender.FEMALE, 66, 128, 56, 65,
        0),
    MARU("Maru", GameAsset.MARU_FULL, Gender.FEMALE, 66, 128, 56, 65,
        0),
    PENNY("Penny", GameAsset.PENNY_FULL, Gender.FEMALE, 66, 128, 56, 65,
        1),
    SAM("Sam", GameAsset.SAM_FULL, Gender.MALE, 66, 128, 56, 65,
        0),
    SHANE("Shane", GameAsset.SHANE_FULL, Gender.MALE, 66, 128, 56, 65,
        0),
    SEBASTIAN("Sebastian", GameAsset.SEBASTIAN_FULL, Gender.MALE, 66, 128, 56, 65,
        0),
    ;

    private final Texture texture;
    private final TextureRegion[][] miniTextures;
    private final TextureRegion avatar;
    private final Gender gender;
    private final String name;
    private final int miniOffsetY;
    private final HashMap<Direction, Integer> directions;

    PlayerType(String name, Texture texture, Gender gender, int avatarX, int avatarY, int avatarWidth, int avatarHeight,
               int miniOffsetY) {
        this.texture = texture;
        this.name = name;
        this.gender = gender;
        this.avatar = new TextureRegion(texture, avatarX, avatarY, avatarWidth, avatarHeight);
        this.miniTextures = TextureRegion.split(texture, 16, 32);
        this.miniOffsetY = miniOffsetY;
        directions =  new HashMap<>();
        directions.put(Direction.NORTH, 2);
        directions.put(Direction.EAST, 1);
        directions.put(Direction.SOUTH, 0);
        directions.put(Direction.WEST, 3);
    }

    public static PlayerType findInstance(String character) {
        for (PlayerType type : PlayerType.values()) {
            if (type.name.equalsIgnoreCase(character)) return type;
        }
        return null;
    }

    public TextureRegion getLazy() {
        return miniTextures[this.miniOffsetY][0];
    }

    public AnimatedSprite getWalking(Direction direction) {
        Animation<TextureRegion> animation = new Animation<>(0.1f,
            miniTextures[miniOffsetY + directions.get(direction)][1], miniTextures[miniOffsetY + directions.get(direction)][2],
            miniTextures[miniOffsetY + directions.get(direction)][3], miniTextures[miniOffsetY +  + directions.get(direction)][0]);
        return new AnimatedSprite(animation);
    }

    public AnimatedSprite getFainting() {
        Animation<TextureRegion> animation = new Animation<>(0.1f,
            miniTextures[miniOffsetY + directions.get(Direction.SOUTH)][0], miniTextures[miniOffsetY + directions.get(Direction.SOUTH)][3],
            miniTextures[miniOffsetY + directions.get(Direction.EAST)][3], miniTextures[miniOffsetY + +directions.get(Direction.EAST)][0]);
        AnimatedSprite x = new AnimatedSprite(animation);
        x.setLooping(false);
        return x;
    }
}
