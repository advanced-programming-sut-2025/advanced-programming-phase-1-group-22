package model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;
@Getter
@Setter
@ToString
public class Game  {
    private Village village;
    private List<Player> players;
    private List<NPCType> npcs;
    private List<Friendship> friendships;
    private TimeAndDate timeAndDate;
}
