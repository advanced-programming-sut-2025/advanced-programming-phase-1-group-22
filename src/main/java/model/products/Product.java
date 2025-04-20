package model.products;

import model.Salable;

public interface Product extends Salable {
    int getSellPrice();
}