package io.github.some_example_name.model.structure.farmInitialElements;

import com.badlogic.gdx.graphics.g2d.Sprite;
import io.github.some_example_name.utils.App;
import io.github.some_example_name.utils.GameAsset;
import lombok.Getter;
import lombok.Setter;
import io.github.some_example_name.model.Farm;
import io.github.some_example_name.model.Salable;
import io.github.some_example_name.model.Tile;
import io.github.some_example_name.model.relations.Player;
import io.github.some_example_name.model.shelter.FarmBuildingType;
import io.github.some_example_name.model.source.MineralType;

@Getter
@Setter
public class GreenHouse extends HardCodeFarmElements {
    private final FarmBuildingType farmBuildingType = FarmBuildingType.GreenHouse;
    private Lake pool;
    private Sprite spriteBuilt;
    private Sprite spriteBroken;
    private boolean isBuilt = false;

    public GreenHouse(GreenHouse greenHouse) {
        super(greenHouse);
        this.spriteBuilt = new Sprite(GameAsset.GREENHOUSE);
        this.spriteBuilt.setSize(this.farmBuildingType.getWidth() * App.tileWidth,this.farmBuildingType.getHeight() * App.tileHeight);
        this.spriteBroken = new Sprite(GameAsset.BROKEN_GREENHOUSE);
        this.spriteBroken.setSize(this.farmBuildingType.getWidth() * App.tileWidth,this.farmBuildingType.getHeight() * App.tileHeight);
    }

    public GreenHouse() {
        super.setLength(8);
        super.setWidth(7);
        this.spriteBuilt = new Sprite(GameAsset.GREENHOUSE);
        this.spriteBuilt.setSize(this.farmBuildingType.getWidth() * App.tileWidth,this.farmBuildingType.getHeight() * App.tileHeight);
        this.spriteBroken = new Sprite(GameAsset.BROKEN_GREENHOUSE);
        this.spriteBroken.setSize(this.farmBuildingType.getWidth() * App.tileWidth,this.farmBuildingType.getHeight() * App.tileHeight);
    }

    public int areIngredientsAvailable(Player player) {
        if (player.getAccount().getGolds() < 1000) return 1;
        if (!player.getInventory().checkProductAvailabilityInBackPack(MineralType.WOOD.getName(), 500)) return 2;
        player.getAccount().removeGolds(1000);
        Salable salable = player.getInventory().findProductInBackPackByNAme(MineralType.WOOD.getName());
        player.getInventory().deleteProductFromBackPack(salable, player,500);
        isBuilt = true;
        return 0;
    }


    @Override
    public HardCodeFarmElements cloneEl() {
        return new GreenHouse(this);
    }

    public void build(Farm farm) {
        isBuilt = true;
        for (Tile tile : this.getTiles()) {
            tile.setIsFilled(false);
        }
        pool = new Lake();
        for (int i = 0; i <8; i++) {
            this.getTiles().get(i).setIsFilled(true);
            pool.getTiles().add(this.getTiles().get(i));
        }
        farm.getStructures().add(pool);
    }

    @Override
    public Sprite getSprite() {
        if (this.isBuilt) return this.spriteBuilt;
        return this.spriteBroken;
    }
}
