package model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import model.enums.Weather;
import model.relations.Friendship;
import model.relations.NPC;
import model.relations.Player;
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
        TimeAndDate timeAndDate = this.getTimeAndDate();
        Weather tomorrowWeather = this.getVillage().getTomorrowWeather();
        Weather weather = this.getVillage().getWeather();
        this.getVillage().setWeather(tomorrowWeather);
        Random random = new Random();
        do {
            tomorrowWeather = Weather.values()[random.nextInt(0, 3)];
        } while (tomorrowWeather.getSeasons().contains(timeAndDate.getSeason()));
        if (weather == Weather.STORMY) {
            for (Farm farm : this.village.getFarms()) {
                for (int i = 0; i < 3; i++) {
                    int randX = random.nextInt(farm.getFarmXStart(), farm.getFarmXEnd());
                    int randY = random.nextInt(farm.getFarmYStart(), farm.getFarmYEnd());
                    weather.thunderBolt(randX, randY);
                }
            }
        }
        for (Player player : players) {
            player.resetEnergy();
            addGoldToPlayerForShippingBin(player.getShippingBin().CalculatePriceOfShippingBinProducts(), player);
        }
        for (Farm farm : App.getInstance().getCurrentGame().getVillage().getFarms()) {
            farm.generateRandomForaging();
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
            if (currentPlayer == players.get(i)) break;
        }
        i = (i == players.size() - 1) ? 0 : i + 1;
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
}
