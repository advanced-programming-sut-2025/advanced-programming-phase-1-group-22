package model;

import lombok.Getter;
import lombok.Setter;
import model.products.TreesAndFruitsAndSeeds.Tree;
import model.products.TreesAndFruitsAndSeeds.TreeType;
import model.source.*;
import model.structure.*;
import model.structure.farmInitialElements.HardCodeFarmElements;
import utils.App;

import javax.swing.plaf.synth.SynthTreeUI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

@Getter
@Setter
public class Farm {

    private List<Tile> tiles = new ArrayList<>();
    private List<Player> players = new ArrayList<>();
    private List<Structure> structures = new ArrayList<>();
    private Integer xCenter;
    private Integer yCenter;
    private FarmType farmType;
    private App app = App.getInstance();
    private Integer farmXStart;
    private Integer farmYStart;
    private Integer farmXEnd;
    private Integer farmYEnd;
    private Integer farmIndex = 0;

    public Farm(Player player, FarmType farmType) {
        this.players.add(player);
        this.farmType = farmType;
    }

    public void fillFarmType(int i) {
        farmIndex = i;
        switch (i) {
            case 0 -> {
                xCenter = 30;
                yCenter = 23;
            }
            case 1 -> {
                xCenter = 129;
                yCenter = 23;
            }
            case 2 -> {
                yCenter = 96;
                xCenter = 129;
            }
            case 3 -> {
                xCenter = 30;
                yCenter = 96;
            }
        }
        farmXStart = xCenter - farmType.getLength() / 2;
        farmYStart = yCenter - farmType.getWidth() / 2;
        farmXEnd = xCenter + farmType.getLength() / 2;
        farmYEnd = yCenter + farmType.getWidth() / 2;
        setFarmTiles();
        getClone();
        setFence();
        setDoor();
    }

    public void setFarmTiles() {
        for (int i = farmXStart; i < farmXEnd; i++) {
            tiles.addAll(Arrays.asList(app.getCurrentGame().tiles[i]).subList(farmYStart, farmYEnd));
        }
    }

    public void getClone() {
        for (HardCodeFarmElements structure : farmType.getStructures()) {
            HardCodeFarmElements cloneEl = structure.cloneEl();
            transmission(cloneEl);
            structures.add(cloneEl);
        }
    }

    public void transmission(HardCodeFarmElements farmElements) {
        for (Pair pair : farmElements.getTilePairList()) {
            Tile tile = app.getCurrentGame().tiles[pair.getX() + farmXStart][pair.getY() + farmYStart];
            tile.setIsFilled(true);
            farmElements.getTiles().add(tile);
        }
    }

    public void setFence() {
        for (int i = farmXStart; i < farmXEnd; i++) {
            app.getCurrentGame().getTiles()[i][farmYStart].setTileType(TileType.FENCE);
            app.getCurrentGame().getTiles()[i][farmYStart].setIsFilled(true);
            app.getCurrentGame().getTiles()[i][farmYEnd].setTileType(TileType.FENCE);
            app.getCurrentGame().getTiles()[i][farmYEnd].setIsFilled(true);
        }
        for (int i = farmYStart; i < farmYEnd; i++) {
            app.getCurrentGame().getTiles()[farmXStart][i].setTileType(TileType.FENCE);
            app.getCurrentGame().getTiles()[farmXStart][i].setIsFilled(true);
            app.getCurrentGame().getTiles()[farmXEnd][i].setTileType(TileType.FENCE);
            app.getCurrentGame().getTiles()[farmXEnd][i].setIsFilled(true);
        }
    }

    public void setDoor() {
        Random random = new Random();
        int yRand = random.nextInt(farmYStart, farmYEnd);
        int xRand = random.nextInt(farmXStart, farmXEnd);
        int xConst = 0;
        int yConst = 0;
        switch (farmIndex) {
            case 0 -> {
                xConst = farmXEnd;
                yConst = farmYEnd;
            }
            case 1 -> {
                xConst = farmXStart;
                yConst = farmYEnd;
            }
            case 2 -> {
                xConst = farmXStart;
                yConst = farmYStart;
            }
            case 3 -> {
                xConst = farmXEnd;
                yConst = farmYStart;
            }
        }
        app.getCurrentGame().getTiles()[xRand][yConst].setTileType(TileType.DOOR);
        app.getCurrentGame().getTiles()[xConst][yRand].setTileType(TileType.DOOR);
    }

