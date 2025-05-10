package model.relations;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import model.Actor;
import model.TimeAndDate;
import model.enums.Season;
import save3.JsonPreparable;
import save3.ObjectMapWrapper;
import save3.ObjectWrapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
public class Friendship implements JsonPreparable {
    private Integer id;
    private Actor firstPlayer;
    private Actor secondPlayer;
    private List<Gift> gifts;
    private Integer friendShipLevel;
    private Integer xp;
    private Map<String, Actor> dialogs;
    @JsonBackReference
    private TimeAndDate lastSeen = new TimeAndDate(1, 9, Season.SPRING, 0);
    @JsonBackReference
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
    // اینها برای ذخیره‌سازی
    private ObjectWrapper firstPlayerWrapper;
    private ObjectWrapper secondPlayerWrapper;
    private List<ObjectWrapper> giftWrappers;
    private List<ObjectMapWrapper.Entry> dialogEntries;

    @Override
    public void prepareForSave(ObjectMapper mapper) {
        this.firstPlayerWrapper = new ObjectWrapper(firstPlayer, mapper);
        this.secondPlayerWrapper = new ObjectWrapper(secondPlayer, mapper);

        // هدایا
        this.giftWrappers = new ArrayList<>();
        if (gifts != null) {
            for (Gift gift : gifts) {
                giftWrappers.add(new ObjectWrapper(gift, mapper));
            }
        }

        // دیالوگ‌ها
        this.dialogEntries = new ArrayList<>();
        if (dialogs != null) {
            for (Map.Entry<String, Actor> entry : dialogs.entrySet()) {
                ObjectWrapper actorWrapper = new ObjectWrapper(entry.getValue(), mapper);
                dialogEntries.add(new ObjectMapWrapper.Entry(actorWrapper, entry.getKey())); // مقدار به عنوان string
            }
        }
    }

    @Override
    public void unpackAfterLoad(ObjectMapper mapper) {
        this.firstPlayer = (Actor) firstPlayerWrapper.toObject(mapper);
        this.secondPlayer = (Actor) secondPlayerWrapper.toObject(mapper);

        // هدایا
        this.gifts = new ArrayList<>();
        if (giftWrappers != null) {
            for (ObjectWrapper wrapper : giftWrappers) {
                gifts.add((Gift) wrapper.toObject(mapper));
            }
        }

        // دیالوگ‌ها
        this.dialogs = new HashMap<>();
        if (dialogEntries != null) {
            for (ObjectMapWrapper.Entry entry : dialogEntries) {
                Actor actor = (Actor) entry.objectWrapper.toObject(mapper);
                dialogs.put(String.valueOf(entry.amount), actor); // `amount` اینجا کلید هست نه مقدار
            }
        }
    }
}
