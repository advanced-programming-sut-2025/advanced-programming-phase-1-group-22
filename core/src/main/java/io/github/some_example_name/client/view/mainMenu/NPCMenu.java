package io.github.some_example_name.client.view.mainMenu;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import io.github.some_example_name.client.MainGradle;
import io.github.some_example_name.client.controller.WorldController;
import io.github.some_example_name.common.model.Salable;
import io.github.some_example_name.common.model.enums.Season;
import io.github.some_example_name.common.model.records.Response;
import io.github.some_example_name.common.model.relations.Friendship;
import io.github.some_example_name.common.model.relations.Mission;
import io.github.some_example_name.common.model.relations.NPC;
import io.github.some_example_name.common.model.relations.Player;
import io.github.some_example_name.common.model.tools.BackPack;
import io.github.some_example_name.server.service.RelationService;
import io.github.some_example_name.common.utils.App;
import io.github.some_example_name.common.utils.GameAsset;

import java.util.ArrayList;
import java.util.Map;

public class NPCMenu extends PopUp {
    private final NPC npc;
    private final RelationService relationService = RelationService.getInstance();

    public NPCMenu(NPC npc) {
        this.npc = npc;
    }

    @Override
    public void createMenu(Stage stage, Skin skin, WorldController playerController) {
        super.createMenu(stage, skin, playerController);
        Player currentPlayer = App.getInstance().getCurrentGame().getCurrentPlayer();
        Window window = new Window("", skin);
        window.setSize(900, 700);
        window.setPosition(
            (MainGradle.getInstance().getCamera().viewportWidth - window.getWidth()) / 2f,
            (MainGradle.getInstance().getCamera().viewportHeight - window.getHeight()) / 2f
        );
        window.setMovable(false);
        Friendship friendship = relationService.getFriendShip(currentPlayer, npc);

        Table NPC = new Table();
        NPC.align(Align.top);
        NPC.padTop(30);
        NPC.defaults().padBottom(20);
        NPC.pack();

        Image icon = new Image(npc.getType().getTextureIcon());
        TextButton favorite = new TextButton("Favorites", skin);
        favorite.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                getMenuGroup().clear();
                createFavoriteMenu();
            }
        });
        TextButton gift = new TextButton("Gift", skin);
        gift.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                getMenuGroup().clear();
                createGiftMenu();
            }
        });
        TextButton listQuest = new TextButton("Quests", skin);
        listQuest.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                getMenuGroup().clear();
                createMissionMenu();
            }
        });
        NPC.add(icon).size(256).center().row();
        NPC.add(favorite).width(200).center().row();
        NPC.add(gift).width(200).center().row();
        NPC.add(listQuest).width(200).center().row();

        Image iconHeart = new Image(GameAsset.SECRET_HEART);
        Label friendshipLabel = new Label("Friendship: ", skin);
        ProgressBar xpBar = new ProgressBar(0, (friendship.getFriendShipLevel() + 1) * 200, 1, false, skin);
        xpBar.setValue(friendship.getXp());
        xpBar.setAnimateDuration(0.2f);
        xpBar.setWidth(300);
        Label level = new Label("Level: " + friendship.getFriendShipLevel(), skin);
        Table friendshipRow = new Table();
        friendshipRow.align(Align.left);
        friendshipRow.defaults().spaceRight(20).align(Align.center);
        friendshipRow.add(iconHeart).size(64).padLeft(10);
        friendshipRow.add(friendshipLabel);
        friendshipRow.add(xpBar).width(300);
        friendshipRow.add(level).spaceRight(0);
        NPC.add(friendshipRow).padTop(30).expandX().fillX().row();

        ArrayList<Actor> array = new ArrayList<>();
        array.add(window);
        ImageButton exitButton = provideExitButton(array);

        Table content = new Table();
        content.setFillParent(true);
        content.add(NPC).width(600).padTop(20).expandY().top();
        window.add(content).expand().fill().pad(10);
        Group group = new Group() {
            @Override
            public void act(float delta) {
                window.setPosition(
                    (MainGradle.getInstance().getCamera().viewportWidth - window.getWidth()) / 2f +
                        MainGradle.getInstance().getCamera().position.x - MainGradle.getInstance().getCamera().viewportWidth / 2,
                    (MainGradle.getInstance().getCamera().viewportHeight - window.getHeight()) / 2f +
                        MainGradle.getInstance().getCamera().position.y - MainGradle.getInstance().getCamera().viewportHeight / 2
                );
                exitButton.setPosition(
                    window.getX() + window.getWidth() - exitButton.getWidth() / 2f + 16,
                    window.getY() + window.getHeight() - exitButton.getHeight() / 2f
                );

                super.act(delta);
            }
        };
        group.addActor(window);
        group.addActor(exitButton);
        getMenuGroup().addActor(group);
    }

    @Override
    protected void handleDragRelease(InputEvent event, float x, float y, int pointer, Image itemImage, Salable item, Image dragImage, Boolean flag) {
    }

    private void createGiftMenu() {
        OrthographicCamera camera = MainGradle.getInstance().getCamera();
        Window window = new Window("", skin);
        window.setSize(camera.viewportWidth * 0.5f, camera.viewportHeight * 0.3f);
        window.setPosition(
            (MainGradle.getInstance().getCamera().viewportWidth - window.getWidth()) / 2f,
            (MainGradle.getInstance().getCamera().viewportHeight - window.getHeight()) / 2f
        );
        window.setMovable(false);
        Player currentPlayer = App.getInstance().getCurrentGame().getCurrentPlayer();

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

        int maxCol = 9;
        int maxRow;
        BackPack backPack = App.getInstance().getCurrentGame().getCurrentPlayer().getInventory();
        if (backPack.getBackPackType().getIsInfinite()) {
            synchronized (backPack.getProducts()){
                maxRow = Math.max(5, backPack.getProducts().size() / maxCol + 1);
            }
        } else {
            maxRow = (int) Math.ceil((double) backPack.getBackPackType().getCapacity() / maxCol);
        }

        for (int row = 0; row < maxRow; row++) {
            for (int col = 0; col < maxCol; col++) {
                Image slot = new Image(slotTexture);

                int index = row * 9 + col;
                synchronized (currentPlayer.getInventory().getProducts()){
                    if (index < currentPlayer.getInventory().getProducts().size()) {
                        java.util.List<Salable> items = new ArrayList<>(currentPlayer.getInventory().getProducts().keySet());
                        Salable item = items.get(index);
                        Image itemImage = new Image(item.getTexture());
                        ArrayList<ScrollPane> list = new ArrayList<>();
                        list.add(scrollPane);
                        itemImage.addListener(new ClickListener() {
                            @Override
                            public void clicked(InputEvent event, float x, float y) {
                                createGiftDialog(item);
                            }
                        });
                        int count = currentPlayer.getInventory().getProducts().get(item);
                        Label countLabel = new Label(String.valueOf(count), skin);
                        countLabel.setFontScale(0.7f);
                        countLabel.setAlignment(Align.right);
                        countLabel.setColor(Color.GREEN);
                        Container<Label> labelContainer = new Container<>(countLabel);
                        labelContainer.setFillParent(false);
                        labelContainer.setSize(30, 20);
                        labelContainer.setPosition(66, 5);
                        itemImage.setColor(1, 1, 1, 1f);
                        itemImage.setSize(90, 90);
                        Stack stack = new Stack();
                        stack.add(slot);
                        stack.add(itemImage);
                        stack.add(labelContainer);
                        inventory.add(stack).size(96, 96);
                    } else {
                        inventory.add(slot).size(96, 96);
                    }
                }
            }
            inventory.row();
        }

        ArrayList<Actor> array = new ArrayList<>();
        array.add(window);
        ImageButton exitButton = new ImageButton(new TextureRegionDrawable(new TextureRegion(GameAsset.EXIT_BUTTON)));
        exitButton.setSize(32, 32);

        exitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                createMenu(stage, skin, getController());
            }
        });

        Table content = new Table();
        content.setFillParent(true);
        content.add(scrollPane).width(600).height(220).padBottom(20).padTop(50).row();

        window.add(content).expand().fill().pad(10);
        Group group = new Group() {
            @Override
            public void act(float delta) {
                window.setPosition(
                    (MainGradle.getInstance().getCamera().viewportWidth - window.getWidth()) / 2f +
                        MainGradle.getInstance().getCamera().position.x - MainGradle.getInstance().getCamera().viewportWidth / 2,
                    (MainGradle.getInstance().getCamera().viewportHeight - window.getHeight()) / 2f +
                        MainGradle.getInstance().getCamera().position.y - MainGradle.getInstance().getCamera().viewportHeight / 2
                );
                exitButton.setPosition(
                    window.getX() + window.getWidth() - exitButton.getWidth() / 2f + 16,
                    window.getY() + window.getHeight() - exitButton.getHeight() / 2f
                );

                super.act(delta);
            }
        };
        group.addActor(window);
        group.addActor(exitButton);
        getMenuGroup().addActor(group);
    }

    private void createGiftDialog(Salable salable) {
        final Window giftWindow = new Window("Gift", GameAsset.SKIN);
        giftWindow.setSize(700, 500);
        giftWindow.setModal(true);
        giftWindow.setMovable(false);

        ImageButton exitButton = new ImageButton(new TextureRegionDrawable(new TextureRegion(GameAsset.EXIT_BUTTON)));
        exitButton.setSize(32, 32);

        exitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                giftWindow.remove();
                exitButton.remove();
                getMenuGroup().clear();
                createGiftMenu();
            }
        });

        Table content = new Table();

        Image image = new Image(salable.getTexture());
        image.setSize(64, 64);
        Label name = new Label(salable.getName(), GameAsset.SKIN);

        Table topRow = new Table();
        topRow.add(image).pad(10);
        topRow.add(name).pad(10);
        content.add(topRow).pad(10).row();


        TextButton buyButton = new TextButton("Gift", GameAsset.SKIN);
        buyButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                giftWindow.remove();
                exitButton.remove();
                getController().showResponse(relationService.giftNPC(npc, salable.getName()));
                getMenuGroup().clear();
                createGiftMenu();
            }
        });

        content.add(buyButton).pad(10).row();

        giftWindow.add(content).pad(20);

        Group group = new Group() {
            @Override
            public void act(float delta) {
                giftWindow.setPosition(
                    (MainGradle.getInstance().getCamera().viewportWidth - giftWindow.getWidth()) / 2f +
                        MainGradle.getInstance().getCamera().position.x - MainGradle.getInstance().getCamera().viewportWidth / 2,
                    (MainGradle.getInstance().getCamera().viewportHeight - giftWindow.getHeight()) / 2f +
                        MainGradle.getInstance().getCamera().position.y - MainGradle.getInstance().getCamera().viewportHeight / 2
                );
                exitButton.setPosition(
                    giftWindow.getX() + giftWindow.getWidth() - exitButton.getWidth() / 2f + 16,
                    giftWindow.getY() + giftWindow.getHeight() - exitButton.getHeight() / 2f
                );
                super.act(delta);
            }
        };
        group.addActor(giftWindow);
        group.addActor(exitButton);
        getMenuGroup().addActor(group);
    }

    private void createFavoriteMenu() {
        OrthographicCamera camera = MainGradle.getInstance().getCamera();
        Window window = new Window("", skin);
        window.setSize(camera.viewportWidth * 0.3f, camera.viewportHeight * 0.7f);
        window.setPosition(
            (MainGradle.getInstance().getCamera().viewportWidth - window.getWidth()) / 2f,
            (MainGradle.getInstance().getCamera().viewportHeight - window.getHeight()) / 2f
        );
        window.setMovable(false);
        Table favorites = new Table();
        for (Salable favorite : npc.getType().getFavorites()) {
            Image icon = new Image(favorite.getTexture());
            Label name = new Label(favorite.getName(), skin);
            favorites.add(icon).size(128).center().row();
            favorites.add(name).center().row();
        }

        Table content = new Table();
        content.setFillParent(true);
        content.add(favorites).width(600).height(220).padBottom(20).padTop(50).row();

        window.add(content).expand().fill().pad(10);
        ImageButton exitButton = new ImageButton(new TextureRegionDrawable(new TextureRegion(GameAsset.EXIT_BUTTON)));
        exitButton.setSize(32, 32);

        exitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                window.remove();
                exitButton.remove();
                getMenuGroup().clear();
                createMenu(stage, skin, getController());
            }
        });
        Group group = new Group() {
            @Override
            public void act(float delta) {
                window.setPosition(
                    (MainGradle.getInstance().getCamera().viewportWidth - window.getWidth()) / 2f +
                        MainGradle.getInstance().getCamera().position.x - MainGradle.getInstance().getCamera().viewportWidth / 2,
                    (MainGradle.getInstance().getCamera().viewportHeight - window.getHeight()) / 2f +
                        MainGradle.getInstance().getCamera().position.y - MainGradle.getInstance().getCamera().viewportHeight / 2
                );
                exitButton.setPosition(
                    window.getX() + window.getWidth() - exitButton.getWidth() / 2f + 16,
                    window.getY() + window.getHeight() - exitButton.getHeight() / 2f
                );

                super.act(delta);
            }
        };
        group.addActor(window);
        group.addActor(exitButton);
        getMenuGroup().addActor(group);
    }

    private void createMissionMenu() {
        OrthographicCamera camera = MainGradle.getInstance().getCamera();
        Window window = new Window("Quests", skin);
        window.setSize(700, 500);
        window.setMovable(false);

        Table questTable = new Table();
        questTable.align(Align.top);
        questTable.defaults().pad(10);

        Map<Mission, Boolean> missions = relationService.getMissions(npc);
        Player player = App.getInstance().getCurrentGame().getCurrentPlayer();
        Season season = App.getInstance().getCurrentGame().getTimeAndDate().getSeason();
        Friendship friendship = relationService.getFriendShip(player, npc);
        int count = 1;
        for (Map.Entry<Mission, Boolean> missionBooleanEntry : missions.entrySet()) {
            boolean unlocked = missionBooleanEntry.getValue() && missionBooleanEntry.getKey().isAvailable(friendship.getFriendShipLevel(), season);
            TextButton questBtn = new TextButton("Mission " + count, skin);
            questBtn.setColor(unlocked ? Color.WHITE : Color.GRAY);
            questBtn.setDisabled(!unlocked);
            questBtn.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    createMissionDetailWindow(missionBooleanEntry.getKey(), unlocked);
                }
            });
            questTable.add(questBtn).width(500).height(60).row();
            count += 1;
        }

        window.add(questTable).expand().fill();

        ImageButton exitButton = new ImageButton(new TextureRegionDrawable(new TextureRegion(GameAsset.EXIT_BUTTON)));
        exitButton.setSize(32, 32);
        exitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                getMenuGroup().clear();
                createMenu(stage, skin, getController());
            }
        });

        Group group = new Group() {
            @Override
            public void act(float delta) {
                window.setPosition(
                    (camera.viewportWidth - window.getWidth()) / 2f + camera.position.x - camera.viewportWidth / 2,
                    (camera.viewportHeight - window.getHeight()) / 2f + camera.position.y - camera.viewportHeight / 2
                );
                exitButton.setPosition(
                    window.getX() + window.getWidth() - exitButton.getWidth() / 2f + 16,
                    window.getY() + window.getHeight() - exitButton.getHeight() / 2f
                );
                super.act(delta);
            }
        };
        group.addActor(window);
        group.addActor(exitButton);
        getMenuGroup().addActor(group);
    }

    private void createMissionDetailWindow(Mission mission, boolean unlocked) {
        Window missionWindow = new Window("mission", skin);
        missionWindow.setSize(800, 600);
        missionWindow.setModal(true);
        missionWindow.setMovable(false);

        Table content = new Table(skin);
        content.defaults().pad(10);

        boolean done = mission.getDoer() != null;
        final Label[] doneLabel = {new Label("Status: " + (done ? "Completed by " + mission.getDoer().getName() : "Not completed"), skin)};

        Label levelLabel;
        if (mission.getRequiredLevel() != null) {
            levelLabel = new Label("Required Level: " + mission.getRequiredLevel(), skin);
        } else {
            levelLabel = new Label("Required Season: " + mission.getRequiredSeason().name(), skin);
        }

        Label reqLabel = new Label("Required Items:", skin);
        Table reqTable = new Table();
        for (Map.Entry<Salable, Integer> entry : mission.getRequest().entrySet()) {
            Salable item = entry.getKey();
            int count = entry.getValue();
            reqTable.add(new Image(item.getTexture())).size(64);
            reqTable.add(new Label(item.getName(), skin)).padRight(20);
            reqTable.add(new Label("x" + count, skin)).padRight(20);
        }

        Label rewardLabel = new Label("Rewards:", skin);
        Table rewardTable = new Table();
        for (Map.Entry<Salable, Integer> entry : mission.getReward().entrySet()) {
            Salable item = entry.getKey();
            int count = entry.getValue();
            rewardTable.add(new Image(item.getTexture())).size(64);
            rewardTable.add(new Label(item.getName(), skin)).padRight(20);
            rewardTable.add(new Label("x" + count, skin)).padRight(20);
        }

        ImageButton exitButton = new ImageButton(new TextureRegionDrawable(new TextureRegion(GameAsset.EXIT_BUTTON)));
        exitButton.setSize(32, 32);
        exitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                missionWindow.remove();
                exitButton.remove();
            }
        });

        TextButton doMission = new TextButton("Do", skin);
        doMission.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (unlocked) {
                    getController().showResponse(relationService.completeMission(mission, npc));
                } else {
                    getController().showResponse(new Response("mission is not available"));
                }
                missionWindow.remove();
                exitButton.remove();
            }
        });

        content.add(doneLabel[0]).left().row();
        content.add(levelLabel).left().row();
        content.add(reqLabel).left().row();
        content.add(reqTable).left().row();
        content.add(rewardLabel).left().row();
        content.add(rewardTable).left().row();
        content.add(doMission).left().row();

        missionWindow.add(content).expand().fill().pad(20);

        Group group = new Group() {
            @Override
            public void act(float delta) {
                boolean done = mission.getDoer() != null;
                doneLabel[0] = new Label("Status: " + (done ? "Completed by " + mission.getDoer().getName() : "Not completed"), skin);
                missionWindow.setPosition(
                    (MainGradle.getInstance().getCamera().viewportWidth - missionWindow.getWidth()) / 2f +
                        MainGradle.getInstance().getCamera().position.x - MainGradle.getInstance().getCamera().viewportWidth / 2,
                    (MainGradle.getInstance().getCamera().viewportHeight - missionWindow.getHeight()) / 2f +
                        MainGradle.getInstance().getCamera().position.y - MainGradle.getInstance().getCamera().viewportHeight / 2
                );
                exitButton.setPosition(
                    missionWindow.getX() + missionWindow.getWidth() - exitButton.getWidth() / 2f + 16,
                    missionWindow.getY() + missionWindow.getHeight() - exitButton.getHeight() / 2f
                );
                super.act(delta);
            }
        };
        group.addActor(missionWindow);
        group.addActor(exitButton);
        getMenuGroup().addActor(group);
    }
}
