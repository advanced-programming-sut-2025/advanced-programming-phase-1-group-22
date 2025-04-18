package model.tools;

import lombok.Getter;
import model.products.Product;

@Getter
public enum TrashCan implements Tool {
    NORMAL(0), CUPPER(15), IRON(30),
    GOLD(45), IRIDIUM(65);
    private final Integer prunedValue;

    TrashCan(int prunedValue) {
        this.prunedValue = prunedValue;
    }

    public void addSalableToTrashCan(Product product){

    }

    @Override
    public void addToolEfficiency(double efficiency) {

    }
}
