package io.github.some_example_name.common.model.relations;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.some_example_name.client.GameClient;
import io.github.some_example_name.common.model.*;
import io.github.some_example_name.common.model.dto.SpriteHolder;
import io.github.some_example_name.common.model.source.*;
import io.github.some_example_name.common.model.tools.*;
import io.github.some_example_name.common.utils.GameAsset;
import io.github.some_example_name.client.view.GameView;
import lombok.Getter;
import lombok.Setter;
import io.github.some_example_name.common.model.abilitiy.Ability;
import io.github.some_example_name.common.model.animal.Animal;
import io.github.some_example_name.common.model.craft.Craft;
import io.github.some_example_name.common.model.exception.InvalidInputException;
import io.github.some_example_name.common.model.receipe.CookingRecipe;
import io.github.some_example_name.common.model.receipe.CraftingRecipe;
import io.github.some_example_name.common.model.records.Response;
import io.github.some_example_name.common.model.shelter.ShippingBin;
import io.github.some_example_name.common.model.structure.farmInitialElements.Cottage;
import io.github.some_example_name.common.model.structure.stores.StoreType;
import io.github.some_example_name.server.saveGame.JsonPreparable;
import io.github.some_example_name.server.saveGame.ObjectMapWrapper;
import io.github.some_example_name.server.saveGame.ObjectWrapper;
import io.github.some_example_name.server.service.GameService;
import io.github.some_example_name.common.utils.App;
import io.github.some_example_name.client.view.Menu;

import java.util.*;

@Getter
@Setter
public class Player extends Actor implements JsonPreparable {
	private Integer id;
	private User user;
	private Integer energy;
	private Integer maxEnergy;
	private Boolean energyIsInfinite = false;
	private Integer daysOfSadness = 0;
	private BackPack inventory;
	private Buff buff;
	private Ability buffAbility;
    private Direction direction = Direction.SOUTH;
    private boolean dirChanged = false;
    private int isLazy = 0;
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
    private TrashCan currentTrashCan;
    private Boolean isWedding = false;
    private ArrayList<SpriteHolder> sprites = new ArrayList<>();
    private ArrayList<Notification<com.badlogic.gdx.scenes.scene2d.Actor, Actor>> notifications = new ArrayList<>();
	@JsonProperty("abilitiesMap")
	private ObjectMapWrapper abilitiesWrapper;

	@JsonProperty("cookingRecipesMap")
	private ObjectMapWrapper cookingRecipesWrapper;

	@JsonProperty("craftingRecipesMap")
	private ObjectMapWrapper craftingRecipesWrapper;

	@JsonProperty("currentCarryingWrapper")
	private ObjectWrapper currentCarryingWrapper;

	public Player() {
	}

