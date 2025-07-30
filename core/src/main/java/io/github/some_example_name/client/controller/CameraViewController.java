package io.github.some_example_name.client.controller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import io.github.some_example_name.client.GameClient;
import io.github.some_example_name.client.MainGradle;
import io.github.some_example_name.common.model.Salable;
import io.github.some_example_name.common.model.Tile;
import io.github.some_example_name.common.model.dto.TradePriceDto;
import io.github.some_example_name.common.model.dto.TradeProductDto;
import io.github.some_example_name.common.model.relations.Player;
import io.github.some_example_name.common.model.tools.WateringCan;
import io.github.some_example_name.server.service.GameService;
import io.github.some_example_name.server.service.RelationService;
import io.github.some_example_name.common.utils.App;
import io.github.some_example_name.common.utils.GameAsset;
import io.github.some_example_name.client.view.GameView;
import io.github.some_example_name.client.view.mainMenu.InventoryMenu;
import io.github.some_example_name.client.view.mainMenu.NotificationMenu;
import io.github.some_example_name.client.view.mainMenu.PopUp;
import io.github.some_example_name.server.service.TradeService;

import java.util.ArrayList;
import java.util.List;

public class CameraViewController {
    private GameService gameMenuController = new GameService();
    private WorldController worldController;
    private Sprite energyContainer = new Sprite(GameAsset.BUTTON);
    private Sprite energy = new Sprite(GameAsset.GREEN_SQUARE);
    private Sprite eSprite = new Sprite(GameAsset.FILLED_BUTTON);
    private OrthographicCamera camera = MainGradle.getInstance().getCamera();
    private Group inventoryBar;
    private Table bar;
    private Group energyBar;
    private Image energyBackground;
    private Image energyFill;
    private final List<Stack> itemStacks = new ArrayList<>();
    private List<Salable> items = new ArrayList<>();
    private Integer currentToolIndex;
    private boolean journalVisibility = true;

    public CameraViewController(WorldController worldController) {
        this.worldController = worldController;
        initInventoryBar();
        initEnergyBar();
        initButtons();
    }

    public void update() {
        Sprite mainClock = App.getInstance().getCurrentGame().getTimeAndDate().updateBatch(
            MainGradle.getInstance().getBatch());
        updateEnergyBar();
        updateInventoryBar();
        updateButtons(mainClock);
        if (GameView.captureInput) {
            handleInput();
        }
        if (Gdx.input.isKeyPressed(Input.Keys.CONTROL_LEFT) && Gdx.input.isKeyJustPressed(Input.Keys.CAPS_LOCK)) {
            GameView.captureInput = !GameView.captureInput;
        }
    }

    private void updateButtons(Sprite mainClock) {
        bar.setPosition(mainClock.getX() + mainClock.getWidth() * 0.3f,mainClock.getY() - bar.getHeight());
    }

    private void initButtons() {
        Skin skin = GameAsset.SKIN;
        bar = new Table(skin);
        bar.top().left();

        ImageButton journal = new ImageButton(new TextureRegionDrawable(GameAsset.NOTIFICATION));
        ImageButton social = new ImageButton(new TextureRegionDrawable(GameAsset.SOCIAL_ICON));
        Image journalHolder = new Image(GameAsset.GOLDDISPLAY);
        Image socialHolder = new Image(GameAsset.GOLDDISPLAY);
        bar.add(journalHolder).size(50, 20).padRight(70);
        bar.add(socialHolder).size(50, 20).row();
        bar.add(journal).size(50, 50).padRight(70);
        bar.add(social).size(50, 50).row();
        Group buttons = new Group(){
            @Override
            public void act(float delta) {
                if (social.isChecked()) {
                    InventoryMenu inventoryMenu = new InventoryMenu();
                    inventoryMenu.setTabIndex(2);
                    inventoryMenu.createMenu(GameView.stage, GameAsset.SKIN, worldController);
                    social.setChecked(false);
                }
                boolean hasNotifications = !App.getInstance().getCurrentGame().getCurrentPlayer().getNotifications().isEmpty();
                if (journal.isChecked()) {
                    if (hasNotifications) {
                        NotificationMenu notificationMenu = new NotificationMenu();
                        notificationMenu.createMenu(GameView.stage, GameAsset.SKIN, worldController);
                        journal.setChecked(false);
                    }
                }
                if (hasNotifications != journalVisibility) {
                    journal.setColor(hasNotifications ? new Color(1,1,1,1) : new Color(1,1,1,0));
                    journalHolder.setDrawable(new TextureRegionDrawable(
                        hasNotifications ? GameAsset.GOLDDISPLAY : GameAsset.RAW
                    ));
                    journalVisibility = hasNotifications;
                }

                super.act(delta);
            }
        };
        buttons.setSize(bar.getPrefWidth(), bar.getPrefHeight());
        buttons.addActor(bar);
        GameView.stage.addActor(buttons);
    }


