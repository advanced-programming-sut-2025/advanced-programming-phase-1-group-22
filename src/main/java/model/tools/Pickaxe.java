package model.tools;

import lombok.Getter;

@Getter
public enum Pickaxe implements Tool {
    NORMAL("normal pickaxe",0, 5),
    CUPPER("cupper pickaxe",1, 4),
    IRON("iron pickaxe",2, 3),
    GOLD("gold pickaxe",3, 2),
    IRIDIUM("iridium pickaxe",4, 1);

    private final String name;
    private final int level;
    private final int energyCost;

    Pickaxe(String name,int level1, int energyUse1) {
        this.name = name;
        this.level = level1;
        this.energyCost = energyUse1;
    }

    @Override
    public void addToolEfficiency(double efficiency) {

    }

    @Override
    public String getName() {
        return this.name.toLowerCase();
    }
}
