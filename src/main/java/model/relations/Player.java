package model.relations;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import model.*;
import model.abilitiy.Ability;
import model.animal.Animal;
import model.craft.Craft;
import model.exception.InvalidInputException;
import model.gameSundry.Sundry;
import model.gameSundry.SundryType;
import model.receipe.CookingRecipe;
import model.receipe.CraftingRecipe;
import model.records.Response;
import model.shelter.ShippingBin;
import model.source.*;
import model.structure.stores.PierreShop;
import model.structure.stores.StoreType;
import model.tools.*;
import view.Menu;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import static model.source.MineralType.WOOD;

@Getter
@Setter
@ToString
public class Player extends Actor {
    private Integer id;
    private User user;
    private Integer energy;
    private Integer maxEnergy;
    private Integer energyPerTurn;
    private Boolean energyIsInfinite = false;
    private Integer maxEnergyPerTurn;
    private Integer daysOfSadness = 0;
    private BackPack inventory;
    private Buff buff;
    private Ability buffAbility;
    private Map<Ability, Integer> abilities = new HashMap<>();
    private Account account = new Account();
    private List<ShippingBin> shippingBinList = new ArrayList<>();
    private Player couple;
    private List<Trade> gootenTradeList = new ArrayList<>();
    private Boolean isFainted = false;
    private Salable currentCarrying = null;
    private List<Animal> animals = new ArrayList<>();
    private Map<CookingRecipe, Boolean> cookingRecipes = new HashMap<>();
    private List<Craft> crafts = new ArrayList<>();
    private Map<CraftingRecipe, Boolean> craftingRecipes = new HashMap<>();
    private StoreType storeType;
    private Menu currentMenu = Menu.COTTAGE;

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
        for (CookingRecipe value : CookingRecipe.values()) {
            cookingRecipes.put(value, false);
        }
        cookingRecipes.put(CookingRecipe.FRIED_EGG_RECIPE, true);
        cookingRecipes.put(CookingRecipe.BAKED_FISH_RECIPE, true);
        cookingRecipes.put(CookingRecipe.SALAD_RECIPE, true);
        for (CraftingRecipe value : CraftingRecipe.values()) {
            craftingRecipes.put(value, false);
        }
        craftingRecipes.put(CraftingRecipe.MAYONNAISE_MACHINE_RECIPE, true);
        craftingRecipes.put(CraftingRecipe.FURNACE_RECIPE, true);
        craftingRecipes.put(CraftingRecipe.SCARECROW_RECIPE, true);
        addBasicTools();
    }

    private Pair position;
    private FarmType farmType;

    public void removeEnergy(int amount) {
        changeEnergy(-amount);
    }

    public void faint() {
        isFainted = true;
        energy = 0;
    }

    public void changeEnergy(int currentEnergy) {
        if (energyIsInfinite) {
            energy = Math.max(maxEnergy, energy + currentEnergy);
        } else {
            energyPerTurn += currentEnergy;
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
        this.energyIsInfinite = energyIsInfinite;
        if (!energyIsInfinite) {
            energy = Math.min(energy, maxEnergy);
            return;
        }

        energy = maxEnergy;
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
        this.getAccount().setGolds(10);
        inventory.getProducts().put(new Flower(), 1);
        inventory.getProducts().put(WOOD, 100);
        inventory.getProducts().put(new Sundry(SundryType.WEDDING_RING), 1);
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
        for (CraftingRecipe craftingRecipe : craftingRecipes.keySet()) {
            if (craftingRecipe.getName().equalsIgnoreCase(string + " recipe")) {
                if (craftingRecipes.get(craftingRecipe)) return craftingRecipe;
                return null;
            }
        }
        return null;
    }

    public CookingRecipe findCookingRecipe(String string) {
        for (CookingRecipe cookingRecipe : cookingRecipes.keySet()) {
            if (cookingRecipe.getName().equals(string)) {
                if (cookingRecipes.get(cookingRecipe)) return cookingRecipe;
                return null;
            }
        }
        return null;
    }

    public void addCraft(Craft craft) {
        crafts.add(craft);
    }

    public Craft findCraft(Salable craftType) {
        for (Craft craft : crafts) {
            if (craft.getCraftType().equals(craftType)) return craft;
        }
        return null;
    }
}