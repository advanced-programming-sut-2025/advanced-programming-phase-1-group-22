package model.source;

import lombok.Getter;
import lombok.Setter;
import model.structure.Structure;

@Getter
@Setter
public class Seed extends Structure {
    private SeedType seedType;

    public Seed(SeedType seedType) {
        this.seedType = seedType;
    }

}