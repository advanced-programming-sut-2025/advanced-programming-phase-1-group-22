package model.tools;

import lombok.Getter;
import lombok.Setter;
import model.products.HarvestAbleProduct;
import model.relations.Player;
import model.Tile;
import model.structure.Structure;
import model.structure.farmInitialElements.GreenHouse;
import model.structure.farmInitialElements.Lake;
import utils.App;

import java.util.List;

@Getter
@Setter
public class WateringCan implements Tool{
	private WateringCanType wateringCanType;
	private Integer remain;

	public WateringCan(WateringCanType wateringCanType) {
		this.wateringCanType = wateringCanType;
		this.remain = wateringCanType.getCapacity();
	}

	@Override
	public void addToolEfficiency(double efficiency) {

	}

	@Override
	public Tool getToolByLevel(int level) {
		return wateringCanType.getToolByLevel(level);
	}

	@Override
	public int getLevel() {
		return wateringCanType.getLevel();
	}

	@Override
	public int getEnergy(Player player) {
		return wateringCanType.getEnergy(player);
	}

	@Override
	public String useTool(Player player, Tile tile) {
		player.changeEnergy(-this.getEnergy(player));
		List<Structure> structures = App.getInstance().getCurrentGame().getVillage().findStructuresByTile(tile);
		for (Structure structure : structures) {
			if (structure != null){
				if (structure instanceof Lake){ // or GreenHouse
					this.remain = this.wateringCanType.getCapacity();
					return "the watering can completely filled";
				}
				if (structure instanceof GreenHouse){
					if (((GreenHouse)structure).getPool().getTiles().contains(tile)){
						this.remain = this.wateringCanType.getCapacity();
						return "the watering can completely filled";
					}
				}
				if (structure instanceof HarvestAbleProduct){
					if (this.remain <= 0){
						return "your watering can is empty";
					}
					((HarvestAbleProduct)structure).setWaterToday(true);
					this.remain -= 1;
					return "you water the harvest";
				}
			}
		}

		return "you use this tool in a wrong way";
	}

	@Override
	public String getName() {
		return wateringCanType.getName();
	}

	@Override
	public int getSellPrice() {
		return wateringCanType.getSellPrice();
	}


}
