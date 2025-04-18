package model.structure.stores;

import lombok.Getter;

import java.lang.ref.PhantomReference;
@Getter
public enum StoreType {
    BLACK_SMITH("Clint",9,16),CARPENTER_SHOP("Robin",9,20),
    FISH_SHOP("Willy",9,17),JOJA_MART("Morris",9,23),
    PIERRE_SHOP("Pierre",9,17),STARDROPSALON("Gus",12,24),
    MARNIE_SHOP("Marnie",9,16);
    private final String shopperName;
    private final Integer openDoorTime;
    private final Integer closeDoorTime;

    StoreType(String shopperName, Integer openDoorTime, Integer closeDoorTime) {
        this.shopperName = shopperName;
        this.openDoorTime = openDoorTime;
        this.closeDoorTime = closeDoorTime;
    }
}
