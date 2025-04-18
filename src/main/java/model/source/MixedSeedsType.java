package model.source;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
@Getter
public enum MixedSeedsType implements Source{
    SPRING {
        @Override
        protected void initialize() {
            seedTypeList.add(SeedType.CAULIFLOWER_SEEDS);
            seedTypeList.add(SeedType.PARSNIP_SEEDS);
            seedTypeList.add(SeedType.POTATO_SEEDS);
            seedTypeList.add(SeedType.JAZZ_SEEDS);
            seedTypeList.add(SeedType.TULIP_BULB);
            seedTypeList.add(SeedType.GARLIC_SEEDS);
            seedTypeList.add(SeedType.KALE_SEEDS);
            seedTypeList.add(SeedType.RHUBARB_SEEDS);
            seedTypeList.add(SeedType.STRAWBERRY_SEEDS);
        }
    },
    SUMMER {
        @Override
        protected void initialize() {
            seedTypeList.add(SeedType.CORN_SEEDS);
            seedTypeList.add(SeedType.PEPPER_SEEDS);
            seedTypeList.add(SeedType.RADISH_SEEDS);
            seedTypeList.add(SeedType.WHEAT_SEEDS);
            seedTypeList.add(SeedType.POPPY_SEEDS);
            seedTypeList.add(SeedType.SUNFLOWER_SEEDS);
            seedTypeList.add(SeedType.SPANGLE_SEEDS);
            seedTypeList.add(SeedType.MELON_SEEDS);
            seedTypeList.add(SeedType.TOMATO_SEEDS);
        }
    },
    FALL {
        @Override
        protected void initialize() {
            seedTypeList.add(SeedType.ARTICHOKE_SEEDS);
            seedTypeList.add(SeedType.CORN_SEEDS);
            seedTypeList.add(SeedType.EGGPLANT_SEEDS);
            seedTypeList.add(SeedType.PUMPKIN_SEEDS);
            seedTypeList.add(SeedType.SUNFLOWER_SEEDS);
            seedTypeList.add(SeedType.FAIRY_SEEDS);
            seedTypeList.add(SeedType.AMARANTH_SEEDS);
            seedTypeList.add(SeedType.YAM_SEEDS);
            seedTypeList.add(SeedType.CRANBERRY_SEEDS);
        }
    },
    WINTER {
        @Override
        protected void initialize() {
            seedTypeList.add(SeedType.POWDERMELON_SEEDS);
        }
    };

    protected final List<SeedType> seedTypeList;

    MixedSeedsType() {
        this.seedTypeList = new ArrayList<>();
        initialize();
    }

    protected abstract void initialize();
}