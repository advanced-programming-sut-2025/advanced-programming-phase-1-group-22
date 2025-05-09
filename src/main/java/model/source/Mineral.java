package model.source;

import lombok.Getter;
import lombok.Setter;
import model.Salable;
import model.structure.Structure;

import java.io.Serializable;

@Getter
@Setter
public class Mineral extends Structure implements Salable , Serializable {
    private MineralType foragingMineralType;

    public Mineral(MineralType foragingMineralType) {
        this.foragingMineralType = foragingMineralType;
    }

    public Mineral() {
    }

    @Override
    public String getName() {
        return this.foragingMineralType.getName();
    }

    @Override
    public int getSellPrice() {
        return foragingMineralType.getSellPrice();
    }

    @Override
    public Integer getContainingEnergy() {return 0;}
}
