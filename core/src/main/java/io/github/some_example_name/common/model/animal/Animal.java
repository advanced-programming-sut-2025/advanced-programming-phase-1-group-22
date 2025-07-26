package io.github.some_example_name.common.model.animal;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import io.github.some_example_name.common.utils.App;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import io.github.some_example_name.common.model.products.AnimalProductType;
import io.github.some_example_name.common.model.products.ProductQuality;
import io.github.some_example_name.common.model.relations.Player;
import io.github.some_example_name.common.model.Salable;
import io.github.some_example_name.common.model.products.AnimalProduct;
import io.github.some_example_name.common.model.structure.Structure;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

@Getter
@Setter
@NoArgsConstructor
public class Animal extends Structure implements Salable {
    private AnimalType animalType;
    private AnimalProduct todayProduct;
    private Integer relationShipQuality = 0;
    private Boolean isFeed = false;
    private Boolean pet = false;
    private String name;
    private Boolean isAnimalStayOutAllNight = false;
    private Player owner;
    private transient Texture texture;
    private transient Sprite sprite;
    private Queue<Vector2> movementPath = new LinkedList<>();
    private float timeSinceLastMove = 0f;

    public Animal(AnimalType animalType, String name) {
        this.animalType = animalType;
        this.name = name;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public Integer getContainingEnergy() {
        return 0;
    }

    @Override
    public int getSellPrice() {
        return this.animalType.getSellPrice();
    }

    public void produceAnimalProduct() {
        if (!this.isFeed) {
            this.todayProduct = null;
            return;
        }
        double quality = generateQuality();
        AnimalProductType animalProductType = this.animalType.getProductList().get(0);
        if (canHaveSecondProduct()) {
            animalProductType = this.animalType.getProductList().get(1);
        }
        AnimalProduct animalProduct = new AnimalProduct(animalProductType);
        animalProduct.setProductQuality(ProductQuality.getQualityByDouble(quality));
        this.todayProduct = animalProduct;
    }

    public void changeFriendShip(int value) {
        int oldValue = this.relationShipQuality;
        this.setRelationShipQuality(Math.min(1000, Math.max(0, oldValue + value)));
    }

    private double generateQuality() {
        Random random = new Random();
        double R = random.nextDouble(0, 1);
        return ((double) this.relationShipQuality / 1000) * (0.5 + 0.5 * R);
    }

    private boolean canHaveSecondProduct() {
        if (this.relationShipQuality < 100) {
            return false;
        }
        if (this.animalType.getProductList().size() != 2) {
            return false;
        }
        Random random = new Random();
        double R = random.nextDouble(0.5, 1.5);
        double probability = (this.relationShipQuality + (150 * R)) / 1500;
        return probability > 0.5;
    }

    public Sprite getSprite() {
        if (sprite == null) init();
        return sprite;
    }

    public Texture getTexture() {
        if (texture == null) init();
        return texture;
    }

    private void init() {
        this.texture = animalType.getTexture();
        this.sprite = new Sprite(animalType.getTexture());
        this.sprite.setSize(App.tileWidth, App.tileHeight);
    }
}
