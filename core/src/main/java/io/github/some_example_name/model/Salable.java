package io.github.some_example_name.model;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import io.github.some_example_name.model.dto.SpriteHolder;

import java.util.ArrayList;

public interface Salable {
 	String getName();
	int getSellPrice();
    default Texture getTexture(){
        return null;
    }
    default Sprite getSprite(){
        return null;
    }

    default ArrayList<SpriteHolder> getSprites() {
        return null;
    }


    default Integer getContainingEnergy() {
		return 0;
	}
}
