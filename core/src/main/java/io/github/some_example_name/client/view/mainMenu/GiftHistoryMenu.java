package io.github.some_example_name.client.view.mainMenu;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Timer;
import io.github.some_example_name.client.MainGradle;
import io.github.some_example_name.client.controller.WorldController;
import io.github.some_example_name.common.model.Salable;
import io.github.some_example_name.common.model.records.Response;
import io.github.some_example_name.common.model.relations.Friendship;
import io.github.some_example_name.common.model.relations.Gift;
import io.github.some_example_name.common.model.relations.Player;
import io.github.some_example_name.server.service.RelationService;
import io.github.some_example_name.common.utils.App;
import io.github.some_example_name.common.utils.GameAsset;
import lombok.Setter;

import java.util.ArrayList;

@Setter
public class GiftHistoryMenu extends PopUp {
    private Friendship friendship;
    private Player friend;
    private Window window;
    private ScrollPane scrollPane;
    private Float scrollY;
    private ArrayList<ArrayList<ImageButton>> hearts = new ArrayList<>();
    private ArrayList<Boolean> handleHearts = new ArrayList<>();

    public void createMenu(Stage stage, Skin skin, WorldController worldController) {
        super.createMenu(stage, skin, worldController);
        if (friendship.getSecondPlayer() == App.getInstance().getCurrentGame().getCurrentPlayer()) {
            friend = (Player) friendship.getFirstPlayer();
        } else {
            friend = (Player) friendship.getSecondPlayer();
        }
        createInventory(skin, getMenuGroup(), stage);
    }

    @Override
    protected void handleDragRelease(InputEvent event, float x, float y, int pointer, Image itemImage, Salable item, Image dragImage, Boolean flag) {}

    private void createInventory(Skin skin, Group menuGroup, Stage stage) {
        Player currentPlayer = App.getInstance().getCurrentGame().getCurrentPlayer();
        OrthographicCamera camera = MainGradle.getInstance().getCamera();

        window = new Window("", skin);
        window.setSize(camera.viewportWidth * 0.7f, camera.viewportHeight * 0.7f);

        window.setPosition(
            camera.viewportWidth - window.getWidth() / 2f,
            camera.viewportHeight - window.getHeight() / 2f
        );
        window.setMovable(false);

        Table inventory = new Table();
        scrollPane = new ScrollPane(inventory, skin);
        inventory.pack();
        scrollPane.setFadeScrollBars(false);
        scrollPane.setScrollbarsOnTop(false);
        scrollPane.setScrollingDisabled(false, false);
        scrollPane.setScrollBarPositions(true, true);
        scrollPane.setForceScroll(false, true);
        scrollPane.layout();
        scrollPane.setTouchable(Touchable.enabled);
        Table otherPlayer = new Table();

        otherPlayer.add(new Image(friend.getAvatar())).size(200).row();
        otherPlayer.add(new Label(friend.getUser().getNickname(), skin)).width(200).expandX().row();
        int i = 0;
        for (Gift gift : friendship.getGifts()) {
            hearts.add(new ArrayList<>());
            boolean isGifting = gift.getGiver() == currentPlayer;
            Table giftTable = new Table(skin);
            giftTable.add(new Image(gift.getGift().getTexture())).size(50).padRight(15);
            giftTable.add(new Label(gift.getAmount().toString(), skin)).size(50).padRight(15);
            giftTable.add(new Label(gift.getGift().getName(), skin)).colspan(5).width(150).left().row();
            addRating(giftTable, gift, isGifting, i);
            Table rowWrapper = new Table();

            if (isGifting) {
                rowWrapper.add(giftTable).left().expandX();
                rowWrapper.add();
            } else {
                rowWrapper.add();
                rowWrapper.add(giftTable).right().expandX();
            }
            inventory.add(rowWrapper).expandX().fillX().row();
            i++;
        }

        ArrayList<Actor> array = new ArrayList<>();
        array.add(window);
        ImageButton exitButton = provideExitButton(array);

        Table content = new Table();
        content.setFillParent(true);
        content.center();
        content.add(otherPlayer).width(window.getWidth() * 0.15f);
        content.add(scrollPane).width(window.getWidth() * 0.8f).height(window.getHeight() - 100).padBottom(20).padTop(50).row();


        window.add(content).fill().pad(10);
        Group group = new Group() {
            @Override
            public void act(float delta) {
                window.setPosition(
                    (camera.viewportWidth - window.getWidth()) / 2f +
                        camera.position.x - camera.viewportWidth / 2,
                    (camera.viewportHeight - window.getHeight()) / 2f +
                        camera.position.y - camera.viewportHeight / 2
                );
                exitButton.setPosition(
                    window.getX() + window.getWidth() - exitButton.getWidth() / 2f + 16 - exitButton.getWidth(),
                    window.getY() + window.getHeight()
                );

                super.act(delta);
            }
        };
        group.addActor(window);
        group.addActor(exitButton);
        menuGroup.addActor(group);
        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                scrollPane.setScrollY(scrollY == null ? scrollPane.getMaxY() : Math.max(0, scrollY));
            }
        }, 0.2f);
    }

    private void addRating(Table giftTable, Gift gift, boolean isGifting, int k) {
        for (int i = 0; i < 5; i++) {
            hearts.get(hearts.size() - 1).add(null);
        }
        defaultRating(giftTable, gift);
        Drawable filled = new TextureRegionDrawable(GameAsset.SECRET_HEART);
        Drawable empty = new TextureRegionDrawable(GameAsset.EMPTY_HEART);
        int rate = (gift.getRate() == null) ? 0 : gift.getRate();
        handleHearts.add(false);
        if (!isGifting && rate == 0) {
            handleHearts.set(handleHearts.size() - 1, true);
            for (int i = 0; i < 5; i++) {
                int finalI = i;
                ImageButton heartButton = hearts.get(k).get(i);
                heartButton.addListener(new InputListener() {
                    @Override
                    public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                        if (handleHearts.get(k)) {
                            for (int j = 0; j < 5; j++) {
                                hearts.get(k).get(j).getStyle().imageUp = (j <= finalI) ? filled : empty;
                            }
                        }
                    }

                    @Override
                    public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                        if (handleHearts.get(k)) {
                            for (int j = 0; j < 5; j++) {
                                hearts.get(k).get(j).getStyle().imageUp = empty;
                            }
                        }
                    }

                    @Override
                    public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                        if (handleHearts.get(k)) {
                            for (int j = 0; j < 5; j++) {
                                hearts.get(k).get(j).getStyle().imageUp = (j <= finalI) ? filled : empty;
                            }
                            Response resp = RelationService.getInstance().rateGift(gift, friendship, finalI + 1);
                            if (!resp.shouldBeBack()) {
                                getController().showResponse(resp);
                            } else {
                                handleHearts.set(k, false);
                            }
                        } else {
                            getController().showResponse(new Response("You have already votes this gift."));
                        }
                        return true;
                    }
                });
            }
        }
    }

    private void defaultRating(Table giftTable, Gift gift) {
        int rate = (gift.getRate() == null) ? 0 : gift.getRate();
        for (int i = 0; i < 5; i++) {
            Drawable drawable = new TextureRegionDrawable(i < rate ? GameAsset.SECRET_HEART : GameAsset.EMPTY_HEART);
            hearts.get(hearts.size() - 1).set(i, new ImageButton(drawable));
            giftTable.add(hearts.get(hearts.size() - 1).get(i)).padRight(15);
        }
        giftTable.row();
    }
}
