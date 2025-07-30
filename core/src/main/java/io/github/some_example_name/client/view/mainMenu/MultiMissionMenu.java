package io.github.some_example_name.client.view.mainMenu;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import io.github.some_example_name.client.GameClient;
import io.github.some_example_name.client.MainGradle;
import io.github.some_example_name.client.controller.MultiMissionController;
import io.github.some_example_name.client.controller.WorldController;
import io.github.some_example_name.common.model.MultiMission;
import io.github.some_example_name.common.model.Salable;
import io.github.some_example_name.common.model.records.Response;
import io.github.some_example_name.common.model.relations.Player;
import io.github.some_example_name.common.utils.App;

import java.util.ArrayList;
import java.util.Map;

public class MultiMissionMenu extends PopUp {
    private final MultiMissionController controller = new MultiMissionController();

    @Override
    public void createMenu(Stage stage, Skin skin, WorldController playerController) {
        super.createMenu(stage, skin, playerController);
        OrthographicCamera camera = MainGradle.getInstance().getCamera();
        Window window = new Window("Multi Missions", skin);
        window.setSize(camera.viewportWidth * 0.5f, camera.viewportHeight * 0.4f);
        window.setPosition(
            (MainGradle.getInstance().getCamera().viewportWidth - window.getWidth()) / 2f,
            (MainGradle.getInstance().getCamera().viewportHeight - window.getHeight()) / 2f
        );
        window.setMovable(false);

        Table root = new Table();
        root.setFillParent(true);
        window.add(root).expand().fill().pad(10);

        TextButton availableButton = new TextButton("All Missions", skin);
        TextButton activeButton = new TextButton("Active Missions", skin);

        root.add(availableButton).padRight(10).padTop(100);
        root.add(activeButton).padTop(100).row();

        availableButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                createAllMissionWindow();
            }
        });

        activeButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                createActiveMissionWindow();
            }
        });

        ArrayList<Actor> array = new ArrayList<>();
        array.add(window);
        ImageButton exitButton = provideExitButton(array);

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

    private void createAllMissionWindow() {
        OrthographicCamera camera = MainGradle.getInstance().getCamera();
        Window window = new Window("All Missions", skin);
        window.setSize(camera.viewportWidth * 0.5f, camera.viewportHeight * 0.6f);
        window.setPosition(
            (MainGradle.getInstance().getCamera().viewportWidth - window.getWidth()) / 2f,
            (MainGradle.getInstance().getCamera().viewportHeight - window.getHeight()) / 2f
        );
        window.setMovable(false);

        Table root = new Table();
        root.setFillParent(true);
        window.add(root).expand().fill().pad(10);

        Table allMission = createAvailableMissionsTable(skin);
        root.add(allMission);

        ArrayList<Actor> array = new ArrayList<>();
        array.add(window);
        ImageButton exitButton = provideExitButton(array);

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

    private void createActiveMissionWindow() {
        OrthographicCamera camera = MainGradle.getInstance().getCamera();
        Window window = new Window("All Missions", skin);
        window.setSize(camera.viewportWidth * 0.5f, camera.viewportHeight * 0.6f);
        window.setPosition(
            (MainGradle.getInstance().getCamera().viewportWidth - window.getWidth()) / 2f,
            (MainGradle.getInstance().getCamera().viewportHeight - window.getHeight()) / 2f
        );
        window.setMovable(false);

        Table root = new Table();
        root.setFillParent(true);
        window.add(root).expand().fill().pad(10);

        Table activeMission = createActiveMissionsTable(skin);
        root.add(activeMission);

        ArrayList<Actor> array = new ArrayList<>();
        array.add(window);
        ImageButton exitButton = provideExitButton(array);

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

    private Table createAvailableMissionsTable(Skin skin) {
        Table table = new Table();
        ScrollPane scrollPane = new ScrollPane(table, skin);

        int count = 1;
        App.getInstance().getCurrentGame().applyPendingChanges();
        for (MultiMission mission : App.getInstance().getCurrentGame().getMissionsSnapshot()) {
            TextButton missionButton = new TextButton("Mission " + count, skin);
            missionButton.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    showAvailableMissionDetails(mission, skin);
                }
            });
            table.add(missionButton).expandX().fillX().pad(5).row();
            count += 1;
        }

        Table container = new Table();
        container.add(scrollPane).expand().fill();
        return container;
    }

    private Table createActiveMissionsTable(Skin skin) {
        Table table = new Table();
        ScrollPane scrollPane = new ScrollPane(table, skin);

        int count = 1;
        for (MultiMission activeMission : App.getInstance().getCurrentGame().getCurrentPlayer().getActiveMissions()) {
            if (!activeMission.isActive()) break;
            TextButton activeMissionButton = new TextButton("Active Mission " + count, skin);
            activeMissionButton.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    showActiveMissionStatus(activeMission, skin);
                }
            });
            table.add(activeMissionButton).expandX().fillX().pad(5).row();
            count += 1;
        }

        Table container = new Table();
        container.add(scrollPane).expand().fill();
        return container;
    }

    private void showAvailableMissionDetails(MultiMission mission, Skin skin) {
        Dialog dialog = new Dialog("Mission Details", skin);
        Table content = dialog.getContentTable();
        content.add(new Label("Required: Any " + mission.getRequest().getClass().getSimpleName() + " x" + mission.getNumberOfRequest(), skin)).padTop(20).row();
        content.add(new Label("Time: " + mission.getDeadline() + " Days", skin)).padTop(10).row();
        content.add(new Label("Required Player: " + (mission.getNumberOfRequirePlayer() - mission.getPlayers().size()), skin)).padTop(10).row();
        TextButton request = new TextButton("Request Mission", skin);
        request.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (!mission.isActive()) {
                    if (App.getInstance().getCurrentGame().getCurrentPlayer().getActiveMissions().size() <= 2) {
                        mission.addPlayer(App.getInstance().getCurrentGame().getCurrentPlayer(),
                            App.getInstance().getCurrentGame().getTimeAndDate().getTotalDays());
                        GameClient.getInstance().updateMultiMissionPlayer(mission);
                        if (!App.getInstance().getCurrentGame().getCurrentPlayer().getActiveMissions().contains(mission)) {
                            App.getInstance().getCurrentGame().getCurrentPlayer().getActiveMissions().add(mission);
                            getController().showResponse(new Response("Mission added successfully", true));
                        } else {
                            getController().showResponse(new Response("you already joined!"));
                        }
                    } else {
                        getController().showResponse(new Response("you have 3 active mission"));
                    }
                    dialog.remove();
                }
            }
        });
        TextButton back = new TextButton("Back", skin);
        back.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                dialog.remove();
            }
        });
        dialog.getButtonTable().add(request).padRight(20);
        dialog.getButtonTable().add(back);
        dialog.show(getStage());
        dialog.pack();
        dialog.setPosition(
            (MainGradle.getInstance().getCamera().viewportWidth - dialog.getWidth()) / 2f +
                MainGradle.getInstance().getCamera().position.x - MainGradle.getInstance().getCamera().viewportWidth / 2,
            (MainGradle.getInstance().getCamera().viewportHeight - dialog.getHeight()) / 2f +
                MainGradle.getInstance().getCamera().position.y - MainGradle.getInstance().getCamera().viewportHeight / 2
        );
    }

    private void showActiveMissionStatus(MultiMission mission, Skin skin) {
        Dialog dialog = new Dialog("Mission Status", skin);
        makeMissionDetails(mission, dialog.getContentTable());
        TextButton add = new TextButton("Add", skin);
        add.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                showAddItemDialog(mission, skin);
                dialog.remove();
            }
        });
        TextButton back = new TextButton("Back", skin);
        back.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                dialog.remove();
            }
        });
        dialog.getButtonTable().add(add).padRight(20);
        dialog.getButtonTable().add(back);
        dialog.show(getStage());
        dialog.pack();
        dialog.setPosition(
            (MainGradle.getInstance().getCamera().viewportWidth - dialog.getWidth()) / 2f +
                MainGradle.getInstance().getCamera().position.x - MainGradle.getInstance().getCamera().viewportWidth / 2,
            (MainGradle.getInstance().getCamera().viewportHeight - dialog.getHeight()) / 2f +
                MainGradle.getInstance().getCamera().position.y - MainGradle.getInstance().getCamera().viewportHeight / 2
        );
    }

    private void showAddItemDialog(MultiMission mission, Skin skin) {
        Dialog dialog = new Dialog("Select Amount", skin);
        dialog.setModal(true);
        dialog.setMovable(false);

        final int[] count = {1};

        Table table = new Table();
        Label countLabel = new Label(String.valueOf(count[0]), skin);

        TextButton minusBtn = new TextButton("-", skin);
        minusBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (count[0] > 1) {
                    count[0]--;
                    countLabel.setText(String.valueOf(count[0]));
                }
            }
        });

        TextButton plusBtn = new TextButton("+", skin);
        plusBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (mission.canAdd(count[0] + 1)) {
                    count[0]++;
                    countLabel.setText(String.valueOf(count[0]));
                }
            }
        });

        table.add(minusBtn).pad(10);
        table.add(countLabel).pad(10);
        table.add(plusBtn).pad(10);
        table.row();

        TextButton confirmBottom = new TextButton("Confirm", skin);
        confirmBottom.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                int selectedAmount = count[0];
                getController().showResponse(controller.addItemToMission(mission, App.getInstance().getCurrentGame().getCurrentPlayer(), selectedAmount));
                dialog.remove();
            }
        });

        TextButton backBottom = new TextButton("Back", skin);
        backBottom.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                dialog.remove();
            }
        });

        table.add(confirmBottom).colspan(3).padTop(20).padRight(20);
        table.add(backBottom);
        dialog.getContentTable().add(table).pad(20);
        dialog.show(getStage());
        dialog.pack();
        dialog.setPosition(
            (MainGradle.getInstance().getCamera().viewportWidth - dialog.getWidth()) / 2f +
                MainGradle.getInstance().getCamera().position.x - MainGradle.getInstance().getCamera().viewportWidth / 2,
            (MainGradle.getInstance().getCamera().viewportHeight - dialog.getHeight()) / 2f +
                MainGradle.getInstance().getCamera().position.y - MainGradle.getInstance().getCamera().viewportHeight / 2
        );
    }

    private void makeMissionDetails(MultiMission mission, Table table) {
        table.add(new Label("Progress: " + ((mission.getMissionProgress() / mission.getNumberOfRequest()) * 100) + "%   " +
            mission.getMissionProgress() + "/" + mission.getNumberOfRequest(), skin)).padTop(20).row();
        for (Map.Entry<Player, Integer> playerIntegerEntry : mission.getPlayers().entrySet()) {
            table.add(new Label(playerIntegerEntry.getKey().getUser().getUsername() + " Progress: " +
                ((playerIntegerEntry.getValue() / mission.getNumberOfRequest()) * 100) + "%   " +
                playerIntegerEntry.getValue() + "/" + mission.getNumberOfRequest(), skin)).padTop(10).row();
        }
        int passDays = App.getInstance().getCurrentGame().getTimeAndDate().getTotalDays() - mission.getStartedDay();
        table.add(new Label("TimeLeft: "
                + (mission.getDeadline() - passDays), skin))
            .padTop(10).row();
    }

    @Override
    protected void handleDragRelease(InputEvent event, float x, float y, int pointer, Image itemImage, Salable item, Image dragImage, Boolean flag) {
    }
}