    private void initInventoryBar() {
        Table bar = new Table(GameAsset.SKIN);
        bar.setFillParent(true);
        bar.bottom().center().padBottom(20);

        for (int i = 0; i < 12; i++) {
            Image slot = new Image(new Texture(Gdx.files.internal("bar.png")));
            Stack stack = new Stack();
            stack.add(slot);
            itemStacks.add(stack);
            bar.add(stack).size(96, 96);
        }
        inventoryBar = new Group(){
            @Override
            public void act(float delta) {
                OrthographicCamera camera = MainGradle.getInstance().getCamera();
                bar.setPosition(camera.position.x - bar.getWidth() / 2f,camera.position.y - camera.viewportHeight / 2f + 40f);
                super.act(delta);
            }
        };
        inventoryBar.addActor(bar);
        GameView.stage.addActor(inventoryBar);
    }

    private void initEnergyBar(){
        Texture texture = GameAsset.GREEN_SQUARE;
        Texture texture1 = GameAsset.ENERGY_BAR_EMPTY;
        OrthographicCamera camera = MainGradle.getInstance().getCamera();
        TextureRegion backgroundRegion = new TextureRegion(texture1);
        TextureRegion fillRegion = new TextureRegion(texture);
        energyBackground = new Image(backgroundRegion);
        energyBackground.setSize(camera.viewportWidth * 0.03f,
            camera.viewportHeight * 0.3f);
        energyFill = new Image(fillRegion);
        energyFill.setSize(energyBackground.getWidth() * 0.5f,
            energyBackground.getHeight() / 215 * 170);
        energyFill.setPosition(energyBackground.getWidth() * 0.25f, energyBackground.getHeight() * 8 / 215);

        Stack energyStack = new Stack();
        energyStack.setSize(energyBackground.getWidth(), energyBackground.getHeight());
        energyStack.add(energyBackground);
        energyStack.add(energyFill);

        energyBar = new Group() {
            @Override
            public void act(float delta) {
                setPosition(camera.position.x + camera.viewportWidth/2f - energyBar.getWidth()*1.1f,
                    camera.position.y - camera.viewportHeight/2f + energyBar.getHeight()*0.3f);
                super.act(delta);
            }
        };

        energyBar.setSize(energyBackground.getWidth(), energyBackground.getHeight());
        energyBar.addActor(energyStack);
        GameView.stage.addActor(energyBar);
    }

    private void updateEnergyBar(){
        Player player = App.getInstance().getCurrentGame().getCurrentPlayer();
        float energyPercent = (float) player.getEnergy() / player.getMaxEnergy();

        float fullHeight = energyBackground.getHeight() * 175 / 215;
        float currentHeight = fullHeight * energyPercent;

        energyFill.setSize(energyBackground.getWidth() * 0.5f, currentHeight);
        energyBackground.setScale(1, player.getMaxEnergy()/200f);
        energyFill.setPosition(energyBackground.getWidth() * 0.25f, energyBackground.getHeight() * 3 / 215);
    }

