package model.source;

import lombok.Getter;
import lombok.Setter;
import model.Salable;
import model.structure.Structure;

@Getter
@Setter
public class Seed extends Structure implements Salable {
    private SeedType seedType;

    public Seed(SeedType seedType) {
        this.seedType = seedType;
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