package model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import model.abilitiy.Ability;
import model.shelter.ShippingBin;
import model.tools.BackPack;

import java.util.List;
import java.util.Map;
@Getter
@Setter
@ToString
public class Player extends Actor {
    private Integer id;
    public User user;
    private Integer energy;
    private Integer maxEnergy;
    private Integer energyPerTurn;
    private Boolean energyIsInfinite;
    private BackPack inventory;
    private Buff buff;
    private Map<Ability, Integer> abilities;
    private ShippingBin shippingBin;
    private Account account = new Account();
    private List<Marry> marriage;
    private Player couple;
    private List<Trade> gootenTradeList;
    private Boolean isFainted;

    public void faint(){
        isFainted = true;
        energy = 0;
    }

    public void changeEnergy(int currentEnergy){
        if (energyIsInfinite){
            energy += currentEnergy;
        }
        else {
            energy = Math.min(energy + currentEnergy,maxEnergy);
        }
    }

    public void resetEnergy(){
        maxEnergy = 200;
        if (isFainted){
            energy = (int) (maxEnergy * 0.75);
            isFainted = false;
        }
        else {
            if (!energyIsInfinite){
                energy = maxEnergy;
            }
        }
    }

    public void setEnergyIsInfinite(Boolean energyIsInfinite) {
        if (!energyIsInfinite){
            energy = Math.min(energy,maxEnergy);
        }
        this.energyIsInfinite = energyIsInfinite;
    }
}