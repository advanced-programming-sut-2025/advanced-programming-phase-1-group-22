package model.products;

import lombok.Getter;
import lombok.Setter;
import model.Salable;
import model.structure.Structure;

@Getter
@Setter
public class AnimalProduct extends Structure implements Salable {
    private AnimalProductType animalProductType;

    public AnimalProduct(AnimalProductType animalProductType) {
        this.animalProductType = animalProductType;
    }

    @Override
    public String getName() {
        return this.animalProductType.getName();
    }

    @Override
    public int getSellPrice() {
        return animalProductType.getSellPrice();
    }
}