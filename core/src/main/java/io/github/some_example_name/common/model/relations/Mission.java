package io.github.some_example_name.common.model.relations;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import io.github.some_example_name.common.model.Actor;
import io.github.some_example_name.common.model.Salable;
import io.github.some_example_name.common.model.enums.Season;
import io.github.some_example_name.server.saveGame.ObjectMapWrapper;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Supplier;

@Getter
@Setter
@ToString
@NoArgsConstructor(force = true)
public class Mission {
    private int id;
    private final AtomicReference<Actor> doer = new AtomicReference<>();
    private NPCType requester;
    private Integer requiredLevel;
    private Season requiredSeason;
    private final Supplier<Map<Salable, Integer>> requestSupplier;
    private final Supplier<Map<Salable, Integer>> rewardSupplier;
    private transient volatile Map<Salable, Integer> resolvedRequest;
    private transient volatile Map<Salable, Integer> resolvedReward;

    public Mission(Integer id, Supplier<Map<Salable, Integer>> requestSupplier,
                   Supplier<Map<Salable, Integer>> rewardSupplier,
                   Integer requiredLevel) {
        this(null, id, requestSupplier, rewardSupplier, requiredLevel, null);
    }

    public Mission(Integer id, Supplier<Map<Salable, Integer>> requestSupplier,
                   Supplier<Map<Salable, Integer>> rewardSupplier,
                   Season requiredSeason) {
        this(null, id, requestSupplier, rewardSupplier, null, requiredSeason);
    }

    private Mission(NPCType requester,
                    Integer id,
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

    public boolean isAvailable(int currentLevel, Season currentSeason) {
        if (requiredLevel != null && currentLevel < requiredLevel) {
            return false;
        }
        if (requiredSeason != null && requiredSeason != currentSeason) {
            return false;
        }
        return true;
    }

    @JsonIgnore
    private Map<Salable, Integer> reward = new HashMap<>();

    @JsonProperty("reward")
    private ObjectMapWrapper rewardWrapper;

    public void prepareForSave(ObjectMapper mapper) {
        this.rewardWrapper = new ObjectMapWrapper((Map<Object, Integer>) (Map<?, ?>) reward, mapper);
    }

    public void unpackAfterLoad(ObjectMapper mapper) {
        this.reward = (Map<Salable, Integer>) (Map<?, ?>) rewardWrapper.toMap(mapper);
    }

    public Actor getDoer() {
        return doer.get();
    }

    public void setDoer(Actor actor) {
        doer.compareAndSet(null, actor);
    }
}
