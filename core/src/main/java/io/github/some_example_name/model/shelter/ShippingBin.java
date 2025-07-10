package io.github.some_example_name.model.shelter;

import com.badlogic.gdx.graphics.g2d.Sprite;
import io.github.some_example_name.utils.App;
import io.github.some_example_name.utils.GameAsset;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import io.github.some_example_name.model.Salable;
import io.github.some_example_name.model.structure.farmInitialElements.HardCodeFarmElements;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@ToString
public class ShippingBin extends HardCodeFarmElements {
    private final FarmBuildingType farmBuildingType = FarmBuildingType.SHIPPING_BIN;
    private Map<Salable, Integer> salable = new HashMap<>();
    private Sprite sprite;

    public ShippingBin(ShippingBin shippingBin) {
        super(shippingBin);
        this.sprite = new Sprite(GameAsset.SHIPPINGBIN);
        this.sprite.setSize(App.tileWidth * 2, App.tileHeight * 2);
    }

    public ShippingBin() {
        super.setHeight(1);
        super.setWidth(1);
        this.sprite = new Sprite(GameAsset.SHIPPINGBIN);
        this.sprite.setSize(App.tileWidth * 2, App.tileHeight * 2);
    }

    @Override
    public HardCodeFarmElements copyEl() {
        return new ShippingBin(this);
    }

    public Integer CalculatePriceOfShippingBinProducts() {
        int price = 0;

        for (Map.Entry<Salable, Integer> salableIntegerEntry : salable.entrySet()) {
            price += salableIntegerEntry.getKey().getSellPrice() * salableIntegerEntry.getValue();
        }

        salable.clear();
        return price;
    }

    public void add(Salable salable, int count) {
        if (this.salable.containsKey(salable)) {
            this.salable.put(salable, this.salable.get(salable) + count);
        } else {
            this.salable.put(salable, count);
        }
    }
}
