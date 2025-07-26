package io.github.some_example_name.common.model;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import io.github.some_example_name.common.model.dto.SpriteHolder;

import java.util.ArrayList;

@JsonTypeInfo(
    use = JsonTypeInfo.Id.CLASS,
    include = JsonTypeInfo.As.PROPERTY,
    property = "@class"
)

public interface Salable {
    String getName();

    int getSellPrice();

    default Texture getTexture() {
        return null;
    }

    default Sprite getSprite() {
        return null;
    }

    default ArrayList<SpriteHolder> getSprites() {
        return null;
    }

    default Salable copy() {
        return this;
    }

    default Integer getContainingEnergy() {
        return 0;
    }
}
