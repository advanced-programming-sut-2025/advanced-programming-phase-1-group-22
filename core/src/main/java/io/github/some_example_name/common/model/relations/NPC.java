package io.github.some_example_name.common.model.relations;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.utils.Timer;
import io.github.some_example_name.client.GameClient;
import io.github.some_example_name.common.model.NpcWalkingStrategy;
import io.github.some_example_name.common.model.Pair;
import io.github.some_example_name.common.model.Tile;
import io.github.some_example_name.common.model.structure.stores.Store;
import io.github.some_example_name.common.utils.App;
import io.github.some_example_name.common.utils.GameAsset;
import lombok.Getter;
import lombok.NoArgsConstructor;
import io.github.some_example_name.common.model.Actor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;

@NoArgsConstructor
@Getter
@Setter
public class NPC extends Actor {
    private NPCType type;
    private transient Sprite sprite;
    private boolean haveDialog = false;
    private transient Sprite spriteDialogBox;
    private boolean giftedToday = false;
    private int movingState = 0;
    private Store store;
    private NPCHouse house;

    public NPC(NPCType type, Store store) {
        this.type = type;
        this.store = store;
    }

    private void init() {
        this.sprite = new Sprite(type.getTextureCharacter());
        this.sprite.setSize((float) App.tileWidth, (float) (App.tileHeight * 1.5));
        this.spriteDialogBox = new Sprite(GameAsset.DIALOG_BOX);
        this.spriteDialogBox.setSize(App.tileWidth / 2f, App.tileHeight / 2f);
    }

    public Sprite getSprite() {
        if (sprite == null) init();
        return sprite;
    }

    public Sprite getSpriteDialogBox() {
        if (spriteDialogBox == null) init();
        return spriteDialogBox;
    }

    @Override
    public String getName() {
        return type.getName();
    }

    @Override
    public String getNickname() {
        return getName();
    }

    public void walk(Tile tile) {
        NpcWalkingStrategy walkingStrategy = new NpcWalkingStrategy();
        ArrayList<Pair> path = walkingStrategy.getPath(
            new Pair(getTiles().getFirst().getX(), getTiles().getFirst().getY()),
            new Pair(tile.getX(), tile.getY()), house
        );
        GameClient.getInstance().npcWalk(getName(), path);
    }

    public void applyWalk(LinkedList<Pair> path) {
        int i = 0;
        for (Pair pair : path) {
            Timer.schedule(new Timer.Task() {
                @Override
                public void run() {
                    getTiles().clear();
                    getTiles().add(App.getInstance().getCurrentGame().getTiles()[pair.getX()][pair.getY()]);
                }
            }, i++);
        }
    }


    public void goToStore() {
        Random random = new Random();
        walk(store.getTiles().get(random.nextInt(store.getTiles().size())));
        movingState = 1;
    }

    public void moveRandomly() {
        Random random = new Random();
        int maxX = 159;
        int maxY = 119;
        int x = random.nextInt(maxX), y;
        if (x < (maxX + 1)/2 - 3 || x >= (maxX + 1)/2 + 3) y = random.nextInt(6) + (maxY + 1)/2 - 3;
        else y = random.nextInt(maxY);
        walk(App.getInstance().getCurrentGame().getTiles()[x][y]);
        movingState = 2;
    }

    public void goHome() {
        walk(house.getTiles().getFirst());
    }
}
