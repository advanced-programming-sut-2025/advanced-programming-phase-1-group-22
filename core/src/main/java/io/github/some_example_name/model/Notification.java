package io.github.some_example_name.model;

import com.badlogic.gdx.graphics.Texture;
import io.github.some_example_name.utils.GameAsset;
import lombok.Getter;

@Getter
public class Notification <T> {
    private final NotificationType type;
    private final T data;

	public Notification(NotificationType type, T data) {
		this.type = type;
        this.data = data;
	}
}
