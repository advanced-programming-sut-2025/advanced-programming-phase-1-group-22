package io.github.some_example_name.model;

import com.badlogic.gdx.graphics.Texture;
import io.github.some_example_name.utils.GameAsset;
import lombok.Getter;

@Getter
public enum NotificationType {
	GIFT(GameAsset.GIFT),
    TALK(GameAsset.CHAT),
    MARRIAGE(GameAsset.WEDDING_RING),
    TRADE(GameAsset.ISLAND_TRADER_ICON);
    private final Texture texture;;

	NotificationType(Texture texture) {
		this.texture = texture;
	}
}
