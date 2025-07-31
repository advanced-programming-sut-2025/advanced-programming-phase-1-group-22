package io.github.some_example_name.common.model;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import io.github.some_example_name.common.model.structure.Structure;
import io.github.some_example_name.common.utils.GameAsset;

public abstract class Actor extends Structure {
    public abstract String getName();
    public TextureRegion getAvatar() {
        return new TextureRegion(GameAsset.ABIGAIL); //TODO
    }
    public TextureRegion getAvaar() {
        return new TextureRegion(GameAsset.ABIGAIL); //TODO
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (obj.getClass() != this.getClass()) return false;
        return getName().equals(((Actor) obj).getName());
    }

    public abstract String getNickname();
}
