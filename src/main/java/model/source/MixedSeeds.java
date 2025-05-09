package model.source;

import lombok.Getter;
import lombok.Setter;
import model.Salable;
import model.structure.Structure;

@Getter
@Setter
public class MixedSeeds extends Structure implements Salable {
    private MixedSeedsType mixedSeedsType;

    public MixedSeeds(MixedSeedsType mixedSeedsType) {
        this.mixedSeedsType = mixedSeedsType;
    }

    @Override
    public String getName() {
        return this.mixedSeedsType.getName();
    }

    @Override
    public int getSellPrice() {
        return mixedSeedsType.getSellPrice();
    }

    @Override
    public Integer getContainingEnergy() {return 0;}
}
