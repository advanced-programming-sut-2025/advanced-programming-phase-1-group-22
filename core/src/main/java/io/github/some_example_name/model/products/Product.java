package io.github.some_example_name.model.products;

import io.github.some_example_name.model.Salable;

public interface Product extends Salable {
    int getSellPrice();
    int getEnergy();
}
