package io.github.some_example_name.common.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

public abstract class SpriteMixIn {
    @JsonIgnore
    public abstract void setRegion(com.badlogic.gdx.graphics.g2d.TextureRegion region);

    @JsonIgnore
    public abstract void setRegion(com.badlogic.gdx.graphics.Texture texture);
}

