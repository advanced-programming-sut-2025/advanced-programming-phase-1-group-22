package model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import model.animal.Animal;
import model.enums.Weather;
import model.gameSundry.SundryType;
import model.products.HarvestAbleProduct;
import model.products.TreesAndFruitsAndSeeds.Tree;
import model.relations.Friendship;
import model.relations.NPC;
import model.relations.Player;
import model.structure.Structure;
import org.hibernate.sql.ast.tree.from.TableReference;
import utils.App;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Getter
@Setter
@ToString
public class Game {
	private Village village;
	private final List<Player> players = new ArrayList<>();
	private Player currentPlayer;
	private final List<NPC> npcs = new ArrayList<>();
	private final List<Friendship> friendships = new ArrayList<>();
	private TimeAndDate timeAndDate;
	private Double weatherCoefficient;
	private final Integer length = 160;
	private final Integer width = 120;
	private int playersInFavorTermination = 0;
	public Tile[][] tiles = new Tile[length][width];

	public void start() {
		timeAndDate = new TimeAndDate();
		for (int i = 0; i < 160; i++) {
			for (int i1 = 0; i1 < 120; i1++) {
				tiles[i][i1] = new Tile(i, i1);
			}
		}
	}

	public void startDay() {
		manageHarvest();
		TimeAndDate timeAndDate = this.getTimeAndDate();
		Weather tomorrowWeather = this.getVillage().getTomorrowWeather();
		Weather weather = this.getVillage().getWeather();
		this.getVillage().setWeather(tomorrowWeather);
		Random random = new Random();
		do {
			tomorrowWeather = Weather.values()[random.nextInt(0, 3)];
		} while (tomorrowWeather.getSeasons().contains(timeAndDate.getSeason()));
		if (weather==Weather.STORMY) {
			for (Farm farm : this.village.getFarms()) {
				for (int i = 0; i < 3; i++) {
					int randX = random.nextInt(farm.getFarmXStart(), farm.getFarmXEnd());
					int randY = random.nextInt(farm.getFarmYStart(), farm.getFarmYEnd());
					weather.thunderBolt(randX, randY);
				}
				int randX = random.nextInt(farm.getFarmXStart(), farm.getFarmXEnd());
				int randY = random.nextInt(farm.getFarmYStart(), farm.getFarmYEnd());
				weather.breakTree(randX, randY);
			}
		}
		automaticWatering(this.village.getWeather());
		setWeatherCoefficientEveryDay();
		for (Farm farm : this.getVillage().getFarms()) {
			crowAttack(farm);
		}
		for (Player player : players) {
			player.resetEnergy();
			addGoldToPlayerForShippingBin(player.getShippingBin().CalculatePriceOfShippingBinProducts(), player);
		}
		for (Farm farm : App.getInstance().getCurrentGame().getVillage().getFarms()) {
			farm.generateRandomForaging();
		}
		for (Farm farm : App.getInstance().getCurrentGame().getVillage().getFarms()) {
			for (Structure structure : farm.getStructures()) {
				if (structure instanceof Animal) {
					((Animal) structure).produceAnimalProduct();
					calculateAnimalFriendShip((Animal) structure);
				}
			}
		}
	}

	public void addGoldToPlayerForShippingBin(int price, Player player) {
		int oldGold = player.getAccount().getGolds();
		player.getAccount().setGolds(oldGold + price);
	}

	public void addPlayer(Player player) {
		for (int i = 0; i < players.size(); i++) {
			friendships.add(new Friendship(players.size() * (players.size() - 1) / 2 + i + 1, players.get(i), player));
		}
		for (int i = 0; i < npcs.size(); i++) {
			friendships.add(new Friendship(20 + 10 * players.size() + i, player, npcs.get(i)));
		}
		players.add(player);
	}

	public void addNPC(NPC npc) {

		npcs.add(npc);
	}

	public void addTermination() {
		playersInFavorTermination++;
	}

	public void nextPlayer() {
		int i;
		for (i = 0; i < players.size(); i++) {
			if (currentPlayer==players.get(i)) break;
		}
		i = (i==players.size() - 1) ? 0:i + 1;
		currentPlayer = players.get(i);
		timeAndDate.moveTimeForward();
	}

	public Farm findFarm() {
		Tile tile = currentPlayer.getTiles().getFirst();
		for (Farm farm : getVillage().getFarms()) {
			if (farm.isPairInFarm(new Pair(tile.getX(), tile.getY()))) {
				return farm;
			}
		}
		return null;
	}

