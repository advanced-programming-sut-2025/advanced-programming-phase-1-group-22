package io.github.some_example_name.client.view.mainMenu;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import io.github.some_example_name.client.MainGradle;
import io.github.some_example_name.client.controller.WorldController;
import io.github.some_example_name.common.model.Salable;
import io.github.some_example_name.common.model.relations.NPC;
import io.github.some_example_name.server.service.RelationService;

import java.util.ArrayList;

public class DialogMenu extends PopUp {
    private final NPC npc;
    private final RelationService relationService = RelationService.getInstance();

    public DialogMenu(NPC npc) {
        this.npc = npc;
    }

    @Override
    public void createMenu(Stage stage, Skin skin, WorldController playerController) {
        super.createMenu(stage, skin, playerController);
        OrthographicCamera camera = MainGradle.getInstance().getCamera();
        Window window = new Window("", skin);
        window.setSize(camera.viewportWidth * 0.5f, camera.viewportHeight * 0.5f);
        window.setPosition(
            (MainGradle.getInstance().getCamera().viewportWidth - window.getWidth()) / 2f,
            (MainGradle.getInstance().getCamera().viewportHeight - window.getHeight()) / 2f
        );
        window.setMovable(false);

        Table dialog = new Table();
        ScrollPane scrollPane = new ScrollPane(dialog, skin);
        scrollPane.setFadeScrollBars(false);
        scrollPane.setScrollbarsOnTop(true);
        scrollPane.setScrollingDisabled(true, false);
        scrollPane.setScrollBarPositions(true, true);
        scrollPane.setForceScroll(false, true);
        scrollPane.layout();
        scrollPane.setTouchable(Touchable.enabled);
        dialog.pack();

        Image icon = new Image(npc.getType().getTextureIcon());
        dialog.add(icon).pad(5).right();
        boolean flag = false;
        for (String string : wrapString(relationService.meetNpc(npc).message(), 35)) {
            if (flag)  dialog.add();
            flag = true;
            Label dialogLabel = new Label(string, skin);
            dialog.add(dialogLabel).pad(5).left();
            dialog.row();
        }


        ArrayList<Actor> array = new ArrayList<>();
        array.add(window);
        ImageButton exitButton = provideExitButton(array);

        Table content = new Table();
        content.setFillParent(true);
        content.add(scrollPane).width(camera.viewportWidth * 0.5f).height(camera.viewportHeight * 0.5f - 100).padBottom(10).padTop(80).row();

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
}
