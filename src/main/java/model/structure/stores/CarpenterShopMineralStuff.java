package model.structure.stores;

import lombok.Getter;
import model.source.MineralType;
@Getter
public enum CarpenterShopMineralStuff {
	WOOD(MineralType.WOOD,10,-1),
	STONE(MineralType.STONE,20,-1);

	private final MineralType mineralType;
	private final Integer price;
	private final Integer dailyLimit;

	CarpenterShopMineralStuff(MineralType mineralType, Integer price, Integer dailyLimit) {
		this.mineralType = mineralType;
		this.price = price;
		this.dailyLimit = dailyLimit;
	}
}
