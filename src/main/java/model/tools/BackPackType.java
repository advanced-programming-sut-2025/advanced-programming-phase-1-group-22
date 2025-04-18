package model.tools;

import lombok.Getter;

@Getter
public enum BackPackType implements Tool {
    NORMAL_BAKCPACK(12), BIG_BACKPACK(24), DELUX_BACKPACK(true);
    private Integer capacity;
    private Boolean isInfinite = false;

    BackPackType(int capacity) {
        this.capacity = capacity;
    }

    BackPackType(boolean isInfinite) {
        this.isInfinite = isInfinite;
    }

    @Override
    public void addToolEfficiency(double efficiency) {
    }
}
