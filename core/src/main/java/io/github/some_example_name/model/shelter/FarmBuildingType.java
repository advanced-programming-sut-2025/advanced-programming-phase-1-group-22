package io.github.some_example_name.model.shelter;

import lombok.Getter;
import lombok.Setter;
import io.github.some_example_name.model.structure.farmInitialElements.Cottage;

@Getter
public enum FarmBuildingType {
    BARN(4, 7, 4,true,false),
    BIG_BARN(8, 7, 4,true,false),
    DELUXE_BARN(12, 7, 4,true,false),
    COOP(4, 6, 3,false,true),
    BIG_COOP(8, 6, 3,false,true),
    DELUXE_COOP(12, 6, 3,false,true),
    WELL(10,3,3,false,false),
    SHIPPING_BIN(20,1,1,false,false),
    Cottage(-1,6,6,false,false),
    GreenHouse(-1,8,7,false,false),
    Lake(-1,-1,-1,false,false),
    Quarry(-1,10,10,false,false);

    private final Integer capacity;
    private final Integer height;
    private final Integer width;
    private final Boolean isBarn;
    private final Boolean isCoop;

    FarmBuildingType(Integer capacity, Integer height, Integer width,Boolean isBarn,Boolean isCoop) {
        this.capacity = capacity;
        this.height = height;
        this.width = width;
        this.isBarn = isBarn;
        this.isCoop = isCoop;
    }
}
