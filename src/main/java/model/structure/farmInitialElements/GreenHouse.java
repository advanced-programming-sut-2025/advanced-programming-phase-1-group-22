package model.structure.farmInitialElements;

import lombok.Getter;
import lombok.Setter;
import model.Salable;
import model.relations.Player;
import model.shelter.FarmBuildingType;
import model.source.MineralType;

@Getter
@Setter
public class GreenHouse extends HardCodeFarmElements {
    private final FarmBuildingType farmBuildingType = FarmBuildingType.GreenHouse;
    private Lake pool;

    private boolean isBuilt = false;
    public GreenHouse(GreenHouse greenHouse) {
        super(greenHouse);
    }

    public GreenHouse() {
        super.setLength(8);
        super.setWidth(7);
    }

    public int areIngredientsAvailable(Player player) {
        if (player.getAccount().getGolds() < 1000) return 1;
        if (!player.getInventory().checkProductAvailabilityInBackPack(MineralType.WOOD.getName(), 500)) return 2;
        player.getAccount().removeGolds(1000);
        Salable salable = player.getInventory().findProductInBackPackByNAme(MineralType.WOOD.getName());
        player.getInventory().deleteProductFromBackPack(salable, player,500);
        isBuilt = true;
        return 0;
    }


    @Override
    public HardCodeFarmElements cloneEl() {
        return new GreenHouse(this);
    }

    public void build() {
        isBuilt = true;
        pool = new Lake();
        for (int i = 0; i <8; i++) {
            pool.getTiles().add(this.getTiles().get(i));
        }
    }
}
