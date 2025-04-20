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
    private final Integer id;
    private final User user;
    private Integer energy = 200;
    private Integer maxEnergy = 200;
    private Integer energyPerTurn = 50;
    private Boolean energyIsInfinite = false;
    private BackPack inventory;
    private Buff buff;
    private Map<Ability, Integer> abilities;
    private List<ShippingBin> shippingBinList;
    private Account account;
    private List<Marry> marriage;
    private Player couple;
    private List<Trade> gootenTradeList;
    private Boolean isFainted;

    public Player(Integer id, User user) {
        this.id = id;
        this.user = user;
        //TODO initializing other fields
    }

    public void faint(){

    }
    public void changeEnergy(int currentEnergy){



    }

    public void resetEnergy(){

    }
}
