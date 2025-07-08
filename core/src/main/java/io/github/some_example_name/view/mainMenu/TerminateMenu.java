package io.github.some_example_name.view.mainMenu;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import io.github.some_example_name.MainGradle;
import io.github.some_example_name.controller.WorldController;
import io.github.some_example_name.model.Salable;
import io.github.some_example_name.model.records.Response;
import io.github.some_example_name.model.relations.Player;
import io.github.some_example_name.utils.App;
import lombok.Setter;

import java.util.ArrayList;

@Setter
public class TerminateMenu extends PopUp {
    private int thoseInFavor = 0;
    private Window window;
    private float scrollX = 0, scrollY = 0;
    private Label label;
    private TextButton inFavor, against;

    public void createMenu(Stage stage, Skin skin, WorldController worldController) {
        super.createMenu(stage, skin, worldController);
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

        TextButton pauseButton = new TextButton("Save and leave the game", skin);
        TextButton endButton = new TextButton("End the game", skin);
        label = new Label("Are you in favor of ending the game", skin);
        inFavor = new TextButton("Yes", skin);
        against = new TextButton("No", skin);
        inventory.add(pauseButton).row();
        inventory.add(endButton).row();

        ArrayList<Actor> array = new ArrayList<>();
        array.add(window);
        ImageButton exitButton = provideExitButton(array);

        Table content = new Table();
        content.setFillParent(true);
        content.add(inventory).width(window.getWidth()).height(window.getHeight()*0.8f - 100).padBottom(20).padTop(50).row();

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
                    getController().showResponse(getGameService().exitGame());
                    MainGradle.getInstance().getScreen().dispose();
                    MainGradle.getInstance().initialMenu();
                    pauseButton.setChecked(false);
                }

                if (endButton.isChecked()) {
                    thoseInFavor = 1;
                    getGameService().nextTurn();
                    pauseButton.setChecked(false);
                    pauseButton.remove();
                    endButton.remove();
                    inventory.add(label).width(100).colspan(2).row();
                    inventory.add(inFavor).width(50).padRight(20);
                    inventory.add(against).width(50).row();
                    endButton.setChecked(false);
                }

                if (against.isChecked()) {
                    getController().showResponse(new Response("Termination stopped.", true));
                    window.remove();
                    close();
                    against.setChecked(false);
                    return;
                }

                if (inFavor.isChecked()) {
                    inFavor.setChecked(false);
                    if (++thoseInFavor == App.getInstance().getCurrentGame().getPlayers().size()) {
                        getController().showResponse(getGameService().finalTermination());
                        MainGradle.getInstance().getScreen().dispose();
                        MainGradle.getInstance().initialMenu();
                    } else {
                        getGameService().nextTurn();
                    }
                }
                super.act(delta);
            }
        };
        group.addActor(window);
        group.addActor(exitButton);
        menuGroup.addActor(group);
    }
}
