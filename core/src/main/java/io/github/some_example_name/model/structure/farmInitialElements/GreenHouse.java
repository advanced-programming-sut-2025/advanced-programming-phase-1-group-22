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

import java.util.Collections;

@Getter
@Setter
public class GreenHouse extends HardCodeFarmElements {
    private final FarmBuildingType farmBuildingType = FarmBuildingType.GreenHouse;
    private Lake pool;
    private Sprite sprite;
    private boolean isBuilt = false;

    public GreenHouse(GreenHouse greenHouse) {
        super(greenHouse);
        this.sprite = new Sprite(GameAsset.GREENHOUSE_UNBUILT);
        this.sprite.setSize(this.farmBuildingType.getWidth() * App.tileWidth,this.farmBuildingType.getHeight() * App.tileHeight);
    }

    public GreenHouse() {
        super.setWidth(8);
        super.setHeight(7);
        this.sprite = new Sprite(GameAsset.GREENHOUSE_UNBUILT);
        this.sprite.setSize(this.farmBuildingType.getWidth() * App.tileWidth,this.farmBuildingType.getHeight() * App.tileHeight);
    }

    public int areIngredientsAvailable(Player player) {
        if (player.getAccount().getGolds() < 1000) return 1;
        if (!player.getInventory().checkProductAvailabilityInBackPack(MineralType.WOOD.getName(), 500)) return 2;
        player.getAccount().removeGolds(1000);
        Salable salable = player.getInventory().findProductInBackPackByNAme(MineralType.WOOD.getName());
        player.getInventory().deleteProductFromBackPack(salable, player,500);
        isBuilt = true;
        sprite =  new Sprite(GameAsset.GREENHOUSE);
        this.sprite.setSize(this.farmBuildingType.getWidth() * App.tileWidth,this.farmBuildingType.getHeight() * App.tileHeight);
        return 0;
    }


    @Override
    public HardCodeFarmElements copyEl() {
        return new GreenHouse(this);
    }

    public void build(Farm farm) {
        sprite =  new Sprite(GameAsset.GREENHOUSE);
        this.sprite.setSize(this.farmBuildingType.getWidth() * App.tileWidth,this.farmBuildingType.getHeight() * App.tileHeight);
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
        Collections.swap(farm.getStructures(), farm.getStructures().size() - 1, farm.getStructures().indexOf(this));
    }
}
