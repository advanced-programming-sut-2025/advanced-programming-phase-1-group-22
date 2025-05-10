package model.relations;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.ToString;
import model.Actor;
import model.Salable;
import model.enums.Season;
import saveGame.ObjectMapWrapper;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Supplier;

@Getter
@ToString
public class Mission {
    private Integer id;
    private Actor doer;
    private final NPCType requester;
    private Integer requiredLevel;
    private Season requiredSeason;
    private final Supplier<Map<Salable, Integer>> requestSupplier;
    private final Supplier<Map<Salable, Integer>> rewardSupplier;
    private transient volatile Map<Salable, Integer> resolvedRequest;
    private transient volatile Map<Salable, Integer> resolvedReward;

    public Mission(Supplier<Map<Salable, Integer>> requestSupplier,
                   Supplier<Map<Salable, Integer>> rewardSupplier,
                   Integer requiredLevel) {
        this(null, requestSupplier, rewardSupplier, requiredLevel, null);
    }

    public Mission(Supplier<Map<Salable, Integer>> requestSupplier,
                   Supplier<Map<Salable, Integer>> rewardSupplier,
                   Season requiredSeason) {
        this(null, requestSupplier, rewardSupplier, null, requiredSeason);
    }

    public Mission(NPCType requester,
                   Supplier<Map<Salable, Integer>> requestSupplier,
                   Supplier<Map<Salable, Integer>> rewardSupplier,
                   Integer requiredLevel) {
        this(requester, requestSupplier, rewardSupplier, requiredLevel, null);
    }

    public Mission(NPCType requester,
                   Supplier<Map<Salable, Integer>> requestSupplier,
                   Supplier<Map<Salable, Integer>> rewardSupplier,
                   Season requiredSeason) {
        this(requester, requestSupplier, rewardSupplier, null, requiredSeason);
    }

    private Mission(NPCType requester,
                    Supplier<Map<Salable, Integer>> requestSupplier,
                    Supplier<Map<Salable, Integer>> rewardSupplier,
                    Integer requiredLevel,
                    Season requiredSeason) {
        this.requester = requester;
        this.requestSupplier = Objects.requireNonNull(requestSupplier);
        this.rewardSupplier = Objects.requireNonNull(rewardSupplier);
        this.requiredLevel = requiredLevel;
        this.requiredSeason = requiredSeason;
    }

    public Map<Salable, Integer> getRequest() {
        Map<Salable, Integer> result = resolvedRequest;
        if (result == null) {
            synchronized (this) {
                result = resolvedRequest;
                if (result == null) {
                    resolvedRequest = Collections.unmodifiableMap(requestSupplier.get());
                    result = resolvedRequest;
                }
            }
        }
        return result;
    }

    public Map<Salable, Integer> getReward() {
        Map<Salable, Integer> result = resolvedReward;
        if (result == null) {
            synchronized (this) {
                result = resolvedReward;
                if (result == null) {
                    resolvedReward = Collections.unmodifiableMap(rewardSupplier.get());
                    result = resolvedReward;
                }
            }
        }
        return result;
    }

    public void setDoer(Actor doer) {
        this.doer = doer;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public boolean isAvailable(int currentLevel, Season currentSeason) {
        if (requiredLevel != null && currentLevel < requiredLevel) {
            return false;
        }
        if (requiredSeason != null && requiredSeason != currentSeason) {
            return false;
        }
        return true;
    }

    public void complete() {
        // Implementation for mission completion
        if (doer != null) {
            // Transfer items from doer to requester (if needed)
            // Give rewards to doer
        }
    }
    @JsonIgnore
    private Map<Salable, Integer> reward = new HashMap<>();

    @JsonProperty("reward")
    private ObjectMapWrapper rewardWrapper;

    public void prepareForSave(ObjectMapper mapper) {
        this.rewardWrapper = new ObjectMapWrapper((Map<Object, Integer>)(Map<?, ?>) reward, mapper);
    }

    public void unpackAfterLoad(ObjectMapper mapper) {
        this.reward = (Map<Salable, Integer>)(Map<?, ?>) rewardWrapper.toMap(mapper);
    }
}