package io.github.some_example_name.model.structure;

import com.badlogic.gdx.graphics.g2d.Sprite;
import io.github.some_example_name.model.dto.SpriteHolder;
import lombok.Getter;
import lombok.Setter;
import io.github.some_example_name.model.Tile;

import java.util.ArrayList;
import java.util.List;
@Getter
@Setter
public abstract class Structure {
    List<Tile> tiles = new ArrayList<>();
    Boolean isPickable = false;

    public Sprite getSprite(){
        return null;
    }

    public ArrayList<SpriteHolder> getSprites() {
        return null;
    }
}
