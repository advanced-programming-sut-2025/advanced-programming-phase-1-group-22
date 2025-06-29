package io.github.some_example_name.controller;

import io.github.some_example_name.view.GameView;
import lombok.Getter;

@Getter
public class GameViewController {
    private final WorldController worldController = new WorldController();
    private final PlayerController playerController = new PlayerController();
    private final ToolController toolController = new ToolController();
    private final GameView view;

    public GameViewController(GameView view) {
        this.view = view;
    }

    public void update(){
        worldController.update();
        playerController.update();
        toolController.update();
    }
}
