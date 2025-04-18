package model.animal;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import model.Salable;
import model.enums.Season;
import model.products.Product;

@Getter
@Setter
@ToString
public class Fish implements Salable {
    private String name;
    private FishType fishType;
    private Integer containingEnergy = 20; //TODO Energy not found
    private Season season;

    public int getSellPrice() {
        return this.fishType.getSellPrice();
    }
}