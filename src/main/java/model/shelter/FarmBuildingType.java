package model.shelter;

import lombok.Getter;
import lombok.Setter;

@Getter
public enum FarmBuildingType {
    BARN(4, 7, 4),
    BIG_BARN(8, 7, 4),
    DELUXE_BARN(12, 7, 4),
    COOP(4, 6, 3),
    BIG_COOP(8, 6, 3),
    DELUXE_COOP(12, 6, 3),
    WELL(10,3,3),
    SHIPPING_BIN(20,1,1);
    private final Integer capacity;
    private final Integer height;
    private final Integer width;

    FarmBuildingType(Integer capacity, Integer height, Integer width) {
        this.capacity = capacity;
        this.height = height;
        this.width = width;
    }
}
