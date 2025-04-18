package model.shelter;

import lombok.Getter;
import lombok.Setter;
import model.structure.Structure;
@Getter
@Setter
public class FarmBuilding extends Structure {
	private FarmBuildingType farmBuildingType;
}