    private void updateInventoryBar(){
        Player player = App.getInstance().getCurrentGame().getCurrentPlayer();
        items = new ArrayList<>(player.getInventory().getProducts().keySet());

        boolean flag = true;
        for (int i = 0; i < itemStacks.size(); i++) {
            Stack stack = itemStacks.get(i);
            while (stack.getChildren().size > 1) {
                stack.removeActorAt(1, true);
            }
            if (i < items.size()) {
                Salable item = items.get(i);
                Image itemImage = new Image(item.getTexture());
                itemImage.setSize(90, 90);
                Container<Label> labelContainer = PopUp.getLabelContainer(player.getInventory().getProducts(), item);
                if (item.equals(player.getCurrentCarrying())) {
                    itemImage.setColor(1f,1f, 1f,1f);
                    flag = false;
                }
                else itemImage.setColor(1f,1f,1f,0.5f);
                if (item instanceof WateringCan wateringCan) {
                    ProgressBar progressBar = new ProgressBar(0, wateringCan.getWateringCanType().getCapacity(), 1, false, GameAsset.SKIN);
                    progressBar.setValue(wateringCan.getRemain());
                    progressBar.setAnimateDuration(0.2f);
                    progressBar.setSize(80, 5);
                    progressBar.setTouchable(Touchable.disabled);
                    progressBar.setColor(Color.BLUE);

                    Table group = new Table();
                    group.add(itemImage).size(90, 90).row();
                    group.add(progressBar).width(80).height(8).padTop(4);

                    stack.add(group);
                    stack.add(labelContainer);
                }else {
                    stack.add(itemImage);
                    stack.add(labelContainer);
                }
            }
        }
        if (flag) {
            currentToolIndex = null;
        }
    }

    private void handleEnergyBar() {
        Player player = App.getInstance().getCurrentGame().getCurrentPlayer();
        energyContainer.setSize(camera.viewportWidth * 0.03f,
                camera.viewportHeight * 0.2f / 200 * player.getMaxEnergy());
        energyContainer.setPosition(camera.position.x + camera.viewportWidth/2f - energyContainer.getWidth()*1.1f,
                camera.position.y - camera.viewportHeight/2f + energyContainer.getHeight()*0.3f);
        energy.setSize(energyContainer.getWidth() * 0.8f,
                energyContainer.getHeight() * player.getEnergy() / player.getMaxEnergy() * 0.94f);
        energy.setPosition(energyContainer.getX() + 0.1f * energyContainer.getWidth(),
                energyContainer.getY() + 0.03f * energyContainer.getHeight());
        eSprite.setSize(energyContainer.getWidth(), energyContainer.getWidth());
        eSprite.setPosition(energyContainer.getX(), energyContainer.getY() + energyContainer.getHeight()*0.90f);

        energyContainer.draw(MainGradle.getInstance().getBatch());
        energy.draw(MainGradle.getInstance().getBatch());
        eSprite.draw(MainGradle.getInstance().getBatch());
        GameAsset.MAIN_FONT.draw(MainGradle.getInstance().getBatch(), "E",
                eSprite.getX() + eSprite.getWidth()*0.2f, eSprite.getY() + eSprite.getHeight()*0.8f);
    }

