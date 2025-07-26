package io.github.some_example_name.common.model.relations;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.some_example_name.common.model.Entry;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import io.github.some_example_name.common.model.Actor;
import io.github.some_example_name.common.model.TimeAndDate;
import io.github.some_example_name.common.model.enums.Season;
import io.github.some_example_name.server.saveGame.JsonPreparable;
import io.github.some_example_name.server.saveGame.ObjectMapWrapper;
import io.github.some_example_name.server.saveGame.ObjectWrapper;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class, property = "@id")
public class Friendship implements JsonPreparable {
    private Integer id;
    private Actor firstPlayer;
    private Actor secondPlayer;
    private List<Gift> gifts;
    private Integer friendShipLevel;
    private Integer xp;
    private ArrayList<Entry<String, Actor>> dialogs;
//    @JsonBackReference
    private TimeAndDate lastSeen = new TimeAndDate(1, 9, Season.SPRING, 0);
//    @JsonBackReference
    private TimeAndDate timeFromGettingFirstLevel = new TimeAndDate(1, 9, Season.SPRING, 0);

    public Friendship(Integer id, Actor firstPlayer, Actor secondPlayer) {
        this.id = id;
        this.firstPlayer = firstPlayer;
        this.secondPlayer = secondPlayer;
        this.gifts = new ArrayList<>();
        this.friendShipLevel = 0;
        this.xp = 0;
        this.dialogs = new ArrayList<>();
    }

    public void talkToNPC() {

    }

    public void talkToPlayer() {

    }

    public Integer getFriendShipLevel() {
        if (secondPlayer instanceof NPC || firstPlayer instanceof NPC) {
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

    private ObjectWrapper firstPlayerWrapper;
    private ObjectWrapper secondPlayerWrapper;
    private List<ObjectWrapper> giftWrappers;
    private List<ObjectMapWrapper.Entry> dialogEntries;

    @Override
    public void prepareForSave(ObjectMapper mapper) {
        this.firstPlayerWrapper = new ObjectWrapper(firstPlayer, mapper);
        this.secondPlayerWrapper = new ObjectWrapper(secondPlayer, mapper);

        this.giftWrappers = new ArrayList<>();
        if (gifts != null) {
            for (Gift gift : gifts) {
                giftWrappers.add(new ObjectWrapper(gift, mapper));
            }
        }

        this.dialogEntries = new ArrayList<>();
        if (dialogs != null) {
            for (Entry<String, Actor> entry : dialogs) {
                ObjectWrapper actorWrapper = new ObjectWrapper(entry.getValue(), mapper);
                dialogEntries.add(new ObjectMapWrapper.Entry(actorWrapper, entry.getKey()));
            }
        }
    }

    @Override
    public void unpackAfterLoad(ObjectMapper mapper) {
        this.firstPlayer = (Actor) firstPlayerWrapper.toObject(mapper);
        this.secondPlayer = (Actor) secondPlayerWrapper.toObject(mapper);

        this.gifts = new ArrayList<>();
        if (giftWrappers != null) {
            for (ObjectWrapper wrapper : giftWrappers) {
                gifts.add((Gift) wrapper.toObject(mapper));
            }
        }

        this.dialogs = new ArrayList<>();
        if (dialogEntries != null) {
            for (ObjectMapWrapper.Entry entry : dialogEntries) {
                Actor actor = (Actor) entry.objectWrapper.toObject(mapper);
                dialogs.add(new Entry<>(String.valueOf(entry.amount), actor));
            }
        }
    }
}
