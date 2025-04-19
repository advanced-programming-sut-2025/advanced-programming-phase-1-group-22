package model.tools;

import lombok.Getter;

@Getter
public enum BackPackType implements Tool {
    NORMAL_BACKPACK("normal backpack",12),
    BIG_BACKPACK("big backpack",24),
    DELUXE_BACKPACK("deluxe backpack",true);

    private final String name;
    private Integer capacity;
    private Boolean isInfinite = false;

    BackPackType(String name,int capacity) {
        this.name = name;
        this.capacity = capacity;
    }

    BackPackType(String name,boolean isInfinite) {
        this.name = name;
        this.isInfinite = isInfinite;
    }

    @Override
    public void addToolEfficiency(double efficiency) {
    }

    @Override
    public String getName() {
        return this.name.toLowerCase();
    }
}
