package io.github.some_example_name.common.model.products;

import io.github.some_example_name.common.model.Salable;

public interface Product extends Salable {
    int getSellPrice();
    int getEnergy();
}
