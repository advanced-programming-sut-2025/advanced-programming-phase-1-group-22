package io.github.some_example_name.model.shelter;

import com.badlogic.gdx.graphics.g2d.Sprite;
import io.github.some_example_name.utils.GameAsset;
import lombok.Getter;
import lombok.Setter;
import io.github.some_example_name.model.structure.farmInitialElements.Cottage;

@Getter
public enum FarmBuildingType {
    BARN(4, 7, 4,true,false,new Sprite(GameAsset.BARN)),
    BIG_BARN(8, 7, 4,true,false,new Sprite(GameAsset.BIG_BARN)),
    DELUXE_BARN(12, 7, 4,true,false,new Sprite(GameAsset.DELUXE_BARN)),
    COOP(4, 6, 3,false,true,new Sprite(GameAsset.COOP)),
    BIG_COOP(8, 6, 3,false,true,new Sprite(GameAsset.BIG_COOP)),
    DELUXE_COOP(12, 6, 3,false,true,new Sprite(GameAsset.DELUXE_COOP)),
    WELL(10,3,3,false,false,new Sprite(GameAsset.WELL)),
    SHIPPING_BIN(20,1,1,false,false,new Sprite(GameAsset.SHIPPINGBIN)),
    Cottage(-1,6,6,false,false,null),
    GreenHouse(-1,8,7,false,false,null),
    Lake(-1,-1,-1,false,false,null),
    Quarry(-1,10,10,false,false,null);

    private final Integer capacity;
    private final Integer height;
    private final Integer width;
    private final Boolean isBarn;
    private final Boolean isCoop;
    private final Sprite sprite;

    FarmBuildingType(Integer capacity, Integer height, Integer width,Boolean isBarn,Boolean isCoop,Sprite sprite) {
        this.capacity = capacity;
        this.height = height;
        this.width = width;
        this.isBarn = isBarn;
        this.isCoop = isCoop;
        this.sprite = sprite;
    }
}
