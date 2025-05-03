package model.relations;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import model.Actor;
import model.Salable;
import model.enums.Season;
import model.products.Product;

import java.util.Collections;
import java.util.Map;

@Getter
@Setter
@ToString
public class Mission {
    private Integer id;
    private Actor doer;
    private NPCType requester;
    private Integer requiredLevel;
    private Season requiredSeason;
    private final SalableMapSupplier requestSupplier;
    private final SalableMapSupplier rewardSupplier;
    private transient volatile Map<Salable, Integer> resolvedRequest;
    private transient volatile Map<Salable, Integer> resolvedReward;

    @FunctionalInterface
    public interface SalableMapSupplier {
        Map<Salable, Integer> get();
    }

    public Mission(NPCType requester, SalableMapSupplier requestSupplier,
                   SalableMapSupplier rewardSupplier, Integer requiredLevel) {
        this.requester = requester;
        this.requestSupplier = requestSupplier;
        this.rewardSupplier = rewardSupplier;
        this.requiredLevel = requiredLevel;
    }

    public Mission(NPCType requester, SalableMapSupplier requestSupplier,
                   SalableMapSupplier rewardSupplier, Season requiredSeason) {
        this.requester = requester;
        this.requestSupplier = requestSupplier;
        this.rewardSupplier = rewardSupplier;
        this.requiredSeason = requiredSeason;
    }


    /**
     * Thread-safe lazy initialization getter that maintains backward compatibility
     * while preventing circular dependency issues during initialization.
     */
    public Map<Salable, Integer> getRequest() {
        // First check (no synchronization)
        Map<Salable, Integer> result = this.resolvedRequest;
        if (result == null) {
            synchronized (this) {
                // Second check (with synchronization)
                result = this.resolvedRequest;
                if (result == null) {
                    // Initialize with empty map if null to maintain backward compatibility
                    this.resolvedRequest = Collections.emptyMap();
                    result = this.resolvedRequest;
                }
            }
        }
        return result;
    }

    /**
     * Alternative version that works with the supplier pattern
     * while being backward compatible
     */
    public Map<Salable, Integer> getReward() {
        // Return directly if already resolved
        if (this.resolvedReward != null) {
            return this.resolvedReward;
        }

        // Handle lazy initialization
        synchronized (this) {
            if (this.resolvedReward == null) {
                if (this.rewardSupplier != null) {
                    this.resolvedReward = Collections.unmodifiableMap(rewardSupplier.get());
                } else {
                    this.resolvedReward = Collections.emptyMap();
                }
            }
        }
        return this.resolvedReward;
    }

    public void giveMission() {

    }

    public void getMission() {

    }
}
