package model.relations;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import model.*;
import model.abilitiy.Ability;
import model.exception.InvalidInputException;
import model.receipe.CookingRecipe;
import model.receipe.CraftingRecipe;
import model.records.Response;
import model.shelter.ShippingBin;
import model.source.*;
import model.tools.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

@Getter
@Setter
public class Player extends Actor {
    private Integer id;
    private User user;
    private Integer energy;
    private Integer maxEnergy;
    private Integer energyPerTurn;
    private Boolean energyIsInfinite;
    private Integer maxEnergyPerTurn;
    private Integer daysOfSadness = 0;
    private BackPack inventory;
    private Buff buff;
    private Map<Ability, Integer> abilities = new HashMap<>();
    private ShippingBin shippingBin;
    private Account account = new Account();
    private List<ShippingBin> shippingBinList = new ArrayList<>();
    private Player couple;
    private List<Trade> gootenTradeList;
    private Boolean isFainted;
    private Salable currentCarrying = null;
    private List<CookingRecipe> cookingRecipes = new ArrayList<>();
    private List<CraftingRecipe> craftingRecipes = new ArrayList<>();

    public Player(User user) {
        this.user = user;
        this.energy = 200;
        this.maxEnergy = 200;
        this.energyPerTurn = 50;
        this.maxEnergyPerTurn = 50;
        this.inventory = new BackPack(BackPackType.NORMAL_BACKPACK);
        for (Ability ability : Ability.values()) {
            abilities.put(ability, 0);
        }
        addBasicTools();
    }

    private Pair position;
    private FarmType farmType;

    public void removeEnergy(int amount) {
        energyPerTurn -= amount;
        energy -= amount;
    }

    public void faint() {
        isFainted = true;
        energy = 0;
    }

    public void changeEnergy(int currentEnergy) {
        if (energyIsInfinite) {
            energy = Math.max(0, energy + currentEnergy);
        } else {
            energy = Math.min(Math.max(0, energy + currentEnergy), maxEnergy);
        }
    }

    public void resetEnergy() {
        maxEnergy = 200;
        if (isFainted) {
            energy = (int) (maxEnergy * 0.75);
            isFainted = false;
        } else {
            if (!energyIsInfinite) {
                energy = maxEnergy;
            }
        }
    }

    public void setEnergyIsInfinite(Boolean energyIsInfinite) {
        if (!energyIsInfinite) {
            energy = Math.min(energy, maxEnergy);
        }
        this.energyIsInfinite = energyIsInfinite;
    }

    public int getAbilityLevel(Ability ability) {
        for (Map.Entry<Ability, Integer> abilityIntegerEntry : abilities.entrySet()) {
            if (abilityIntegerEntry.getKey().equals(ability)) {
                int level = (abilityIntegerEntry.getValue() - 50) / 100;
                return Math.min(4, Math.max(level, 0));
            }
        }
        throw new InvalidInputException("abilityType is invalid");
    }

    public void upgradeAbility(Ability ability) {
        int oldValue = this.getAbilities().get(ability);
        this.getAbilities().put(ability, oldValue + ability.getUpgradeAbility());
        if (ability.equals(Ability.MINING) && this.getAbilities().get(Ability.MINING) >= 2) {
            this.getInventory().addProductToBackPack(generateRandomElement(), 1);
        }
    }

    private void addBasicTools() {
        inventory.getProducts().put(Hoe.NORMAL, 1);
        inventory.getProducts().put(Pickaxe.NORMAL, 1);
        inventory.getProducts().put(Axe.NORMAL, 1);
        inventory.getProducts().put(new WateringCan(WateringCanType.NORMAL), 1);
        inventory.getProducts().put(TrashCan.NORMAL, 1);
        inventory.getProducts().put(new Scythe(), 1);
    }

    private Salable generateRandomElement() {
        Random random = new Random();
        int witchType = random.nextInt(1, 3);

        switch (witchType) {
            case 1 -> {
                int foragingRandSeed = random.nextInt(0, 41);
                return new Seed(SeedType.values()[foragingRandSeed]);
            }
            case 2 -> {
                int cropRand = random.nextInt(0, 21);
                return new Crop(CropType.values()[cropRand]);
            }
            default -> {
                int mineralRand = random.nextInt(0, 21);
                return new Mineral(MineralType.values()[mineralRand]);
            }
        }
    }

    public void notify(Response response) {

    }

    public Map.Entry<Salable, Integer> getItemFromInventory(String name) {
        for (Map.Entry<Salable, Integer> salableIntegerEntry : this.getInventory().getProducts().entrySet()) {
            if (salableIntegerEntry.getKey().getName().equals(name)) {
                return salableIntegerEntry;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return user.toString();
    }

    public CraftingRecipe findCraftingRecipe(String string) {
        for (CraftingRecipe craftingRecipe : craftingRecipes) {
            if  (craftingRecipe.getName().equals(string)) return craftingRecipe;
        }
        return null;
    }
    public CookingRecipe findCookingRecipe(String string) {
        for (CookingRecipe cookingRecipe : cookingRecipes) {
            if  (cookingRecipe.getName().equals(string)) return cookingRecipe;
        }
        return null;
    }
}