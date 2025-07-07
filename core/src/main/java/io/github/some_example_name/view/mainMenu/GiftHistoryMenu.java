package io.github.some_example_name.view.mainMenu;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import io.github.some_example_name.MainGradle;
import io.github.some_example_name.controller.WorldController;
import io.github.some_example_name.model.Farm;
import io.github.some_example_name.model.Salable;
import io.github.some_example_name.model.dto.SpriteHolder;
import io.github.some_example_name.model.records.Response;
import io.github.some_example_name.model.relations.Friendship;
import io.github.some_example_name.model.relations.Gift;
import io.github.some_example_name.model.relations.Player;
import io.github.some_example_name.model.tools.BackPack;
import io.github.some_example_name.service.RelationService;
import io.github.some_example_name.utils.App;
import io.github.some_example_name.utils.GameAsset;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Setter
public class GiftHistoryMenu extends PopUp {
    private Friendship friendship;
    private Player friend;
    private Window window;
    private ArrayList<ArrayList<ImageButton>> hearts = new ArrayList<>();

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
        ScrollPane scrollPane = new ScrollPane(inventory, skin);
        inventory.pack();
        scrollPane.setFadeScrollBars(false);
        scrollPane.setScrollbarsOnTop(true);
        scrollPane.setScrollingDisabled(false, false);
        scrollPane.setScrollBarPositions(true, true);
        scrollPane.setForceScroll(false, true);
        scrollPane.layout();
        scrollPane.setTouchable(Touchable.enabled);

        inventory.add(new Image(friend.getAvatar())).size(100).padRight(20);
        inventory.add(new Label(friend.getUser().getNickname(), skin)).colspan(3).width(80).expandX().row();
        int i = 0;
        for (Gift gift : friendship.getGifts()) {
            hearts.add(new ArrayList<>());
            boolean isGifting = gift.getGiver() == currentPlayer;
            Table giftTable = new Table(skin);
            if (isGifting) giftTable.left();
            else {
                inventory.add();
                inventory.add();
                giftTable.right();
            }
            giftTable.add(new Image(gift.getGift().getTexture())).size(50).padRight(20);
            giftTable.add(new Label(gift.getAmount().toString(), skin)).size(50).padRight(20);
            giftTable.add(new Label(gift.getGift().getName(), skin)).colspan(3).width(80).left().expandX().row();
            addRating(giftTable, gift, isGifting, i);
            inventory.add(giftTable).colspan(2).expandX();
            if (isGifting) {
                inventory.add();
                inventory.add();
            }
            inventory.row();
            i++;
        }

        ArrayList<Actor> array = new ArrayList<>();
        array.add(window);
        ImageButton exitButton = provideExitButton(array);

        Table content = new Table();
        content.setFillParent(true);
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
    }

    private void addRating(Table giftTable, Gift gift, boolean isGifting, int k) {
        for (int i = 0; i < 5; i++) {
            hearts.getLast().add(null);
        }
        defaultRating(giftTable, gift);
        Drawable filled = new TextureRegionDrawable(GameAsset.SECRET_HEART);
        Drawable empty = new TextureRegionDrawable(GameAsset.EMPTY_HEART);
        int rate = (gift.getRate() == null) ? 0 : gift.getRate();
        if (!isGifting && rate == 0) {
            for (int i = 0; i < 5; i++) {
                int finalI = i;
                ImageButton heartButton = hearts.get(k).get(i);
                heartButton.addListener(new InputListener() {
                    @Override
                    public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                        for (int j = 0; j < 5; j++) {
                            hearts.get(k).get(j).getStyle().imageUp = (j <= finalI) ? filled : empty;
                        }
                    }

                    @Override
                    public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                        for (int j = 0; j < 5; j++) {
                            hearts.get(k).get(j).getStyle().imageUp = empty;
                        }
                    }

                    @Override
                    public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                        for (int j = 0; j < 5; j++) {
                            hearts.get(k).get(j).getStyle().imageUp = (j <= finalI) ? filled : empty;
                        }
                        Response resp = RelationService.getInstance().rateGift(friendship, gift, finalI + 1);
                        if (!resp.shouldBeBack()) {
                            getController().showResponse(resp);
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
            hearts.getLast().set(i, new ImageButton(drawable));
            giftTable.add(hearts.getLast().get(i)).padRight(10);
        }
        giftTable.row();
    }
}
