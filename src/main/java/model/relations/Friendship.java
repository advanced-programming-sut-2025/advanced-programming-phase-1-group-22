package model.relations;

import lombok.Getter;
import lombok.Setter;
import model.Actor;

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
