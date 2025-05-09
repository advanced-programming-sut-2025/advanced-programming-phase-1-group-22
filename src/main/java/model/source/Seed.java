package model.source;

import lombok.Getter;
import lombok.Setter;
import model.Salable;
import model.structure.Structure;

import java.io.Serializable;

@Getter
@Setter
public class Seed extends Structure implements Salable, Serializable {
    private SeedType seedType;

    public Seed(SeedType seedType) {
        this.seedType = seedType;
    }

    public Seed() {
    }

    @Override
    public String getName() {
        return this.seedType.getName();
    }

    @Override
    public int getSellPrice() {
        return seedType.getSellPrice();
    }
    public void burn() {
        //TODO
    }

    @Override
    public Integer getContainingEnergy() {return seedType.getContainingEnergy();}
}