package io.github.some_example_name.common.model;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import io.github.some_example_name.common.model.structure.Structure;
import io.github.some_example_name.common.utils.GameAsset;

public abstract class Actor extends Structure {
    public abstract String getName();
    public TextureRegion getAvatar() {
        return new TextureRegion(GameAsset.ABIGAIL); //TODO
    }
}
