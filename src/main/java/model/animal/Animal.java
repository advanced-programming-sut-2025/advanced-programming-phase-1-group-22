package model.animal;

import lombok.Getter;
import lombok.Setter;
import model.Player;
import model.Salable;
import model.TimeAndDate;
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

}