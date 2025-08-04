package io.github.some_example_name.client.view.mainMenu;

import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import io.github.some_example_name.client.GameClient;
import io.github.some_example_name.client.controller.mainMenu.LobbyController;
import io.github.some_example_name.client.controller.mainMenu.StartGameMenuController;
import io.github.some_example_name.common.model.Lobby;
import io.github.some_example_name.common.model.records.Response;
import io.github.some_example_name.common.utils.App;
import io.github.some_example_name.common.utils.InitialGame;
import io.github.some_example_name.common.variables.Session;
import lombok.Getter;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class LobbyMenu extends Menu {
    @Getter
    private final LobbyController controller = new LobbyController(this);
    private final TextButton createLobby;
    private final TextButton listLobby;
    private final ScrollPane usersScroll;
    private final Table users;
    private final TextButton back;
    private Runnable updateLobbyList;

    public LobbyMenu(Skin skin) {
        super(skin);
        this.title.setText("Lobby Menu");
        this.createLobby = new TextButton("Create Lobby", skin);
        this.listLobby = new TextButton("List Lobbies", skin);
        this.back = new TextButton("Back", skin);
        this.users = new Table(skin);
        this.usersScroll = new ScrollPane(users, skin);
        users.pack();
        usersScroll.setFadeScrollBars(false);
        usersScroll.setScrollbarsOnTop(true);
        usersScroll.setScrollingDisabled(true, false);
        usersScroll.setScrollBarPositions(true, true);
        usersScroll.setForceScroll(false, true);
        usersScroll.layout();
        usersScroll.setTouchable(Touchable.enabled);
        usersScroll.setHeight(200);
    }

    @Override
    protected void update(float delta) {
        synchronized (App.getInstance().getUsersUpdated()) {
            if (App.getInstance().getUsersUpdated().get()) {
                App.getInstance().getUsersUpdated().set(false);
                updateUsers();
            }
        }
    }

    @Override
    protected void showStage() {
        this.table.add(createLobby).width(400).row();
        this.createLobby.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                createLobbyDialog(skin);
            }
        });
        this.table.add(listLobby).width(400).row();
        this.listLobby.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                lobbyListDialog(skin);
            }
        });
        this.table.add(back).width(400).row();
        this.back.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                setScreen(new MainMenu(skin));
            }
        });
        this.table.add(usersScroll).width(400).row();
    }

    private void updateUsers() {
        users.clearChildren();
        synchronized (App.getInstance().getUsers()) {
            List<Map.Entry<String, List<String>>> users = App.getInstance().getUsers().entrySet()
                .stream().sorted(Map.Entry.comparingByKey()).toList();
            for (Map.Entry<String, List<String>> entry : users) {
                this.users.add(new Label(entry.getKey(), skin)).width(185).padRight(30);
                this.users.add(new Label(entry.getValue().isEmpty() ? "<LobbyLess>" : entry.getValue().get(entry.getValue().size() - 1), skin)).width(185);
                this.users.row();
            }
        }
    }

    private void createLobbyDialog(Skin skin) {
        Dialog dialog = new Dialog("Create Lobby", skin);
        dialog.pad(20);
        dialog.getContentTable().defaults().pad(10);

        dialog.getContentTable().add("Lobby Name:");
        TextField lobbyNameField = new TextField("", skin);
        dialog.getContentTable().add(lobbyNameField).width(400).row();

        CheckBox visibleCheckbox = new CheckBox("Visible to others", skin);
        visibleCheckbox.setChecked(true);
        dialog.getContentTable().add(visibleCheckbox).colspan(2).left().row();

        CheckBox privateCheckbox = new CheckBox("Private Lobby", skin);
        dialog.getContentTable().add(privateCheckbox).colspan(2).left().row();

        Label passwordLabel = new Label("Password:", skin);
        TextField passwordField = new TextField("", skin);
        passwordField.setPasswordMode(true);
        passwordField.setPasswordCharacter('*');
        passwordLabel.setVisible(false);
        passwordField.setVisible(false);
        dialog.getContentTable().add(passwordLabel);
        dialog.getContentTable().add(passwordField).width(400).row();

        privateCheckbox.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                boolean isChecked = privateCheckbox.isChecked();
                passwordLabel.setVisible(isChecked);
                passwordField.setVisible(isChecked);
            }
        });

        TextButton create = new TextButton("Create", skin);
        create.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                String lobbyName = lobbyNameField.getText();
                boolean isVisible = visibleCheckbox.isChecked();
                boolean isPrivate = privateCheckbox.isChecked();
                String password = isPrivate ? passwordField.getText() : "";
                Response response = controller.createLobby(Session.getCurrentUser(), lobbyName, isPrivate, password, isVisible);
                if (!response.shouldBeBack()) {
                    alert(response.message(), 5);
                }
                dialog.hide();
            }
        });
        TextButton cancel = new TextButton("Cancel", skin);
        cancel.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                dialog.hide();
            }
        });

        dialog.getButtonTable().add(create);
        dialog.getButtonTable().add(cancel);

        dialog.show(stage);
    }

    private void lobbyListDialog(Skin skin) {
        Dialog dialog = new Dialog("Lobby List", skin);
        dialog.pad(20);
        dialog.getContentTable().defaults().pad(10);

        TextField searchField = new TextField("", skin);
        searchField.setMessageText("Search hidden lobbies...");
        dialog.getContentTable().add(searchField).fillX().colspan(2).row();

        Table lobbyTable = new Table(skin);
        lobbyTable.top().left();

        TextButton refresh = new TextButton("refresh", skin);
        refresh.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                updateLobbyList.run();
            }
        });
        lobbyTable.add(refresh).left().padBottom(5).row();

        updateLobbyList = () -> {
            lobbyTable.clear();
            lobbyTable.add(refresh).left().padBottom(5).row();

            String query = searchField.getText().trim().toLowerCase();
            Long id = null;
            try {
                id = Long.parseLong(query);
            } catch (NumberFormatException e) {
                if (!query.isEmpty())
                    alert("You have to enter id to search", 5);
            }
            synchronized (App.getInstance().getLobbies()) {
                for (Lobby lobby : App.getInstance().getLobbies()) {
                    if (lobby.isVisible() || (id != null && lobby.getId() == id)
                        || lobby.getMembers().contains(Session.getCurrentUser().getUsername())) {
                        String text = "Name: " + lobby.getName() + " | ID: " + lobby.getId() + " | Owner: " + lobby.getAdmin();
                        TextButton lobbyButton = new TextButton(text, skin);
                        lobbyTable.add(lobbyButton).left().padBottom(5).row();

                        lobbyButton.addListener(new ClickListener() {
                            @Override
                            public void clicked(InputEvent event, float x, float y) {
                                showLobbyDetailDialog(lobby, skin);
                            }
                        });
                    }
                }
            }

            if (lobbyTable.getChildren().size == 0) {
                lobbyTable.add("No lobbies found.").left().row();
            }
        };

        updateLobbyList.run();

        ScrollPane scrollPane = new ScrollPane(lobbyTable, skin);
        scrollPane.setFadeScrollBars(false);
        scrollPane.setScrollingDisabled(true, false);
        scrollPane.setScrollbarsOnTop(true);

        dialog.getContentTable().add(scrollPane).width(500).height(300).colspan(2).row();

        searchField.setTextFieldListener((textField, c) -> updateLobbyList.run());

        dialog.button("Close", false);
        dialog.show(stage);
    }

    private void showLobbyDetailDialog(Lobby lobby, Skin skin) {
        Dialog detailDialog = new Dialog("Lobby Details", skin);
        detailDialog.pad(20);
        detailDialog.getContentTable().defaults().pad(10);

        detailDialog.getContentTable().add("Lobby Name: " + lobby.getName()).left().row();
        detailDialog.getContentTable().add("Lobby ID: " + lobby.getId()).left().row();
        detailDialog.getContentTable().add("Owner: " + lobby.getAdmin()).left().row();

        detailDialog.getContentTable().add("Members:").left().row();
        for (String member : lobby.getMembers()) {
            detailDialog.getContentTable().add("- " + member).left().row();
        }

        TextButton joinButton = new TextButton("Join Lobby", skin);
        joinButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                detailDialog.hide();
                joinLobby(lobby);
            }
        });

        detailDialog.getContentTable().add(joinButton).width(400).padTop(10).row();
        for (String member : lobby.getMembers()) {
            if (member.equals(Session.getCurrentUser().getUsername())) {
                TextButton left = new TextButton("Left", skin);
                left.addListener(new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        leftLobby(lobby, member);
                        detailDialog.hide();
                    }
                });
                detailDialog.getContentTable().add(left).width(400).padTop(5).row();
                TextButton reConnectToGame = new TextButton("Reconnect Game", skin);
                reConnectToGame.addListener(new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        reconnect(lobby);
                        detailDialog.hide();
                    }
                });
                detailDialog.getContentTable().add(reConnectToGame).width(400).padTop(5).row();
                TextButton load = new TextButton("load Game", skin);
                load.addListener(new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        loadGame(lobby);
                        detailDialog.hide();
                    }
                });
                detailDialog.getContentTable().add(load).width(400).padTop(5).row();
            }
        }

        if (Session.getCurrentUser().getUsername().equals(lobby.getAdmin())) {
            TextButton startGameButton = new TextButton("Start Game", skin);
            startGameButton.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    detailDialog.hide();
                    startGame(lobby);
                }
            });
            detailDialog.getContentTable().add(startGameButton).width(400).padTop(5).row();
        }

        detailDialog.button("Back", false);
        detailDialog.show(stage);
    }

    private void joinLobby(Lobby lobby) {
        if (lobby.is_public()) {
            if (lobby.getMembers().size() <= 3 && !lobby.getMembers().contains(Session.getCurrentUser().getUsername())) {
                lobby.getMembers().add(Session.getCurrentUser().getUsername());
                GameClient.getInstance().joinLobbyMessage(lobby.getId());
            } else {
                alert("lobby is full or you already joined", 5);
            }
        } else {
            Dialog passDialog = new Dialog("Enter Password", skin);
            TextField passField = new TextField("", skin);
            passField.setPasswordMode(true);
            passField.setPasswordCharacter('*');
            passDialog.getContentTable().add("Password: ");
            passDialog.getContentTable().add(passField).row();

            TextButton join = new TextButton("Join", skin);
            join.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    String password = passField.getText();
                    Response response = controller.joinLobby(lobby, Session.getCurrentUser().getUsername(), password);
                    if (!response.shouldBeBack()) {
                        alert(response.message(), 5);
                    }
                    passDialog.hide();
                }
            });
            TextButton cancel = new TextButton("Cancel", skin);
            cancel.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    passDialog.hide();
                }
            });

            passDialog.getButtonTable().add(join);
            passDialog.getButtonTable().add(cancel);

            passDialog.show(stage);
        }
    }

    private void leftLobby(Lobby lobby, String member) {
        lobby.getMembers().remove(member);
        if (lobby.getAdmin().equals(member)) {
            if (!lobby.getMembers().isEmpty()) {
                lobby.setAdmin(lobby.getMembers().get(0));
            } else {
                App.getInstance().getLobbies().remove(lobby);
            }
        }
        GameClient.getInstance().leftLobbyMessage(lobby.getId());
    }

    private void reconnect(Lobby lobby) {
        if (lobby.getGameServer() != null && lobby.isGameStart()) {
            App.getInstance().setCurrentLobby(lobby);
            InitialGame initialGame = new InitialGame();
            initialGame.initial();
            StartGameMenuController.getInstance().setReconnect(true);
            GameClient.getInstance().DCReconnect(Session.getCurrentUser().getUsername());
        } else {
            alert("there is no alive game!", 5);
        }
    }

    private void loadGame(Lobby lobby) {
        if (lobby.getGameServer() != null && lobby.isGameServerSaved()) {
            if (!canStartGame(lobby)) {
                alert("all of players should be online", 5);
            } else {
                App.getInstance().setCurrentLobby(lobby);
                GameClient.getInstance().readyForLoadGame(lobby);
                setScreen(new StartGameMenu(skin, 3));
            }
        } else {
            alert("there is no saved game!", 5);
        }
    }

    private void startGame(Lobby lobby) {
        if (lobby.getMembers().size() >= 2) {
            if (!canStartGame(lobby)) {
                alert("all of players should be online", 5);
            } else {
                lobby.setGameStart(true);
                App.getInstance().setCurrentLobby(lobby);
                InitialGame initialGame = new InitialGame();
                initialGame.initial();
                GameClient.getInstance().startGame(lobby.getId());
                setScreen(new StartGameMenu(skin, 1));
            }
        } else {
            alert("need at least one more player", 5);
        }
    }

    private boolean canStartGame(Lobby lobby) {
        for (String member : lobby.getMembers()) {
            if (!App.getInstance().getUsers().containsKey(member)) {
                return false;
            }
        }
        return true;
    }
}
