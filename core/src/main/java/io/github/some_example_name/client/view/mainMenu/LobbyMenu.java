package io.github.some_example_name.client.view.mainMenu;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import io.github.some_example_name.common.model.Lobby;
import io.github.some_example_name.common.model.User;
import io.github.some_example_name.common.utils.App;
import io.github.some_example_name.common.variables.Session;

public class LobbyMenu extends Menu {
    private final TextButton createLobby;
    private final TextButton listLobby;
    private Runnable updateLobbyList;

    public LobbyMenu(Skin skin) {
        super(skin);
        this.title.setText("Lobby Menu");
        this.createLobby = new TextButton("Create Lobby", skin);
        this.listLobby = new TextButton("List Lobbies", skin);
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
                createLobby();
                //TODO create lobby
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

        updateLobbyList = () -> {
            lobbyTable.clear();

            String query = searchField.getText().trim().toLowerCase();
            Integer id = null;
            try {
                id = Integer.parseInt(query);
            } catch (NumberFormatException e) {
                if (!query.isEmpty())
                    alert("You have to enter id to search", 5);
            }
            for (Lobby lobby : App.getInstance().getLobbies()) {
                if (lobby.isVisible() || (id != null && lobby.getId() == id)) {
                    String text = "Name: " + lobby.getName() + " | ID: " + lobby.getId() + " | Owner: " + lobby.getAdmin().getUsername();
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
        detailDialog.getContentTable().add("Owner: " + lobby.getAdmin().getUsername()).left().row();

        detailDialog.getContentTable().add("Members:").left().row();
        for (User member : lobby.getMembers()) {
            detailDialog.getContentTable().add("- " + member.getUsername()).left().row();
        }

        TextButton joinButton = new TextButton("Join Lobby", skin);
        joinButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                detailDialog.hide();
                joinLobby(lobby);
            }
        });

        detailDialog.getContentTable().add(joinButton).padTop(10).row();
        for (User member : lobby.getMembers()) {
            if (member.getUsername().equals(Session.getCurrentUser().getUsername())) {
                TextButton left = new TextButton("Left", skin);
                left.addListener(new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        leftLobby(lobby, member);
                        detailDialog.hide();
                    }
                });
                detailDialog.getContentTable().add(left).padTop(5).row();
            }
        }

        if (Session.getCurrentUser().getUsername().equals(lobby.getAdmin().getUsername())) {
            TextButton startGameButton = new TextButton("Start Game", skin);
            startGameButton.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    detailDialog.hide();
                    startGame(lobby);
                }
            });
            detailDialog.getContentTable().add(startGameButton).padTop(5).row();
        }

        detailDialog.button("Back", false);
        detailDialog.show(stage);
    }

    private void createLobby() {

    }

    private void joinLobby(Lobby lobby) {
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

                //TODO join lobby
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

    private void leftLobby(Lobby lobby, User member) {
        lobby.getMembers().remove(member);
        if (lobby.getAdmin().getUsername().equals(member.getUsername())) {
            if (!lobby.getMembers().isEmpty()) {
                lobby.setAdmin(lobby.getMembers().get(0));
            } else {
                App.getInstance().getLobbies().remove(lobby);
                //TODO remove lobby
            }
        }
    }

    private void startGame(Lobby lobby) {
        //TODO startGame
    }

}
