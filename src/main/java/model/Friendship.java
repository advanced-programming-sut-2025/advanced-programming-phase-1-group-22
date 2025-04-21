package model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@ToString
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
}
