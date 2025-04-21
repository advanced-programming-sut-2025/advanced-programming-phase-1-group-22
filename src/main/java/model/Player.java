package model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import model.abilitiy.Ability;
import model.exception.InvalidInputException;
import model.products.TreesAndFruitsAndSeeds.Tree;
import model.products.TreesAndFruitsAndSeeds.TreeType;
import model.shelter.ShippingBin;
import model.source.*;
import model.structure.Stone;
import model.structure.StoneType;
import model.structure.Trunk;
import model.structure.TrunkType;
import model.tools.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

@Getter
@Setter
@ToString
public class Player extends Actor {
    private Integer id;
    private User user;
    private Integer energy;
    private Integer maxEnergy;
    private Integer energyPerTurn;
    private Boolean energyIsInfinite;
    private final Integer id;
    private final User user;
    private Integer energy = 200;
    private Integer maxEnergy = 200;
    private Integer energyPerTurn = 50;
    private Integer maxEnergyPerTurn = 50;
    private Boolean energyIsInfinite = false;
    private BackPack inventory;
    private Buff buff;
    private Map<Ability, Integer> abilities = new HashMap<>();
    private ShippingBin shippingBin;
    private Account account = new Account();
    private List<Marry> marriage;
    private Map<Ability, Integer> abilities = new HashMap<>();
    private List<ShippingBin> shippingBinList = new ArrayList<>();
    private Account account;
    private List<Marry> marriage = new ArrayList<>();
    private Player couple;
    private List<Trade> gootenTradeList;
    private Boolean isFainted;
    private Salable currentCarrying = null;

    public Player(User user) {
        this.user = user;
        this.energy = 200;
        this.maxEnergy = 200;
        this.energyPerTurn = 50;
        this.inventory = new BackPack(BackPackType.NORMAL_BACKPACK);
        abilities.put(Ability.FARMING,0);
        abilities.put(Ability.FORAGING,0);
        abilities.put(Ability.FISHING,0);
        abilities.put(Ability.MINING,0);
        addBasicTools();
    }

    private Pair position;
    private FarmType farmType;

    public Player(Integer id, User user) {
        this.id = id;
        this.user = user;
        inventory = new BackPack();
        for (Ability ability : Ability.values()) {
            abilities.put(ability, 0);
        }
        account = new Account();
    }

    public void removeEnergy(int amount) {
        energyPerTurn -= amount;
        energy -= amount;
    }

    public void faint(){
        isFainted = true;
        energy = 0;
    }

    public void changeEnergy(int currentEnergy){
        if (energyIsInfinite){
            energy = Math.max(0,energy + currentEnergy);
        }
        else {
            energy = Math.min(Math.max(0,energy + currentEnergy),maxEnergy);
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

    public int getAbilityLevel(Ability ability){
        for (Map.Entry<Ability, Integer> abilityIntegerEntry : abilities.entrySet()) {
            if (abilityIntegerEntry.getKey().equals(ability)){
                int level = (abilityIntegerEntry.getValue() - 50) / 100;
				return Math.min(4,Math.max(level, 0));
			}
        }
        throw new InvalidInputException("abilityType is invalid");
    }

    public void upgradeAbility(Ability ability){
        int oldValue = this.getAbilities().get(ability);
        this.getAbilities().put(ability,oldValue + ability.getUpgradeAbility());
        if (ability.equals(Ability.MINING) && this.getAbilities().get(Ability.MINING) >= 2){
            this.getInventory().addProductToBackPack(generateRandomElement(),1);
        }
    }

    private void addBasicTools(){
        inventory.getProducts().put(Hoe.NORMAL,1);
        inventory.getProducts().put(Pickaxe.NORMAL,1);
        inventory.getProducts().put(Axe.NORMAL,1);
        inventory.getProducts().put(new WateringCan(WateringCanType.NORMAL),1);
        inventory.getProducts().put(TrashCan.NORMAL,1);
        inventory.getProducts().put(new Scythe(),1);
    }

    private Salable generateRandomElement(){
        Random random = new Random();
        int witchType = random.nextInt(1,3);

        switch (witchType){
            case 1 ->{
                int foragingRandSeed = random.nextInt(0, 41);
				return new Seed(SeedType.values()[foragingRandSeed]);
            }
            case 2 ->{
                int cropRand = random.nextInt(0, 21);
				return new Crop(CropType.values()[cropRand]);
            }
            default ->{
                int mineralRand = random.nextInt(0,21);
				return new Mineral(MineralType.values()[mineralRand]);
            }
        }
    }
}