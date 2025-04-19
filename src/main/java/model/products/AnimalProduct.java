package model.products;

import lombok.Getter;
import lombok.Setter;
import model.Salable;

@Getter
@Setter
public class AnimalProduct implements Salable {
    private AnimalProductType animalProductType;

    public AnimalProduct(AnimalProductType animalProductType) {
        this.animalProductType = animalProductType;
    }

    @Override
    public String getName() {
        return this.animalProductType.getName();
    }
}