    public void handleInput(){
        Player player = App.getInstance().getCurrentGame().getCurrentPlayer();
        if (Gdx.input.isKeyPressed(Input.Keys.CONTROL_LEFT)) {
            if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_1)) {
                gameMenuController.toolEquip("normal hoe");
                gameMenuController.C_AddItem("cherry sapling", "1");
                gameMenuController.C_AddItem("deluxe retaining soil", "1");
                gameMenuController.useTool("south");
                Tile tile = App.getInstance().getCurrentGame().tiles[App.getInstance().getCurrentGame().getCurrentPlayer().getTiles().get(0).getX()][App.getInstance().getCurrentGame().getCurrentPlayer().getTiles().get(0).getY() - 1];
                gameMenuController.plantSeed("cherry sapling", tile);
                gameMenuController.fertilize("deluxe retaining soil", tile);
                gameMenuController.toolEquip("normal watering can");
                gameMenuController.useTool("south");
            }
            if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_3)) {
                gameMenuController.C_WeatherSet("RAINY");
                gameMenuController.C_AdvanceDate("1");
            }
            if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_4)) {
                gameMenuController.C_WeatherSet("SNOWY");
                gameMenuController.C_AdvanceDate("1");
            }
            if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_2)) {
                gameMenuController.C_WeatherSet("STORMY");
                gameMenuController.C_AdvanceDate("1");
            }
            if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_5)) {
                gameMenuController.C_AddItem("cherry bomb recipe", "1");
                gameMenuController.C_AddItem("bomb recipe", "1");
                gameMenuController.C_AddItem("mega bomb recipe", "1");
                gameMenuController.C_AddItem("Sprinklers recipe", "1");
                gameMenuController.C_AddItem("quality Sprinklers recipe", "1");
                gameMenuController.C_AddItem("iridium Sprinklers recipe", "1");
                gameMenuController.C_AddItem("charcoal klin recipe", "1");
                gameMenuController.C_AddItem("furnace recipe", "1");
                gameMenuController.C_AddItem("scare crow recipe", "1");
                gameMenuController.C_AddItem("deluxe scarecrow recipe", "1");
                gameMenuController.C_AddItem("bee house recipe", "1");
                gameMenuController.C_AddItem("cheese press recipe", "1");
                gameMenuController.C_AddItem("keg recipe", "1");
                gameMenuController.C_AddItem("loom recipe", "1");
                gameMenuController.C_AddItem("mayonnaise machine recipe", "1");
                gameMenuController.C_AddItem("oil maker recipe", "1");
                gameMenuController.C_AddItem("preserves jar recipe", "1");
                gameMenuController.C_AddItem("dehydrator recipe", "1");
                gameMenuController.C_AddItem("grass started recipe", "1");
                gameMenuController.C_AddItem("fish smoker recipe", "1");
                gameMenuController.C_AddItem("mystic tree seed recipe", "1");
                gameMenuController.C_AddItem("fiber", "60");
                gameMenuController.C_AddItem("fried egg recipe", "1");
                gameMenuController.C_AddItem("baked fish recipe", "1");
                gameMenuController.C_AddItem("salad recipe", "1");
                gameMenuController.C_AddItem("salmon dinner recipe", "1");
                gameMenuController.C_AddItem("hashbrowns recipe", "1");
                gameMenuController.C_AddItem("omelet recipe", "1");
                gameMenuController.C_AddItem("pancakes recipe", "1");
                gameMenuController.C_AddItem("bread recipe", "1");
                gameMenuController.C_AddItem("tortilla recipe", "1");
                gameMenuController.C_AddItem("pizza recipe", "1");
                gameMenuController.C_AddItem("maki roll recipe", "1");
                gameMenuController.C_AddItem("triple shot espresso recipe", "1");
                gameMenuController.C_AddItem("cookie recipe", "1");
                gameMenuController.C_AddItem("vegetable medley recipe", "1");
                gameMenuController.C_AddItem("farmer's lunch recipe", "1");
                gameMenuController.C_AddItem("survival burger recipe", "1");
                gameMenuController.C_AddItem("dish O' the Sea recipe", "1");
                gameMenuController.C_AddItem("seafoam pudding recipe", "1");
                gameMenuController.C_AddItem("miner's treat recipe", "1");
                gameMenuController.C_AddItem("triple shot espresso recipe", "1");
                gameMenuController.C_AddItem("triple shot espresso recipe", "1");
            }
            if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_6)) {
                TradeService.getInstance().tradePriceRequest(
                    new TradePriceDto("Roham1234", "offer", "flower", 1, 200));
            }
            if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_7)) {
                gameMenuController.C_AddItem("pizza", "5");
                gameMenuController.C_AddDollars("2000");
                TradeService.getInstance().tradeProductRequest(
                    new TradeProductDto("Roham1234", "offer", "flower", 1, "pizza", 1));
            }
            if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_8)) {
                TradeService.getInstance().tradePriceOffer(
                    new TradePriceDto("Roham1234", "offer", "pizza", 1, 200));
                gameMenuController.C_AddItem("bee_house", "1");
                gameMenuController.placeItem("bee_house", "south");
            }
            if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_9)) {
                TradeService.getInstance().tradeProductOffer(
                    new TradeProductDto("Roham1234", "offer", "pizza", 1, "flower", 1));
            }
            if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_0)) {
                gameMenuController.C_AddItem("flower", "2");
                gameMenuController.C_AddItem("wedding ring", "2");
            }
            if (Gdx.input.isKeyJustPressed(Input.Keys.MINUS)) {
                gameMenuController.C_AddItem("bee_house", "5");
                RelationService.getInstance().giveGift("Clara1234", "bee_house", 2);
                RelationService.getInstance().giveGift("Clara1234", "bee_house", 2);
            }
            if (Gdx.input.isKeyJustPressed(Input.Keys.EQUALS)) {
                RelationService.getInstance().talkToAnotherPlayer("Clara1234", "bee_house");
                RelationService.getInstance().talkToAnotherPlayer("Clara1234", "bee_house");
            }
        } else {
            if (Gdx.input.isKeyJustPressed(Input.Keys.F4)) {
                GameView.captureInput = false;
                GameView.screenshotting = true;
            }
            if (Gdx.input.isKeyJustPressed(Input.Keys.GRAVE)) {
                player.setCurrentCarrying(null);
                GameClient.getInstance().updatePlayerCarryingObject(player);
                currentToolIndex = -1;

            }
            int[] shortcuts = {Input.Keys.NUM_1, Input.Keys.NUM_2, Input.Keys.NUM_3, Input.Keys.NUM_4, Input.Keys.NUM_5,
                Input.Keys.NUM_6, Input.Keys.NUM_7, Input.Keys.NUM_8, Input.Keys.NUM_9, Input.Keys.NUM_0, Input.Keys.MINUS,
                Input.Keys.EQUALS
            };
            for (int i = 0; i < shortcuts.length; i++) {
                if (Gdx.input.isKeyJustPressed(shortcuts[i])) {
                    if (i < player.getInventory().getProducts().size()) {
                        player.setCurrentCarrying(items.get(i));
                        GameClient.getInstance().updatePlayerCarryingObject(player);
                        currentToolIndex = i;
                    }
                }
            }
            if (Gdx.input.isKeyJustPressed(Input.Keys.TAB)) {
                if (currentToolIndex == null) currentToolIndex = -1;
                player.setCurrentCarrying(items.get(++currentToolIndex % Math.min(12, player.getInventory().getProducts().size())));
                GameClient.getInstance().updatePlayerCarryingObject(player);
            }
            if (GameView.scrollY != 0) {
                int mod = Math.min(12, player.getInventory().getProducts().size());
                if (mod != 0) {
                    if (currentToolIndex == null) currentToolIndex = GameView.scrollY > 0 ? mod - 1 : 0;
                    currentToolIndex += (int) Math.ceil(GameView.scrollY / 3);
                    currentToolIndex %= mod;
                    currentToolIndex += mod;
                    currentToolIndex %= mod;
                    player.setCurrentCarrying(items.get(currentToolIndex));
                    GameClient.getInstance().updatePlayerCarryingObject(player);
                    GameView.scrollY = 0;
                }
            }
        }
    }
}
