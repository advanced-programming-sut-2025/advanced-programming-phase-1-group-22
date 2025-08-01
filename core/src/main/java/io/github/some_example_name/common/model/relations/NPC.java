package io.github.some_example_name.common.model.relations;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.utils.Timer;
import io.github.some_example_name.client.GameClient;
import io.github.some_example_name.client.MainGradle;
import io.github.some_example_name.common.model.*;
import io.github.some_example_name.common.model.dto.SpriteHolder;
import io.github.some_example_name.common.model.structure.stores.Store;
import io.github.some_example_name.common.utils.App;
import io.github.some_example_name.common.utils.GameAsset;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;

@NoArgsConstructor
@Getter
@Setter
public class NPC extends Actor {
    private NPCType type;
    private boolean haveDialog = false;
    private String dialog = "";
    private transient SpriteHolder spriteDialogBox;
    private transient ArrayList<SpriteHolder> sprites;
    private boolean giftedToday = false;
    private int movingState = 0;
    private Store store;
    private NPCHouse house;
    private boolean dirChanged = false;
    private Direction direction = Direction.SOUTH;

    public NPC(NPCType type, Store store) {
        this.type = type;
        this.store = store;
    }

    private void init() {
        this.sprites = new ArrayList<>();
        sprites.add(new SpriteHolder(type.getLazy(Direction.SOUTH)));
        sprites.get(0).setSize((float) App.tileWidth, (float) (App.tileHeight * 1.5));
        sprites.get(0).setChanged(true);
        spriteDialogBox = new SpriteHolder(new Sprite(GameAsset.DIALOG_BOX), new Tuple<>(0f, 1.5f));
        spriteDialogBox.setSize(App.tileWidth / 2f, App.tileHeight / 2f);
    }

    public ArrayList<SpriteHolder> getSprites() {
        if (this.sprites == null) init();
        if (dirChanged) {
            dirChanged = false;
            this.sprites.get(0).setSprite(type.getWalking(direction));
            ((AnimatedSprite) this.sprites.get(0).getSprite()).setLooping(true);
        }
        if (isHaveDialog() && sprites.size() < 2) {
            sprites.add(spriteDialogBox);
        } else if (!isHaveDialog() && sprites.size() > 1) {
            sprites.remove(1);
        }
        return sprites;
    }

    public void setDirection(Direction direction) {
        if (this.direction != direction) {
            dirChanged = true;
            this.direction = direction;
        }
    }

    public void setLazyDirection(Direction direction) {
        this.direction = direction;
        this.sprites.get(0).setSprite(type.getLazy(direction));
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
        new Thread(() -> {
            NpcWalkingStrategy walkingStrategy = new NpcWalkingStrategy();
            ArrayList<Pair> path = walkingStrategy.getPath(
                new Pair(getTiles().getFirst().getX(), getTiles().getFirst().getY()),
                new Pair(tile.getX(), tile.getY()), house
            );
            if (path == null) {
                movingState--;
                return;
            }
            GameClient.getInstance().npcWalk(type.getName(), path);
        }).start();
    }

    public void applyWalk(LinkedList<Pair> path) {
        if (path.isEmpty()) {
            setLazyDirection(Direction.SOUTH);
            return;
        }
        Pair pair = path.getFirst();
        Direction dir = Direction.getByXAndY(
            pair.getX() - getTiles().getFirst().getX(),
            pair.getY() - getTiles().getFirst().getY()
        );
        if (dir == null) {
            dir = Direction.SOUTH; //todo bug
        }
        setDirection(dir);
        getTiles().clear();
        getTiles().add(App.getInstance().getCurrentGame().getTiles()[pair.getX()][pair.getY()]);
        path.removeFirst();
        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                applyWalk(path);
            }
        }, 0.3f);
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
    }

    public void goHome() {
        walk(house.getTiles().getFirst());
        movingState = 3;
    }

    public void addDialog(String response) {
        haveDialog = true;
        dialog = response;
    }
}
