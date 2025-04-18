package model.products;

import lombok.Getter;
import lombok.Setter;
import model.Salable;
import model.enums.Season;
import model.source.Seed;
import model.source.Source;

import java.util.List;

@Getter
@Setter
public abstract class HarvestAbleProduct implements Salable {
    private Integer id;
    private String name;
    private Source source;
    private Boolean isFarming;
    private List<Integer> harvests;
    private Boolean isMultiHarvestable;
    private Integer timeDiffBetweenHarvests;
    private Integer basePrice;
    private Boolean isEdible;
    private Integer containingEnergy;
    private List<Season> seasonList;
    private Boolean isEnormosable;

    public HarvestAbleProduct(String name, Source source, Boolean isFarming, List<Integer> harvests,
                              Boolean isMultiHarvestable, Integer timeDiffBetweenHarvests, Integer basePrice,
                              Boolean isEdible, Integer containingEnergy, List<Season> seasonList, Boolean isEnormosable) {
        this.name = name;
        this.source = source;
        this.isFarming = isFarming;
        this.harvests = harvests;
        this.isMultiHarvestable = isMultiHarvestable;
        this.timeDiffBetweenHarvests = timeDiffBetweenHarvests;
        this.basePrice = basePrice;
        this.isEdible = isEdible;
        this.containingEnergy = containingEnergy;
        this.seasonList = seasonList;
        this.isEnormosable = isEnormosable;
    }

    @Override
    public String toString() {
        return "HarvestAbleProduct{" +
                ", name='" + name + '\'' +
                ", source=" + source +
                ", isFarming=" + isFarming +
                ", harvests=" + harvests +
                ", isMultiHarvestable=" + isMultiHarvestable +
                ", timeDiffBetweenHarvests=" + timeDiffBetweenHarvests +
                ", basePrice=" + basePrice +
                ", isEdible=" + isEdible +
                ", ContainingEnergy=" + containingEnergy +
                ", seasonList=" + seasonList +
                ", isEnormosable=" + isEnormosable +
                '}';
    }
}
