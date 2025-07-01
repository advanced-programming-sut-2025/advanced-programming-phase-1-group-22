package io.github.some_example_name.model;

import com.badlogic.gdx.graphics.Texture;

public interface Salable {
 	String getName();
	int getSellPrice();
    default Texture getTexture(){
        return null;
    }

    default Integer getContainingEnergy() {
		return 0;
	}
}
