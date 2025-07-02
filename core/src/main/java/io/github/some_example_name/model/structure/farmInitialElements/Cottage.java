package io.github.some_example_name.model.structure.farmInitialElements;

import com.badlogic.gdx.graphics.g2d.Sprite;
import io.github.some_example_name.model.Farm;
import io.github.some_example_name.utils.App;
import io.github.some_example_name.utils.GameAsset;
import io.github.some_example_name.view.Menu;
import lombok.Getter;
import lombok.Setter;
import io.github.some_example_name.model.shelter.FarmBuildingType;

@Setter
public class Cottage extends HardCodeFarmElements {
	private final FarmBuildingType farmBuildingType = FarmBuildingType.Cottage;
    private Sprite outsideSprite;
    private Sprite insideSprite;

	public Cottage(HardCodeFarmElements hardCodeFarmElements) {
		super(hardCodeFarmElements);
        this.outsideSprite = new Sprite(GameAsset.HOUSE);
        this.outsideSprite.setSize(App.tileWidth * 6,App.tileHeight * 6);
        this.insideSprite = new Sprite(GameAsset.COTTAGE_INSIDE);
        this.insideSprite.setSize(App.tileWidth * 6,App.tileHeight * 6);
	}

	public Cottage() {
		super.setLength(6);
		super.setWidth(6);
        this.outsideSprite = new Sprite(GameAsset.HOUSE);
        this.outsideSprite.setSize(App.tileWidth * 6,App.tileHeight * 6);
        this.insideSprite = new Sprite(GameAsset.COTTAGE_INSIDE);
        this.insideSprite.setSize(App.tileWidth * 6,App.tileHeight * 6);
	}

	@Override
	public HardCodeFarmElements cloneEl() {
		return new Cottage(this);
	}

    @Override
    public Sprite getSprite(){
        Farm farm = currentFarm();
        if (farm == null || App.getInstance().getCurrentGame().getCurrentPlayer().getCurrentMenu() != Menu.COTTAGE) {
            return outsideSprite;
        } else {
            return insideSprite;
        }
    }

    private Farm currentFarm() {
        for (Farm farm : App.getInstance().getCurrentGame().getVillage().getFarms()) {
            if (farm.getPlayers().get(0).equals(App.getInstance().getCurrentGame().getCurrentPlayer())) {
                return farm;
            }
        }
        return null;
    }
}
