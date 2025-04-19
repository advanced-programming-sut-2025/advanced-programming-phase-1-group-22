package model.tools;

import lombok.Getter;
import model.products.Product;

@Getter
public enum TrashCan implements Tool {
    NORMAL("normal trashcan",0),
    CUPPER("cupper trashcan",15),
    IRON("iron trashcan",30),
    GOLD("gold trashcan",45),
    IRIDIUM("iridium trashcan",65);

    private final String name;
    private final Integer prunedValue;

    TrashCan(String name,int prunedValue) {
        this.name = name;
        this.prunedValue = prunedValue;
    }

    public void addSalableToTrashCan(Product product){

    }

    @Override
    public void addToolEfficiency(double efficiency) {

    }

    @Override
    public String getName() {
        return this.name.toLowerCase();
    }
}
