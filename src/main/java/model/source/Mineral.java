package model.source;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import model.Salable;
import model.structure.Structure;

@Getter
@Setter
@NoArgsConstructor
public class Mineral extends Structure implements Salable {
    private MineralType foragingMineralType;

    public Mineral(MineralType foragingMineralType) {
        this.foragingMineralType = foragingMineralType;
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
