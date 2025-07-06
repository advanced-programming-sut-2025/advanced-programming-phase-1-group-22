package io.github.some_example_name.model;

import com.badlogic.gdx.graphics.Texture;
import io.github.some_example_name.model.structure.Structure;
import io.github.some_example_name.utils.GameAsset;

import java.io.Serializable;

public abstract class Actor extends Structure {
    public Texture getAvatar() {
        return GameAsset.ABIGAIL; //TODO
    }
}
