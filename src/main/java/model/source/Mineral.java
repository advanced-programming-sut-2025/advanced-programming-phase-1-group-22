package model.source;

import lombok.Getter;
import lombok.Setter;
import model.Salable;
import model.structure.Structure;

@Getter
@Setter
public class Mineral extends Structure implements Salable {
    private MineralType foragingMineralType;

    @Override
    public String getName() {
        return this.foragingMineralType.getName();
    }

    @Override
    public int getSellPrice() {
        return foragingMineralType.getSellPrice();
    }
}
