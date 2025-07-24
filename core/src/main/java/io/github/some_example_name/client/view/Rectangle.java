package io.github.some_example_name.client.view;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Rectangle {
    private float x;
    private float y;
    private final float width;
    private final float height;

    public Rectangle(float x, float y, float width, float height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public boolean collision(Rectangle other) {
        return other.x < this.x + this.width &&
            this.x < other.x + other.width &&
            other.y < this.y + this.height &&
            this.y < other.y + other.height;
    }
}
