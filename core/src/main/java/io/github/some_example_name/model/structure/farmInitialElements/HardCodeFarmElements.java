package io.github.some_example_name.model.structure.farmInitialElements;

import lombok.Getter;
import io.github.some_example_name.model.Pair;
import io.github.some_example_name.model.structure.Structure;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
public abstract class HardCodeFarmElements extends Structure {
	List<Pair> tilePairList = new ArrayList<>();
	private Integer width;
	private Integer height;

	public HardCodeFarmElements() {

	}

	public HardCodeFarmElements(HardCodeFarmElements hardCodeFarmElements) {
		this.tilePairList = hardCodeFarmElements.tilePairList;
		this.width = hardCodeFarmElements.width;
		this.height = hardCodeFarmElements.height;
	}

	public abstract HardCodeFarmElements copyEl();

}