	public Player(User user) {
		this.user = user;
		this.energy = 200;
		this.maxEnergy = 200;
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
    private PlayerType playerType;

	public void removeEnergy(int amount) {
		changeEnergy(-amount);
	}

    public void setPlayerType(PlayerType playerType) {
        this.playerType = playerType;
        sprites.add(new SpriteHolder(playerType.getLazy(Direction.SOUTH), new Tuple<>(0f, 0f)));
        sprites.get(0).setSize((float) App.tileWidth, (float) (App.tileHeight * 1.5));
        sprites.get(0).setChanged(true);
    }

    public void faint() {
        GameClient.getInstance().faint();
	}

    public void applyFaint() {
        this.sprites.get(0).setSprite(playerType.getFainting());
        ((AnimatedSprite)this.sprites.get(0).getSprite()).setRotationAnimation(
            new Animation<>(0.1f, 0f, 0f, 45f, 90f)
        );
        GameView.captureInput = false;
        isFainted = true;
        energy = 0;
    }

	public void changeEnergy(int currentEnergy) {
		if (energyIsInfinite) {
			energy = Math.max(maxEnergy, energy + currentEnergy);
		} else {
			energy = Math.min(Math.max(0, energy + currentEnergy), maxEnergy);
		}
		if (energy <= 0){
			faint();
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
		inventory.getProducts().put(Hoe.NORMAL, 1);
		inventory.getProducts().put(Pickaxe.IRIDIUM, 1);
		inventory.getProducts().put(Axe.NORMAL, 1);
		inventory.getProducts().put(new WateringCan(WateringCanType.NORMAL), 1);
		inventory.getProducts().put(new Scythe(), 1);
        inventory.getProducts().put(FishingPole.IRIDIUM,1);
        this.currentTrashCan = TrashCan.NORMAL;
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

	public void notify(Response response, NotificationType type, Actor source) {
        GameClient.getInstance().notifyPlayer(getUser().getUsername(), response, type, source);
	}

    public void getNotified(Response response, NotificationType type, Actor source) {
        notifications.add(new Notification<>(type, new Label(response.message(), GameAsset.SKIN), source));
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
        string = string.replace("_", " ");
		for (CraftingRecipe craftingRecipe : craftingRecipes.keySet()) {
			if (craftingRecipe.getName().equalsIgnoreCase(string + " recipe")) {
				return craftingRecipe;
			}
		}
		return null;
	}

	public CookingRecipe findCookingRecipe(String string) {
		for (CookingRecipe cookingRecipe : cookingRecipes.keySet()) {
			if (cookingRecipe.getName().equalsIgnoreCase(string)) {
				return cookingRecipe;
			}
		}
		return null;
	}

	public void addCraft(Craft craft) {
		crafts.add(craft);
	}

	public Craft findCraft(Salable craftType) {
		for (Craft craft : crafts) {
			if (craft.equals(craftType)) return craft;
		}
		return null;
	}

	@Override
	public void prepareForSave(ObjectMapper mapper) {
		this.abilitiesWrapper = new ObjectMapWrapper((Map<Object, Integer>) (Map<?, ?>) abilities, mapper);

		Map<Object, Integer> cookingAsInt = new HashMap<>();
		cookingRecipes.forEach((k, v) -> cookingAsInt.put(k, v ? 1:0));
		this.cookingRecipesWrapper = new ObjectMapWrapper(cookingAsInt, mapper);

		Map<Object, Integer> craftingAsInt = new HashMap<>();
		craftingRecipes.forEach((k, v) -> craftingAsInt.put(k, v ? 1:0));
		this.craftingRecipesWrapper = new ObjectMapWrapper(craftingAsInt, mapper);

		if (currentCarrying!=null)
			this.currentCarryingWrapper = new ObjectWrapper(currentCarrying, mapper);

		if (inventory != null)
			inventory.prepareForSave(mapper);
	}

	@Override
	public void unpackAfterLoad(ObjectMapper mapper) {
		this.abilities = (Map<Ability, Integer>) (Map<?, ?>) abilitiesWrapper.toMap(mapper);

		Map<Object, Integer> cookMap = cookingRecipesWrapper.toMap(mapper);
		this.cookingRecipes = new HashMap<>();
		cookMap.forEach((k, v) -> cookingRecipes.put((CookingRecipe) k, v==1));

		Map<Object, Integer> craftMap = craftingRecipesWrapper.toMap(mapper);
		this.craftingRecipes = new HashMap<>();
		craftMap.forEach((k, v) -> craftingRecipes.put((CraftingRecipe) k, v==1));

		if (currentCarryingWrapper!=null)
			this.currentCarrying = (Salable) currentCarryingWrapper.toObject(mapper);

		if (inventory != null)
			inventory.unpackAfterLoad(mapper);
	}

	public void goToCottage() {
		Cottage cottage = null;
		for (Farm farm : App.getInstance().getCurrentGame().getVillage().getFarms()) {
			if (!farm.getPlayers().isEmpty() && farm.getPlayers().get(0) == this) {
				if (getUser().getUsername().equals("beta2")) {
					int b= 5;
				}
				cottage = farm.getCottage();
			}
		}
		if (cottage == null) return;
		int x1 = cottage.getTiles().get(0).getX();
		int y1 = cottage.getTiles().get(0).getY();

		WalkingStrategy walkingStrategy = new WalkingStrategy();
		int energy = walkingStrategy.calculateEnergy(
				new Pair(this.getTiles().get(0).getX(), this.getTiles().get(0).getY()), new Pair(x1, y1)
		);
		if (this.getEnergy() < energy || energy == -1) {
			this.walkTillFaint(walkingStrategy.getDistances(), new Pair(x1, y1));
			this.faint();
			walkingStrategy.getDistances().clear();
			return;
		}
		this.getTiles().clear();
		walkingStrategy.getDistances().clear();
		this.getTiles().add(App.getInstance().getCurrentGame().tiles[x1][y1]);
		this.setCurrentMenu(Menu.COTTAGE);
	}

	public void walkTillFaint(Map<Pair, Integer> distances, Pair dest) {
		boolean flag = true;
		for (Pair pair : distances.keySet()) {
			if (pair.getX() == dest.getX() && pair.getY() == dest.getY()) {
				dest = pair;
				flag = false;
				break;
			}
		}
		if (flag) return;
		ArrayList<Pair> path = new ArrayList<>(Collections.nCopies(distances.get(dest) + 1, null));
		path.set(distances.get(dest), dest);
		Integer length = distances.get(dest);
		int[] xs = {1, 0, -1, 0, 1, 1, -1, -1};
		int[] ys = {0, 1, 0, -1, 1, -1, 1, -1};
		while (length >= 1) {
			length --;
			boolean flag1 = true;
			for (int i = 0; flag1 && i < 8; i++) {
				for (Pair pair : distances.keySet()) {
					if (pair.getX() == dest.getX() + xs[i] && pair.getY() == dest.getY() + ys[i] &&
                            Objects.equals(distances.get(pair), length)) {
						path.set(length, pair);
						dest = pair;
						flag1 = false;
						break;
					}
				}
			}
		}
		int max = Math.min(path.size() - 1, (energy)*20);
		getTiles().clear();
		getTiles().add(App.getInstance().getCurrentGame().getTiles()[path.get(max).getX()][path.get(max).getY()]);
	}

    public ArrayList<SpriteHolder> getSprites() {
        if (dirChanged) {
            ((AnimatedSprite)this.sprites.get(0).getSprite()).setRotationAnimation(null);
            dirChanged = false;
            this.sprites.get(0).setSprite(playerType.getWalking(direction));
        }
        return sprites;
    }

    public void setDirection(Direction direction) {
        if (((AnimatedSprite) this.sprites.get(0).getSprite()).isLooping() ||
            this.direction != direction) {
            dirChanged = true;
            isLazy++;
            int finalIsLazy = isLazy;
            com.badlogic.gdx.utils.Timer.schedule(new com.badlogic.gdx.utils.Timer.Task() {
                @Override
                public void run() {
                    if (isLazy == finalIsLazy) {
                        isLazy = 0;
                    }
                }
            }, 0.1f);
        }
        if (!isFainted) ((AnimatedSprite) this.sprites.get(0).getSprite()).setLooping(true);
        this.direction = direction;
    }
    public void setLazyDirection(Direction direction) {
        this.direction = direction;
        this.sprites.get(0).setSprite(playerType.getLazy(direction));
    }

    @Override
    public TextureRegion getAvatar() {
        return playerType.getAvatar();
    }

    @Override
    public String getName() {
        return user.getUsername();
    }

    public void setProposal() {
        this.direction = Direction.NORTH;
        this.sprites.get(0).setSprite(playerType.getProposal());
    }
}
