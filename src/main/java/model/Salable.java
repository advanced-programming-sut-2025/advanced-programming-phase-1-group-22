package model;

public interface Salable {
 	String getName();
	int getSellPrice();

    default Integer getContainingEnergy() {
		return 0;
	}
}
