package model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class Game {
    private Village village;
    private List<Player> players;
    private Player currentPlayer;
    private List<NPCType> npcs;
    private List<Friendship> friendships;
    private TimeAndDate timeAndDate;
    private final Integer length = 160;
    private final Integer width = 120;
    public Tile[][] tiles = new Tile[length][width];

    public void start() {
        for (int i = 0; i < 160; i++) {
            for (int i1 = 0; i1 < 120; i1++) {
                tiles[i][i1] = new Tile(i, i1);
            }
        }
    }
}
