package model.structure.stores;

import lombok.Getter;
import model.source.MineralType;
@Getter
public enum BlackSmithStuff {
    COAL(MineralType.COAL, 150, -1),
    IRON_ORE(MineralType.IRON_ORE, 150, -1),
    COPPER_ORE(MineralType.COPPER_ORE, 75, -1),
    GOLD_ORE(MineralType.GOLD_ORE, 400, -1);
    private final MineralType mineralType;
    private final Integer price;
    private final Integer dailyLimit;

    BlackSmithStuff(MineralType mineralType, Integer price, Integer dailyLimit) {
        this.mineralType = mineralType;
        this.price = price;
        this.dailyLimit = dailyLimit;
    }
}
