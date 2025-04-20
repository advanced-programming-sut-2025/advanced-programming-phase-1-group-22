package model.tools;

import lombok.Getter;

@Getter
public enum Axe implements Tool{
    NORMAL("normal axe",0, 5),
    CUPPER("cupper axe",1, 4),
    IRON("iron axe",2, 3),
    GOLD("gold axe",3, 2),
    IRIDIUM("iridium axe",4, 1);

    private final String name;
    private final int level;
    private final int energyCost;

    Axe(String name,int level1, int energyUse1) {
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

    @Override
    public int getSellPrice() {
        return 0;
    }
}
