package io.github.some_example_name.model;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import io.github.some_example_name.model.structure.Structure;
import io.github.some_example_name.utils.GameAsset;

public abstract class Actor extends Structure {
    public TextureRegion getAvatar() {
        return new TextureRegion(GameAsset.ABIGAIL); //TODO
    }
}
