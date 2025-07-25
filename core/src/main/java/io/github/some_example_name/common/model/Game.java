package io.github.some_example_name.common.model;

import com.badlogic.gdx.utils.Timer;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import io.github.some_example_name.client.GameClient;
import io.github.some_example_name.client.view.GameView;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import io.github.some_example_name.common.model.structure.stores.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import io.github.some_example_name.common.model.animal.Animal;
import io.github.some_example_name.common.model.enums.Weather;
import io.github.some_example_name.common.model.gameSundry.SundryType;
import io.github.some_example_name.common.model.products.HarvestAbleProduct;
import io.github.some_example_name.common.model.products.TreesAndFruitsAndSeeds.Tree;
import io.github.some_example_name.common.model.relations.Friendship;
import io.github.some_example_name.common.model.relations.Mission;
import io.github.some_example_name.common.model.relations.NPC;
import io.github.some_example_name.common.model.relations.Player;
import io.github.some_example_name.common.model.shelter.ShippingBin;
import io.github.some_example_name.common.model.structure.Structure;
import io.github.some_example_name.common.utils.App;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Setter
@Getter
@ToString
@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class, property = "@id")
public class Game implements Serializable {
//    @JsonManagedReference
    private Village village;
    private final List<Player> players = new ArrayList<>();
    private Player currentPlayer;
    private final List<NPC> npcs = new ArrayList<>();
    private final List<Friendship> friendships = new ArrayList<>();
//    @JsonManagedReference
    private TimeAndDate timeAndDate;
    private Double weatherCoefficient = 1.0;
    private final Integer length = 160;
    private final Integer width = 120;
    private int playersInFavorTermination = 0;
    public Tile[][] tiles = new Tile[length][width];
    private int fadingInTheNight = 0;

    public void start() {
        timeAndDate = new TimeAndDate(1, 9);
        for (int i = 0; i < 160; i++) {
            for (int i1 = 0; i1 < 120; i1++) {
                tiles[i][i1] = new Tile(i, i1);
            }
        }
    }

