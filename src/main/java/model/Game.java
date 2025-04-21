package model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import utils.App;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
public class Game {
    private Village village;
    private final List<Player> players = new ArrayList<>();
    private Player currentPlayer;
    private final List<NPCType> npcs = new ArrayList<>();
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

    public void startNewDay(){
        for (Player player : players) {
            player.resetEnergy();
            addGoldToPlayerForShippingBin(player.getShippingBin().CalculatePriceOfShippingBinProducts(),player);
        }
        for (Farm farm : App.getInstance().getCurrentGame().getVillage().getFarms()) {
            farm.generateRandomForaging();
        }
    }

    public void addGoldToPlayerForShippingBin(int price, Player player){
       int oldGold = player.getAccount().getGolds();
       player.getAccount().setGolds(oldGold + price);
    }

    public void addPlayer(Player player) {
        players.add(player);
    }

    public void addTermination() {
        playersInFavorTermination++;
    }

    public void nextPlayer() {
        int i;
        for (i = 0; i < players.size(); i++) {
            if (currentPlayer == players.get(i)) break;
        }
        i = (i == players.size() - 1) ? 0 : i+1;
//        if (players.get(i).getIsFainted()) nextPlayer();
        currentPlayer = players.get(i);
        timeAndDate.moveTimeForward();
    }
}
