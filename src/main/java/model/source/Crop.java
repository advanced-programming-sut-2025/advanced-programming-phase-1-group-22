package model.source;

import lombok.Getter;
import lombok.Setter;
import model.enums.Season;
import model.products.HarvestAbleProduct;
import model.products.TreesAndFruitsAndSeeds.FruitType;
import model.structure.Structure;

import java.util.List;

@Getter
@Setter
public class Crop extends HarvestAbleProduct implements Source {
    private CropType cropType;

    public Crop(CropType cropType1) {
        super(cropType1.getName(), cropType1.getSource(), cropType1.isForaging(),
                cropType1.getHarvestStages(), !cropType1.isOneTime(), cropType1.getRegrowthTime(),
                cropType1.getSellPrice(), cropType1.isEdible(),
                cropType1.getEnergy(), cropType1.getSeasons(), cropType1.isCanBecomeGiant());
        this.cropType = cropType1;
    }
}
