package io.github.some_example_name.common.model.structure;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import io.github.some_example_name.common.model.dto.SpriteComponent;
import io.github.some_example_name.common.model.dto.SpriteHolder;
import lombok.Getter;
import lombok.Setter;
import io.github.some_example_name.common.model.Tile;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@JsonTypeInfo(
    use = JsonTypeInfo.Id.CLASS,
    include = JsonTypeInfo.As.PROPERTY,
    property = "@class"
)
public abstract class Structure {
    List<Tile> tiles = new ArrayList<>();
    Boolean isPickable = false;

    public Sprite getSprite() {
        return null;
    }

    public ArrayList<SpriteHolder> getSprites() {
        return null;
    }

    public Integer getWidth() {
        return null;
    }

    public Integer getHeight() {
        return null;
    }

    public SpriteComponent getSpriteComponent() {
        return null;
    }
}
