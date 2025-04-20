package model.structure.farmInitialElements;

import lombok.Getter;
import model.Pair;
import model.structure.Structure;

import java.util.ArrayList;
import java.util.List;

@Getter
public abstract class HardCodeFarmElements extends Structure {
	List<Pair> tilePairList = new ArrayList<>();
	private Integer length;
	private Integer width;

	public HardCodeFarmElements() {

	}

	public HardCodeFarmElements(HardCodeFarmElements hardCodeFarmElements) {
		this.tilePairList = hardCodeFarmElements.tilePairList;
		this.length = hardCodeFarmElements.length;
		this.width = hardCodeFarmElements.width;
	}

	public abstract HardCodeFarmElements cloneEl();

	public void setTilePairList(List<Pair> tilePairList) {
		this.tilePairList = tilePairList;
	}

	public void setLength(Integer length) {
		this.length = length;
	}

	public void setWidth(Integer width) {
		this.width = width;
	}
}
