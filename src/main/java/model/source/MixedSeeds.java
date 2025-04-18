package model.source;

import lombok.Getter;
import lombok.Setter;
import model.structure.Structure;

@Getter
@Setter
public class MixedSeeds extends Structure {
    private MixedSeedsType mixedSeedsType;

    public MixedSeeds(MixedSeedsType mixedSeedsType) {
        this.mixedSeedsType = mixedSeedsType;
    }

}