    public void generateRandomStructures() {
        Random random = new Random();
        int trunkRand = random.nextInt(10, 12);
        int stoneRand = random.nextInt(12, 15);
        int foragingRand = random.nextInt(14, 18);
        int foragingRand2 = random.nextInt(12, 16);
        int foragingRand3 = random.nextInt(10,12);
        for (int i = 0; i < trunkRand; i++) {
            Trunk trunk = null;
            switch (trunkRand % 3) {
                case 0 -> {
                    trunk = new Trunk(TrunkType.SMALL_TRUNK);
                }
                case 1 -> {
                    trunk = new Trunk(TrunkType.NORMAL_TRUNK);
                }
                case 2 -> {
                    trunk = new Trunk(TrunkType.LARGE_TRUNK);
                }
            }
            setStructurePlace(trunk, trunk.getTrunkType().getLength(), trunk.getTrunkType().getWidth());
        }
        for (int i = 0; i < stoneRand; i++) {
            Stone stone = null;
            switch (stoneRand % 3) {
                case 0 -> {
                    stone = new Stone(StoneType.SMALL_STONE);
                }
                case 1 -> {
                    stone = new Stone(StoneType.NORMAL_STONE);
                }
                case 2 -> {
                    stone = new Stone(StoneType.LARGE_STONE);

                }
            }
            setStructurePlace(stone, stone.getStoneType().getLength(), stone.getStoneType().getWidth());
        }

        for (int i = 0; i < foragingRand; i++) {
            int foragingRandSeed = random.nextInt(0, 41);
            Seed seed = new Seed(SeedType.values()[foragingRandSeed]);
            seed.setIsPickable(true);
            setStructurePlace(seed, 1, 1);
            int cropRand = random.nextInt(41, 62);
            Crop crop = new Crop(CropType.values()[cropRand]);
            crop.setIsPickable(true);
            setStructurePlace(crop, 1, 1);
        }
        for (int i = 0; i < foragingRand2; i++) {
            int foragingRandTree = random.nextInt(0, 5);
            Tree tree = new Tree(TreeType.values()[foragingRandTree]);
            setStructurePlace(tree, 1, 1);
        }
        for (int i = 0; i < foragingRand3; i++) {
            int mineralRand = random.nextInt(0,21);
            Mineral mineral = new Mineral(MineralType.values()[mineralRand]);
            mineral.setIsPickable(true);
            setStructurePlace(mineral,1,1);
        }
    }

    public void setStructurePlace(Structure structure, int length, int width) {
        Tile[][] tiles1 = app.getCurrentGame().tiles;
        List<Tile> tiles2 = new ArrayList<>();
        boolean flag = true;
        Random random = new Random();
        int randX = -1;
        int randY = -1;
        for (int i = 0; i < 30; i++) {
            randX = random.nextInt(farmXStart, farmXEnd);
            randY = random.nextInt(farmYStart, farmYEnd);
            for (int j = randX; j < randX + length; j++) {
                for (int k = randY; k < randY + width; k++) {
                    if (tiles1[j][k].getIsFilled()) {
                        flag = false;
                    } else {
                        tiles1[j][k].setIsFilled(true);
                        tiles2.add(tiles1[j][k]);
                    }
                }
            }
            if (flag) {
                structure.getTiles().addAll(tiles2);
                this.getStructures().add(structure);
                return;
            }
            tiles2.clear();
        }
    }

    public void generateRandomForaging(){
        Random random = new Random();
        int foragingRand = random.nextInt(14, 18);
        int foragingRand2 = random.nextInt(12, 16);
        int foragingRand3 = random.nextInt(10,12);
        for (int i = 0; i < foragingRand; i++) {
            int foragingRandSeed = random.nextInt(0, 41);
            Seed seed = new Seed(SeedType.values()[foragingRandSeed]);
            if (seed.getSeedType().getSeason().equals(App.getInstance().getCurrentGame().getTimeAndDate().getSeason())){
                seed.setIsPickable(true);
                setForagingPlace(seed, 1, 1);
            }
        }
        for (int i = 0; i < foragingRand2; i++){
            int cropRand = random.nextInt(41, 62);
            Crop crop = new Crop(CropType.values()[cropRand]);
            if (crop.getCropType().getSeasons().contains(App.getInstance().getCurrentGame().getTimeAndDate().getSeason())){
                crop.setIsPickable(true);
                setForagingPlace(crop, 1, 1);
            }
        }
        for (int i = 0; i < foragingRand3; i++) {
            int mineralRand = random.nextInt(0,21);
            Mineral mineral = new Mineral(MineralType.values()[mineralRand]);
            mineral.setIsPickable(true);
            setStructurePlace(mineral,1,1);
        }
    }

    private void setForagingPlace(Structure structure,int length, int width){
        Tile[][] tiles1 = app.getCurrentGame().tiles;
        List<Tile> tiles2 = new ArrayList<>();
        boolean flag = true;
        Random random = new Random();
        int randX = -1;
        int randY = -1;
        for (int i = 0; i < this.tiles.size(); i++) {
            randX = random.nextInt(farmXStart, farmXEnd);
            randY = random.nextInt(farmYStart, farmYEnd);
            for (int j = randX; j < randX + length; j++) {
                for (int k = randY; k < randY + width; k++) {
                    if (tiles1[j][k].getIsFilled() ||
                            !tiles1[j][k].getTileType().equals(TileType.PLOWED)) {
                        flag = false;
                    } else {
                        int rand = random.nextInt(1,100);
                        if (rand == 1){
                            tiles1[j][k].setIsFilled(true);
                            tiles2.add(tiles1[j][k]);
                        }
                    }
                }
            }
            if (flag) {
                structure.getTiles().addAll(tiles2);
                this.getStructures().add(structure);
                return;
            }
            tiles2.clear();
        }
    }
}

















