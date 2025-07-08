package io.github.some_example_name.view.mainMenu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.Timer;
import io.github.some_example_name.MainGradle;
import io.github.some_example_name.controller.WorldController;
import io.github.some_example_name.model.*;
import io.github.some_example_name.model.enums.Gender;
import io.github.some_example_name.model.records.Response;
import io.github.some_example_name.model.relations.Player;
import io.github.some_example_name.service.RelationService;
import io.github.some_example_name.utils.App;
import io.github.some_example_name.view.GameView;
import lombok.Setter;

import java.util.Collections;

@Setter
public class FriendPopUp extends PopUp {
    private Player player;
    private Window window;

    public void createMenu(Stage stage, Skin skin, WorldController worldController) {
        super.createMenu(stage, skin, worldController);
        createInventory(skin, getMenuGroup(), stage);
    }

    @Override
    protected void handleDragRelease(InputEvent event, float x, float y, int pointer, Image itemImage, Salable item, Image dragImage, Boolean flag) {}

    private void close(Window window) {
        GameView.captureInput = true;
        window.remove();
    }

    private void createInventory(Skin skin, Group menuGroup, Stage stage) {
        OrthographicCamera camera = MainGradle.getInstance().getCamera();

        window = new Window("", skin);
        window.setSize(camera.viewportWidth * 0.3f, camera.viewportHeight * 0.35f);
        window.setMovable(false);

        Table info = new Table();

        info.pack();
        info.add(new Image(player.getAvatar())).size(70).padRight(10);
        info.add(new Label(player.getUser().getNickname(), skin)).width(100).row();
        TextButton hug = new TextButton("Hug!", skin);
        TextButton flower = new TextButton("Give Flower!", skin);
        TextButton marry = new TextButton("Ask Marriage!", skin);
        info.add(flower).colspan(2).expandX().row();
        info.add(hug).colspan(2).expandX().row();
        if (App.getInstance().getCurrentGame().getCurrentPlayer().getUser().getGender() == Gender.MALE &&
            player.getUser().getGender() == Gender.FEMALE &&
            App.getInstance().getCurrentGame().getCurrentPlayer().getCouple() == null) {
            info.add(marry).colspan(2).expandX().row();
        }
        hug.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                handleHug(player);
                return true;
            }
        });

        flower.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                handleFlower(player);
                return true;
            }
        });

        marry.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                handleAskMarriage(player);
                return true;
            }
        });

        Table content = new Table();
        content.setFillParent(true);
        content.add(info).width(window.getWidth()).height(window.getHeight() - 100).padBottom(20).padTop(50).row();


        window.add(content).fill().pad(10);
        Group group = new Group() {
            @Override
            public void act(float delta) {
                window.setPosition(
                    (player.getTiles().get(0).getX() + 1) * App.tileWidth,
                    player.getTiles().get(0).getY() * App.tileHeight
                );
                if (Gdx.input.isKeyJustPressed(Input.Keys.ANY_KEY)) {
                    if (!Gdx.input.isKeyPressed(Input.Keys.CONTROL_LEFT)) close(window);
                }
                if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
                    Vector3 mouse = new Vector3(GameView.screenX, GameView.screenY,0);
                    MainGradle.getInstance().getCamera().unproject(mouse);
                    Vector2 mouseWorld = new Vector2(mouse.x,mouse.y);
                    if (mouseWorld.x < window.getX() || mouseWorld.x > window.getX() + window.getWidth() ||
                        mouseWorld.y < window.getY() || mouseWorld.y > window.getY() + window.getHeight()) close(window);
                }

                super.act(delta);
            }
        };
        group.addActor(window);
        menuGroup.addActor(group);
    }

    private void handleAskMarriage(Player player) {
        Player currentPlayer = App.getInstance().getCurrentGame().getCurrentPlayer();
        Tile origin = currentPlayer.getTiles().getFirst();
        Tile dest = App.getInstance().getCurrentGame().tiles[player.getTiles().getFirst().getX()][player.getTiles().getFirst().getY() - 1];
        currentPlayer.getTiles().clear();
        currentPlayer.getTiles().add(dest);
        Direction direction = initialHandle(player, RelationService.getInstance().marry(player.getUser().getUsername(), "Wedding Ring"));
        if (direction == null) {
            currentPlayer.getTiles().clear();
            currentPlayer.getTiles().add(origin);
            return;
        }
        currentPlayer.setDirection(direction);
        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                currentPlayer.setProposal();
            }
        }, 0.5f);
        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                do {
                    getGameService().nextTurn();
                } while (player != App.getInstance().getCurrentGame().getCurrentPlayer());
                DoYouMarryMePopUp doYouMarryMePopUp = new DoYouMarryMePopUp();
                doYouMarryMePopUp.setPlayer(currentPlayer);
                doYouMarryMePopUp.setOrigin(origin);
                doYouMarryMePopUp.createMenu(stage, skin, getController());
            }
        }, 1);
    }

    private Direction initialHandle(Player player, Response resp) {
        window.remove();
        Player currentPlayer = App.getInstance().getCurrentGame().getCurrentPlayer();
        if (!resp.shouldBeBack()) {
            getController().showResponse(resp);
            return null;
        }
        GameView.captureInput = false;
        Direction direction = Direction.getByXAndY(
            player.getTiles().getFirst().getX() - currentPlayer.getTiles().getFirst().getX(),
            player.getTiles().getFirst().getY() - currentPlayer.getTiles().getFirst().getY()
        );
        if (direction == null) return null;
        player.setLazyDirection(direction.reverse());
        currentPlayer.setLazyDirection(direction);
        return direction;
    }

    private void handleFlower(Player player) {
        Direction direction = initialHandle(player,
            RelationService.getInstance().giveGift(player.getUser().getUsername(), "flower", 1));
        if (direction == null) return;
        getController().drawFlower(direction);
    }

    private void handleHug(Player player) {
        Direction direction = initialHandle(player, RelationService.getInstance().hug(player.getUser().getUsername()));
        if (direction == null) return;
        Player currentPlayer = App.getInstance().getCurrentGame().getCurrentPlayer();
        Tile origin = currentPlayer.getTiles().getFirst();
        currentPlayer.setDirection(direction);
        currentPlayer.getTiles().clear();
        currentPlayer.getTiles().addAll(player.getTiles());
        boolean flag = false;
        int x = -1, y = -1;
        for (Player player1 : App.getInstance().getCurrentGame().getPlayers()) {
            if (player1 == currentPlayer) break;
            if (player == player1) {
                flag = true;
                break;
            }
        }

        for (int i = 0; i < App.getInstance().getCurrentGame().getPlayers().size(); i++) {
            Player player1 = App.getInstance().getCurrentGame().getPlayers().get(i);
            if (player1 == currentPlayer) x = i;
            if (player == player1) y = i;
        }
        if (flag) {
            Collections.swap(App.getInstance().getCurrentGame().getPlayers(), x, y);
        }
        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                currentPlayer.getSprites().getFirst().setOffset(new Tuple<>( -0.1f, 0.1f));
                currentPlayer.setLazyDirection(Direction.SOUTH);
                player.setLazyDirection(Direction.NORTH);
            }
        }, 0.5f);
        boolean finalFlag = flag;
        int finalX = x;
        int finalY = y;
        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                currentPlayer.getSprites().getFirst().setOffset(new Tuple<>( 0f, 0f));
                currentPlayer.setDirection(direction);
                currentPlayer.getTiles().clear();
                currentPlayer.getTiles().add(origin);
            }
        }, 3f);
        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                player.setLazyDirection(direction.reverse());
                GameView.captureInput = true;
                if (finalFlag) {
                    Collections.swap(App.getInstance().getCurrentGame().getPlayers(), finalX, finalY);
                }
            }
        }, 3.3f);
    }
}
