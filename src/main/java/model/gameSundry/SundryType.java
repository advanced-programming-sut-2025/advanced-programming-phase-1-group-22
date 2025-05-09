package model.gameSundry;

import lombok.Getter;
import model.Salable;
import model.products.Product;

import java.io.Serializable;

@Getter
public enum SundryType implements SalableIntegration , Serializable {
    RICE("Rice",200 / 2),
    WHEAT_FLOUR("Wheat Flour",100 / 2),
    BOUQUET("Bouquet",1000 / 2),
    WEDDING_RING("Wedding Ring",10000 / 2),
    SUGAR("Sugar",100 / 2),
    VINEGAR("Vinegar",200 / 2),
    DELUXE_RETAINING_SOIL("Deluxe Retaining Soil",150 / 2),
    QUALITY_RETAINING_SOIL("Quality Retaining Soil",150 / 2),
    BASIC_RETAINING_SOIL("Basic Retaining Soil",100 / 2),
    GRASS_STARTER("Grass Starter",100 / 2),
    SPEED_GROW("Speed Grow",100 / 2),

    ;
    private String name;
    private Integer price;

    SundryType() {
    }

    SundryType(String name, Integer price) {
        this.name = name;
        this.price = price;
    }

    @Override
    public int getSellPrice() {
        return this.price;
    }

    @Override
    public Integer getContainingEnergy() {return 0;}
}
