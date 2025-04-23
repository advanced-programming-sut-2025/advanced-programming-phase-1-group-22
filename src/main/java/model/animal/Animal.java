package model.animal;

import lombok.Getter;
import lombok.Setter;
import model.relations.Player;
import model.Salable;
import model.TimeAndDate;
import model.products.AnimalProduct;
import model.structure.Structure;

@Getter
@Setter
public class Animal extends Structure implements Salable {
    private AnimalType animalType;
    private TimeAndDate lastProductGotten;
    private Integer relationShipQuality;
    private Boolean isFeed;
    private Integer tet;
    private Player owner;
    private Boolean milk = false;
    private Boolean shaving = false;
    private Boolean grassEaten = false;

    @Override
    public String getName() {
        return this.animalType.getName();
    }

    @Override
    public int getSellPrice() {
        return 0;
    }

    public AnimalProduct getAnimalProduct(){
        //TODO
        //it is just a sample
        return new AnimalProduct(animalType.getProductList().get(0));
    }
}