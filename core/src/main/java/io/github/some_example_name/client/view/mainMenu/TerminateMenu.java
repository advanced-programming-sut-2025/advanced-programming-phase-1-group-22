package io.github.some_example_name.client.view.mainMenu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import io.github.some_example_name.client.GameClient;
import io.github.some_example_name.client.MainGradle;
import io.github.some_example_name.client.controller.WorldController;
import io.github.some_example_name.common.model.Salable;
import io.github.some_example_name.common.model.records.Response;
import io.github.some_example_name.common.model.relations.Player;
import io.github.some_example_name.common.utils.App;
import io.github.some_example_name.common.utils.GameAsset;
import io.github.some_example_name.server.service.GameService;
import lombok.Setter;

import java.util.ArrayList;

@Setter
public class TerminateMenu extends PopUp {
    private Window window;
    private Label label;
    private TextButton waitLabel;
    private TextButton inFavor, against;
    private Table inventory;
    private TextButton pauseButton;
    private TextButton endButton;
    private Integer state = 0;

    public void createMenu(Stage stage, Skin skin, WorldController worldController) {
        super.createMenu(stage, skin, worldController);
        createInventory(skin, getMenuGroup(), stage);
    }

    @Override
    protected void handleDragRelease(InputEvent event, float x, float y, int pointer, Image itemImage, Salable item, Image dragImage, Boolean flag) {
    }


    public void setState(int i) {
        if (i == 0) {
            inventory.add(pauseButton).row();
            inventory.add(endButton).row();
        } else {
            inventory.add(label).expandX().colspan(2).row();
            inventory.add(inFavor).width(200).padRight(20);
            inventory.add(against).width(200).row();
        }
        state = i;
    }

    private void createInventory(Skin skin, Group menuGroup, Stage stage) {
        OrthographicCamera camera = MainGradle.getInstance().getCamera();

        window = new Window("", skin);
        window.setSize(camera.viewportWidth * 0.7f, camera.viewportHeight * 0.7f);

        window.setPosition(
            camera.viewportWidth - window.getWidth() / 2f,
            camera.viewportHeight - window.getHeight() / 2f
        );
        window.setMovable(false);

        inventory = new Table();

        pauseButton = new TextButton("Save and leave the game", skin);
        endButton = new TextButton("End the game", skin);
        label = new Label("Are you in favor of ending the game", skin);
        inFavor = new TextButton("Yes", skin);
        waitLabel = new TextButton("Wait for server response!", skin);
        against = new TextButton("No", skin);

        ArrayList<Actor> array = new ArrayList<>();
        array.add(window);
        ImageButton exitButton = provideExitButton(array);

        Table content = new Table();
        content.setFillParent(true);
        content.add(inventory).width(window.getWidth()).height(window.getHeight() * 0.8f - 100).padBottom(20).padTop(50).row();

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

                if (pauseButton.isChecked()) {
                    Response response = getGameService().exitGame();
                    getController().showResponse(response);
                    if (response.shouldBeBack()) {
                        GameClient.getInstance().saveGame(App.getInstance().getCurrentLobby().getId());
                        GameService.getInstance().finalTermination();
                        App.getInstance().getCurrentLobby().setGameStart(false);
                        App.getInstance().getCurrentLobby().setGameServerSaved(true);
                        close();
                        MainGradle.getInstance().getScreen().dispose();
                        MainGradle.getInstance().setScreen(new LobbyMenu(GameAsset.SKIN_MENU));
                    }
                    pauseButton.setChecked(false);
                }

                if (endButton.isChecked()) {
                    GameClient.getInstance().emptyAction("propose_end");
                    window.remove();
                    close();
                    endButton.setChecked(false);
                }

                if (against.isChecked()) {
                    GameClient.getInstance().emptyAction("stop_termination");
                    getController().showResponse(new Response("Termination stopped.", true));
                    undoTermination();
                    return;
                }

                if (inFavor.isChecked()) {
                    GameClient.getInstance().emptyAction("continue_termination");
                    inFavor.setChecked(false);
                    label.remove();
                    inFavor.remove();
                    against.remove();
                    inventory.center();
                    inventory.add(waitLabel).expandX().colspan(2);
                    state = 2;
                }
                super.act(delta);
            }
        };
        group.addActor(window);
        group.addActor(exitButton);
        menuGroup.addActor(group);
    }

    public void undoTermination() {
        Gdx.app.postRunnable(() ->
            getController().showResponse(new Response("Termination stopped.", false))
        );
        window.remove();
        close();
        against.setChecked(false);
    }

    public void terminate() {
        Gdx.app.postRunnable(() -> {
            getController().showResponse(getGameService().finalTermination());
            close();
            MainGradle.getInstance().getScreen().dispose();
            MainGradle.getInstance().setScreen(new LobbyMenu(GameAsset.SKIN_MENU));
        });
    }

    @Override
    protected void endTask(ArrayList<Actor> array, ImageButton exitButton) {
        if (state != 1) super.endTask(array, exitButton);
    }
}
