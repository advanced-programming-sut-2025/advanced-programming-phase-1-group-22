package model.tools;

import lombok.Getter;

@Getter
public enum Hoe implements Tool {
    NORMAL("normal hoe",0, 5),
    CUPPER("cupper hoe",1, 4),
    IRON("iron hoe",2, 3),
    GOLD("gold hoe",3, 2),
    IRIDIUM("iridium hoe",4, 1);

    private final String name;
    private final int level;
    private final int energyCost;

    Hoe(String name,int level1, int energyUse1) {
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
