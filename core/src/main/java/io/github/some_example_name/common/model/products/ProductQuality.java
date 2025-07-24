package io.github.some_example_name.common.model.products;

import lombok.Getter;
import io.github.some_example_name.common.model.exception.InvalidInputException;

@Getter
public enum ProductQuality {
    NORMAL(1.0, 0.0, 0.5, 1),
    SILVER(1.25, 0.5, 0.7, 2),
    GOLD(1.5, 0.7, 0.9, 3),
    IRIDIUM(2.0, 0.9, 100.0, 4);

    private final Double priceCoefficient;
    private final Double startOfTheRange;
    private final Double endOfTheRange;
    private final Integer level;

    ProductQuality(Double priceCoefficient, Double startOfTheRange, Double endOfTheRange, Integer level) {
        this.priceCoefficient = priceCoefficient;
        this.startOfTheRange = startOfTheRange;
        this.endOfTheRange = endOfTheRange;
        this.level = level;
    }

    public static ProductQuality getQualityByDouble(Double quality) {
        for (ProductQuality value : ProductQuality.values()) {
            if (value.startOfTheRange <= quality &&
                value.endOfTheRange >= quality) {
                return value;
            }
        }
        throw new InvalidInputException("this quality is wrong");
    }

    public static ProductQuality getByLevel(int level) {
        for (ProductQuality value : ProductQuality.values()) {
            if (value.level == level) return value;
        }
        return null;
    }
}
