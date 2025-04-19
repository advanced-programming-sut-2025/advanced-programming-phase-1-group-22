package model.tools;

import lombok.Getter;

@Getter
public class MilkPail implements Tool {
    private static MilkPail instance;

    private MilkPail() {
    }

    public static MilkPail getInstance() {
        if (instance == null) {
            instance = new MilkPail();
        }
        return instance;
    }

    private final Integer energyUse = 4;
    private final Integer price = 100;

    @Override
    public void addToolEfficiency(double efficiency) {

    }

    @Override
    public String getName() {
        return "milkpail";
    }
}
