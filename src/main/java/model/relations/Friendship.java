package model.relations;

import lombok.Getter;
import lombok.Setter;
import model.Actor;
import model.TimeAndDate;
import model.enums.Season;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter
public class Friendship {
    private Integer id;
    private Actor firstPlayer;
    private Actor secondPlayer;
    private List<Gift> gifts;
    private Integer friendShipLevel;
    private Integer xp;
    private Map<String, Actor> dialogs;
    private TimeAndDate lastSeen = new TimeAndDate(1, 9, Season.SPRING, 0);
    private TimeAndDate timeFromGettingFirstLevel = new TimeAndDate(1, 9, Season.SPRING, 0);

    public Friendship(Integer id, Actor firstPlayer, Actor secondPlayer) {
        this.id = id;
        this.firstPlayer = firstPlayer;
        this.secondPlayer = secondPlayer;
        this.gifts = new ArrayList<>();
        this.friendShipLevel = 0;
        this.xp = 0;
        this.dialogs = new HashMap<>();
    }

    public void talkToNPC() {

    }

    public void talkToPlayer() {

    }

    public Integer getFriendShipLevel() {
        if (secondPlayer instanceof NPC || firstPlayer instanceof Player) {
            return friendShipLevel + xp / 200;
        }
        return friendShipLevel + xp / 100;
    }

    @Override
    public String toString() {
        return "Friendship{" +
                ", friend=" + secondPlayer +
                ", friendShipLevel=" + friendShipLevel +
                ", xp=" + xp +
                '}';
    }
}
