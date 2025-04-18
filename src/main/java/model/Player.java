package model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import model.abilitiy.Ability;
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
    private List<ShippingBin> shippingBinList;
    private Account account;
    private List<Marry> marriage;
    private Player couple;
    private List<Trade> gootenTradeList;
    private Boolean isFainted;
    public void faint(){

    }
    public void changeEnergy(int currentEnergy){



    }

    public void resetEnergy(){

    }
}
