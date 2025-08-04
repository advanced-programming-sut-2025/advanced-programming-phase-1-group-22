package io.github.some_example_name.common.model.tools;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import io.github.some_example_name.client.view.GameView;
import io.github.some_example_name.client.view.WaterEffect;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import io.github.some_example_name.common.model.products.HarvestAbleProduct;
import io.github.some_example_name.common.model.relations.Player;
import io.github.some_example_name.common.model.Tile;
import io.github.some_example_name.common.model.structure.Structure;
import io.github.some_example_name.common.model.structure.farmInitialElements.GreenHouse;
import io.github.some_example_name.common.model.structure.farmInitialElements.Lake;
import io.github.some_example_name.common.utils.App;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class WateringCan implements Tool {
    private WateringCanType wateringCanType;
    private Integer remain;
    private Texture texture;
    private Sprite sprite;

    public WateringCan(WateringCanType wateringCanType) {
        this.wateringCanType = wateringCanType;
        this.texture = wateringCanType.getTexture();
        this.sprite = new Sprite(wateringCanType.getTexture());
        this.sprite.setSize(App.tileWidth, App.tileHeight);
        this.remain = wateringCanType.getCapacity();
    }

    @Override
    public void addToolEfficiency(double efficiency) {

    }

    @Override
    public Tool getToolByLevel(int level) {
        return wateringCanType.getToolByLevel(level);
    }

    @Override
    public int getLevel() {
        return wateringCanType.getLevel();
    }

    @Override
    public int getEnergy(Player player) {
        return wateringCanType.getEnergy(player);
    }

    @Override
    public String useTool(Player player, Tile tile) {
        player.changeEnergy(-this.getEnergy(player));
        List<Structure> structures = App.getInstance().getCurrentGame().getVillage().findStructuresByTile(tile);
        for (Structure structure : structures) {
            if (structure != null) {
                if (structure instanceof Lake) {
                    this.remain = this.wateringCanType.getCapacity();
                    return "the watering can completely filled";
                }
                if (structure instanceof GreenHouse && ((GreenHouse) structure).isBuilt()) {
                    if (((GreenHouse) structure).getPool().getTiles().contains(tile)) {
                        this.remain = this.wateringCanType.getCapacity();
                        return "the watering can completely filled";
                    }
                }
                if (structure instanceof HarvestAbleProduct) {
                    if (this.remain <= 0) {
                        return "your watering can is empty";
                    }
                    ((HarvestAbleProduct) structure).setWaterToday(true);
                    this.remain -= 1;
                    WaterEffect waterEffect = new WaterEffect(tile.getX() * App.tileWidth, tile.getY() * App.tileHeight);
                    GameView.stage.addActor(waterEffect);
                    return "you water the harvest";
                }
            }
        }

        return "you use this tool in a wrong way";
    }

    @Override
    public Sprite getSprite() {
        return this.wateringCanType.getSprite();
    }

    @Override
    public String getName() {
        return wateringCanType.getName();
    }

    @Override
    public int getSellPrice() {
        return wateringCanType.getSellPrice();
    }


    @Override
    public Integer getContainingEnergy() {
        return 0;
    }

    @Override
    public Texture getTexture() {
        return wateringCanType.getTexture();
    }
}