	private void calculateAnimalFriendShip(Animal animal) {
		if (!animal.getIsFeed()) {
			changeFriendShip(animal, -20);
		}
		if (animal.getIsAnimalStayOutAllNight()) {
			changeFriendShip(animal, -20);
		}
		if (!animal.getPet()) {
			changeFriendShip(animal, -10);
		}
		animal.setIsFeed(false);
		animal.setPet(false);
	}

	private void changeFriendShip(Animal animal, int value) {
		int oldValue = animal.getRelationShipQuality();
		animal.setRelationShipQuality(oldValue + value);
	}

	private void automaticWatering(Weather weather) {
		if (weather.equals(Weather.RAINY) || weather.equals(Weather.STORMY)) {
			for (Farm farm : App.getInstance().getCurrentGame().getVillage().getFarms()) {
				for (Structure structure : farm.getStructures()) {
					if (structure instanceof HarvestAbleProduct &&
							!((HarvestAbleProduct) structure).getInGreenHouse()) {
						((HarvestAbleProduct) structure).setWaterToday(true);
					}
				}
			}
		}
	}

	private void manageHarvest() {
		for (Farm farm : this.getVillage().getFarms()) {
			for (Structure structure : farm.getStructures()) {
				if (structure instanceof HarvestAbleProduct harvestAbleProduct) {
					if (!harvestAbleProduct.getIsWaterToday() && !harvestAbleProduct.getAroundSprinkler()) {
						int oldNumber = harvestAbleProduct.getNumberOfWithoutWaterDays();
						harvestAbleProduct.setNumberOfWithoutWaterDays(oldNumber + 1);
						if (harvestAbleProduct.getNumberOfWithoutWaterDays() >= 2) {
							this.getVillage().removeStructure(harvestAbleProduct);
							for (Tile tile : structure.getTiles()) {
								tile.setTileType(TileType.FLAT);
							}
						}
					} else {
						harvestAbleProduct.setNumberOfWithoutWaterDays(0);
					}
					if (!harvestAbleProduct.getAroundSprinkler() && !canHoldWater(harvestAbleProduct)) {
						harvestAbleProduct.setWaterToday(false);
					}
					if (harvestAbleProduct instanceof Tree) {
						((Tree) harvestAbleProduct).setAttackByCrow(false);
					}
				}
			}
		}
	}

	private void setWeatherCoefficientEveryDay() {
		double weatherCoefficient = 1.0;
		if (this.getVillage().getWeather().equals(Weather.SNOWY)) {
			weatherCoefficient = 2.0;
		} else if (this.getVillage().getWeather().equals(Weather.RAINY) ||
				this.getVillage().getWeather().equals(Weather.STORMY)) {
			weatherCoefficient = 1.5;
		}
		this.weatherCoefficient = weatherCoefficient;
	}

	private void crowAttack(Farm farm) {
		int numberOfHarvest = 0;
		List<HarvestAbleProduct> harvestAbleProducts = new ArrayList<>();
		for (Structure structure : farm.getStructures()) {
			if (structure instanceof HarvestAbleProduct) {
				harvestAbleProducts.add((HarvestAbleProduct) structure);
				numberOfHarvest += 1;
			}
		}
		int numberOfCrow = numberOfHarvest / 16;
		for (int i = 0; i < numberOfCrow; i++) {
			Random random = new Random();
			Random random1 = new Random();
			if (random.nextInt() % 4==0) {
				HarvestAbleProduct harvestAbleProduct = harvestAbleProducts.get(random1.nextInt() % harvestAbleProducts.size());
				if (!harvestAbleProduct.getAroundScareCrow() && !harvestAbleProduct.getInGreenHouse()) {
					if (harvestAbleProduct instanceof Tree) {
						((Tree) harvestAbleProduct).setAttackByCrow(true);
					} else {
						App.getInstance().getCurrentGame().getVillage().removeStructure(harvestAbleProduct);
					}
				}
			}
		}
	}

	private boolean canHoldWater(HarvestAbleProduct harvestAbleProduct) {
		List<SundryType> sundryTypes = harvestAbleProduct.getFertilizes();
		if (sundryTypes.contains(SundryType.DELUXE_RETAINING_SOIL)) return true;
		Random random = new Random();
		int rand = random.nextInt();
		if (sundryTypes.contains(SundryType.QUALITY_RETAINING_SOIL)) {
			return rand % 4!=3;
		}
		if (sundryTypes.contains(SundryType.BASIC_RETAINING_SOIL)) {
			return rand % 4==3;
		}
		return false;
	}
}