    public void startDay() {
        fadingInTheNight = 1;
        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                fadingInTheNight = 2;
                GameClient.getInstance().readyForSleep();
            }
        }, 2.5f);
    }

    public void startDayEvents(int tomorrowWeatherByServer) {
        App.getInstance().getCurrentGame().getTimeAndDate().resetDay();
        App.getInstance().getCurrentGame().getTimeAndDate().setWeatherSprite(
            App.getInstance().getCurrentGame().getVillage().getTomorrowWeather()
        );
        App.getInstance().getCurrentGame().getTimeAndDate().setSeasonSprite();
        for (Farm farm : this.village.getFarms()) {
            for (Tile tile : farm.getTiles()) {
                if (tile.getTileType().equals(TileType.THUNDERED)) {
                    tile.setTileType(TileType.FLAT);
                }
            }
        }
        for (Friendship friendship : this.getFriendships()) {
            if (friendship.getLastSeen().getDay().equals(this.getTimeAndDate().getDay())) {
                friendship.setXp(friendship.getXp() + -10);
                if (friendship.getXp() <= 0) {
                    friendship.setXp(90);
                    friendship.setFriendShipLevel(friendship.getFriendShipLevel() - 1);
                }
            }
        }
        for (Structure structure : App.getInstance().getCurrentGame().getVillage().getStructures()) {
            if (structure instanceof NPC npc) {
                npc.setGiftedToday(false);
            }
        }
        giveRewardToLevelThreeFriends();
        manageHarvest();
        Weather tomorrowWeather = this.getVillage().getTomorrowWeather();
        Weather weather = this.getVillage().getWeather();
        this.getVillage().setWeather(tomorrowWeather);
        this.getVillage().setTomorrowWeather(Weather.values()[tomorrowWeatherByServer]);
        Random random = new Random();
        if (this.getVillage().getWeather() == Weather.STORMY) {
            for (Farm farm : this.village.getFarms()) {
                for (int i = 0; i < 3; i++) {
                    int randX = random.nextInt(farm.getFarmXStart(), farm.getFarmXEnd());
                    int randY = random.nextInt(farm.getFarmYStart(), farm.getFarmYEnd());
                    weather.thunderBolt(randX, randY);
                }
            }
        }
        automaticWatering(this.village.getWeather());
        setWeatherCoefficientEveryDay();
        for (Farm farm : this.getVillage().getFarms()) {
            farm.setCrowAttackToday(false);
        }
        for (Player player : players) {
            if (!player.getIsFainted()) player.goToCottage();
            player.resetEnergy();
            int total = player.getShippingBinList().stream()
                .mapToInt(ShippingBin::CalculatePriceOfShippingBinProducts)
                .sum();
            addGoldToPlayerForShippingBin(total, player);

            if (player.getBuff() != null) {
                player.getBuff().defectBuff(player);
                player.setBuff(null);
            }
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
        for (BlackSmithStuff value : BlackSmithStuff.values()) {
            value.resetDailySold();
        }
        for (BlackSmithUpgrade value : BlackSmithUpgrade.values()) {
            value.resetDailySold();
        }
        for (CarpenterShopFarmBuildings value : CarpenterShopFarmBuildings.values()) {
            value.resetDailySold();
        }
        for (CarpenterShopMineralStuff value : CarpenterShopMineralStuff.values()) {
            value.resetDailySold();
        }
        for (FishShopStuff value : FishShopStuff.values()) {
            value.resetDailySold();
        }
        for (JojaMartShopSeed value : JojaMartShopSeed.values()) {
            value.resetDailySold();
        }
        for (MarnieShopAnimalRequierment value : MarnieShopAnimalRequierment.values()) {
            value.resetDailySold();
        }
        for (MarnieShopAnimal value : MarnieShopAnimal.values()) {
            value.resetDailySold();
        }
        for (PierreShop value : PierreShop.values()) {
            value.resetDailySold();
        }
        for (TheStardropSaloonStuff value : TheStardropSaloonStuff.values()) {
            value.resetDailySold();
        }
        for (Player player : this.getPlayers()) {
            if (player.getDaysOfSadness() > 0) {
                player.setEnergy(player.getEnergy() / 2);
            } else {
                player.setDaysOfSadness(player.getDaysOfSadness() - 1);
            }
        }
        GameView.captureInput = true;
        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                fadingInTheNight = 0;
            }
        }, 4);
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
            if (currentPlayer == players.get(i)) break;
        }
        i = (i == players.size() - 1) ? 0 : i + 1;
        currentPlayer = players.get(i);
        timeAndDate.moveTimeForward();
    }

    public Farm findFarm() {
        Tile tile = currentPlayer.getTiles().get(0);
        for (Farm farm : getVillage().getFarms()) {
            if (farm.isPairInFarm(new Pair(tile.getX(), tile.getY()))) {
                return farm;
            }
        }
        return null;
    }

    private void calculateAnimalFriendShip(Animal animal) {
        if (!animal.getIsFeed()) {
            animal.changeFriendShip(-20);
        }
        if (animal.getIsAnimalStayOutAllNight()) {
            animal.changeFriendShip(-20);
        }
        if (!animal.getPet()) {
            animal.changeFriendShip(-10);
        }
        animal.setIsFeed(false);
        animal.setPet(false);
    }

    public void automaticWatering(Weather weather) {
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
            List<Structure> structures = new ArrayList<>(farm.getStructures());
            for (Structure structure : structures) {
                if (structure instanceof HarvestAbleProduct harvestAbleProduct) {
                    if (!harvestAbleProduct.getIsWaterToday() && !harvestAbleProduct.getAroundSprinkler()) {
                        int oldNumber = harvestAbleProduct.getNumberOfWithoutWaterDays();
                        harvestAbleProduct.setNumberOfWithoutWaterDays(oldNumber + 1);
                        if (harvestAbleProduct.getNumberOfWithoutWaterDays() >= 2) {
                            App.getInstance().getCurrentGame().getVillage().removeStructure(harvestAbleProduct);
                            for (Tile tile : structure.getTiles()) {
                                tile.setTileType(TileType.FLAT);
                            }
                            continue;
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

    private boolean canHoldWater(HarvestAbleProduct harvestAbleProduct) {
        List<SundryType> sundryTypes = harvestAbleProduct.getFertilizes();
        if (sundryTypes.contains(SundryType.DELUXE_RETAINING_SOIL)) return true;
        Random random = new Random();
        int rand = random.nextInt();
        if (sundryTypes.contains(SundryType.QUALITY_RETAINING_SOIL)) {
            return rand % 4 != 3;
        }
        if (sundryTypes.contains(SundryType.BASIC_RETAINING_SOIL)) {
            return rand % 4 == 3;
        }
        return false;
    }

    private void giveRewardToLevelThreeFriends() {
        for (Friendship friendship : this.getFriendships()) {
            if ((friendship.getFirstPlayer() instanceof NPC && friendship.getSecondPlayer() instanceof Player)) {
                if (friendship.getFriendShipLevel() == 3) {
                    NPC npc = (NPC) friendship.getFirstPlayer();
                    Mission mission = npc.getType().getMissions().get(npc.getType().getMissions().size() % 3);
                    int size = mission.getReward().entrySet().size();
                    Salable[] array = (Salable[]) mission.getReward().keySet().toArray();
                    Salable gift = array[size % 4];
                    ((Player) friendship.getSecondPlayer()).getInventory().addProductToBackPack(gift, 1);
                }
            }
            if ((friendship.getSecondPlayer() instanceof NPC && friendship.getFirstPlayer() instanceof Player)) {
                if (friendship.getFriendShipLevel() == 3) {
                    NPC npc = (NPC) friendship.getSecondPlayer();
                    Mission mission = npc.getType().getMissions().get(npc.getType().getMissions().size() % 3);
                    int size = mission.getReward().entrySet().size();
                    Salable[] array = (Salable[]) mission.getReward().keySet().toArray();
                    Salable gift = array[size % 4];
                    ((Player) friendship.getFirstPlayer()).getInventory().addProductToBackPack(gift, 1);
                }
            }
        }
    }

}
