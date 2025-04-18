package model.tools;

import model.Salable;
import model.products.Product;

public interface Tool extends Salable {
    void addToolEfficiency(double efficiency);

}
