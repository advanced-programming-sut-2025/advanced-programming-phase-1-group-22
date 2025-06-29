package io.github.some_example_name.model;

public interface Salable {
 	String getName();
	int getSellPrice();

    default Integer getContainingEnergy() {
		return 0;
	}
}
