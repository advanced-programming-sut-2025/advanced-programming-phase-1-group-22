package io.github.some_example_name.common.model.shelter;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import io.github.some_example_name.common.model.Salable;
import io.github.some_example_name.common.utils.GameAsset;
import lombok.Getter;

@Getter
public enum FarmBuildingType implements Salable {
    BARN("Barn",4, 7, 4,true,false,
        GameAsset.BARN,new Sprite(GameAsset.BARN),GameAsset.BARN_INTERIOR,new Sprite(GameAsset.BARN_INTERIOR)),
    BIG_BARN("Big Barn",8, 7, 4,true,false,
        GameAsset.BIG_BARN,new Sprite(GameAsset.BIG_BARN),GameAsset.BIG_BARN_INTERIOR,new Sprite(GameAsset.BIG_BARN_INTERIOR)),
    DELUXE_BARN("Deluxe Barn",12, 7, 4,true,false,
        GameAsset.DELUXE_BARN,new Sprite(GameAsset.DELUXE_BARN),GameAsset.DELUXE_BARN_INTERIOR,new Sprite(GameAsset.DELUXE_BARN_INTERIOR)),
    COOP("Coop",4, 6, 3,false,true,
        GameAsset.COOP,new Sprite(GameAsset.COOP),GameAsset.COOP_INTERIOR,new Sprite(GameAsset.COOP_INTERIOR)),
    BIG_COOP("Big Coop",8, 6, 3,false,true,
        GameAsset.BIG_COOP,new Sprite(GameAsset.BIG_COOP),GameAsset.BIG_COOP_INTERIOR,new Sprite(GameAsset.BIG_COOP_INTERIOR)),
    DELUXE_COOP("Deluxe Coop",12, 6, 3,false,true,
        GameAsset.DELUXE_COOP,new Sprite(GameAsset.DELUXE_COOP),GameAsset.DELUXE_COOP_INTERIOR,new Sprite(GameAsset.DELUXE_COOP_INTERIOR)),
    WELL("Well",10,3,3,false,false,GameAsset.WELL,new Sprite(GameAsset.WELL),null,null),
    SHIPPING_BIN("Shipping Bin",20,1,1,false,false,GameAsset.SHIPPINGBIN,new Sprite(GameAsset.SHIPPINGBIN),null,null),
    Cottage("Cottage",-1,6,6,false,false,null,null,null,null),
    GreenHouse("GreenHouse",-1,8,7,false,false,null,null,null,null),
    Lake("Lake",-1,-1,-1,false,false,null,null,null,null),
    Quarry("Quarry",-1,10,10,false,false,null,null,null,null);

    private final String name;
    private final Integer capacity;
    private final Integer height;
    private final Integer width;
    private final Boolean isBarn;
    private final Boolean isCoop;
    private final Texture texture;
    private final Texture textureInterior;
    private final Sprite sprite;
    private final Sprite spriteInterior;

    FarmBuildingType(String name,Integer capacity, Integer height, Integer width,Boolean isBarn,Boolean isCoop,
                     Texture texture,Sprite sprite,Texture textureInterior,Sprite spriteInterior) {
        this.capacity = capacity;
        this.height = height;
        this.width = width;
        this.isBarn = isBarn;
        this.isCoop = isCoop;
        this.texture = texture;
        this.sprite = sprite;
        this.name = name;
        this.textureInterior = textureInterior;
        this.spriteInterior = spriteInterior;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public int getSellPrice() {
        return 0;
    }
}
