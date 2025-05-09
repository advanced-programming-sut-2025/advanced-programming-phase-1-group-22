package model;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import model.source.Crop;
import model.source.Mineral;
import model.source.Seed;
import model.tools.Axe;
import model.tools.Tool;

import java.io.Serializable;
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "@class")
@JsonSubTypes({
		@JsonSubTypes.Type(value = Seed.class, name = "seed"),
		@JsonSubTypes.Type(value = Crop.class, name = "crop"),
		@JsonSubTypes.Type(value = Mineral.class, name = "mineral"),
		@JsonSubTypes.Type(value = Tool.class, name = "tool"),
		@JsonSubTypes.Type(value = Axe.class, name = "axe"),
		@JsonSubTypes.Type(value = Flower.class, name = "flower"),
		// ... تمام کلاس‌هایی که Salable رو پیاده‌سازی می‌کنن
})
public interface Salable extends Serializable {

 	String getName();
	int getSellPrice();

    default Integer getContainingEnergy() {
		return 0;
	}
}
