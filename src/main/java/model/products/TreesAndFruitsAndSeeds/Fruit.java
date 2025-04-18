package model.products.TreesAndFruitsAndSeeds;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import model.enums.Season;
import model.products.HarvestAbleProduct;
import model.source.Seed;
import model.source.Source;

import java.util.List;

@Getter
@Setter
@ToString
public class Fruit extends HarvestAbleProduct {
    FruitType fruitType;

    public Fruit(FruitType fruitType1) {
        super(fruitType1.getName(), fruitType1.getTreeType().getSource(), !fruitType1.getTreeType().getIsForaging(),
                fruitType1.getTreeType().getHarvestStages(), true, fruitType1.getTreeType().getHarvestCycle(), fruitType1.getSellPrice(), fruitType1.getIsEdible(),
                fruitType1.getFruitEnergy(), List.of(fruitType1.getSeason()), false);
        this.fruitType = fruitType1;
    }

}
