package io.github.some_example_name.controller;

import com.badlogic.gdx.graphics.OrthographicCamera;
import io.github.some_example_name.MainGradle;
import io.github.some_example_name.model.Farm;
import io.github.some_example_name.model.Game;
import io.github.some_example_name.model.Tile;
import io.github.some_example_name.model.records.Response;
import io.github.some_example_name.model.structure.Structure;
import io.github.some_example_name.utils.App;

public class WorldController {

    public WorldController() {
    }

    public void update() {
        printMap();
    }

    public void showResponse(Response response){

    }

    public void printMap() {
        Game game = App.getInstance().getCurrentGame();
        Tile[][] tiles = game.tiles;
        OrthographicCamera camera = MainGradle.getInstance().getCamera();

        for (int i = 0; i < 160; i++) {
            for (int j = 0; j < 120; j++) {
                if (camera.frustum.pointInFrustum(i * App.tileWidth,j * App.tileHeight,0)){
                    MainGradle.getInstance().getBatch().draw(tiles[i][j].getTileType().getTexture(),
                        i * App.tileWidth, j * App.tileHeight,App.tileWidth,App.tileHeight);
                }
            }
        }

        for (Farm farm : game.getVillage().getFarms()) {
            for (Structure structure : farm.getStructures()) {
                if (isStructureInBond(structure)){
                    if (structure.getSprite() != null){
                        structure.getSprite().setPosition(structure.getTiles().get(0).getX() * App.tileWidth,
                            structure.getTiles().get(0).getY() * App.tileHeight);
                        structure.getSprite().draw(MainGradle.getInstance().getBatch());
                    }
                }
            }
        }
//
//        for (Farm farm : farms) {
//            for (Structure structure : farm.getStructures()) {
//                String emoji = switch (structure) {
//                    case Cottage c          -> "🏡";
//                    case Lake l             -> "🌊";
//                    case Quarry q           ->  "🗿";
//                    case Craft c            -> "🔨";
//                    case FarmBuilding fb    -> "🏚️";
//                    default                 -> "  ";
//                };
//                emoji = pad2.apply(emoji);
//                if (!emoji.isBlank()) {
//                    for (Tile tile : structure.getTiles()) {
//                        str[tile.getX()][tile.getY()] = emoji;
//                    }
//                }
//            }
//        }
//
//        for (Structure structure : structures) {//                    case AnimalProduct ap   -> "🥚";

//            String emoji = switch (structure) {
//                case NPC n          -> "👴";
//                case Player p       -> "🧍";
//                case Store s        -> "🏬";
//                case NPCHouse h     -> "🏡";
//                case Fountain f     -> "🌊";
//                default             -> "  ";
//            };
//            emoji = pad2.apply(emoji);
//            if (!emoji.isBlank()) {
//                for (Tile tile : structure.getTiles()) {
//                    str[tile.getX()][tile.getY()] = emoji;
//                }
//            }
//        }

//        Player player = game.getCurrentPlayer();
//        str[player.getTiles().get(0).getX()][player.getTiles().get(0).getY()] = pad2.apply("🧍");
//
//        int xStart = Math.max(0, x - size / 2);
//        int yStart = Math.max(0, y - size / 2);
//        int xEnd   = Math.min(160, x + size / 2);
//        int yEnd   = Math.min(120, y + size / 2);
//
//        for (int i = yEnd - 1; i >= yStart; i--) {
//            for (int j = xStart; j < xEnd; j++) {
//                System.out.print(str[j][i]);
//            }
//            System.out.println();
//        }
    }

    private boolean isStructureInBond(Structure structure){
        OrthographicCamera camera = MainGradle.getInstance().getCamera();
        for (Tile tile : structure.getTiles()) {
            if (camera.frustum.pointInFrustum(tile.getX() * App.tileWidth,tile.getY() * App.tileHeight,0)) return true;
        }
        return false;
    }
}
