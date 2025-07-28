package io.github.some_example_name.common.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.some_example_name.client.GameClient;
import io.github.some_example_name.common.model.products.HarvestAbleProduct;
import io.github.some_example_name.common.model.shelter.ShippingBin;
import io.github.some_example_name.common.model.source.*;
import io.github.some_example_name.common.model.structure.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import io.github.some_example_name.common.model.products.TreesAndFruitsAndSeeds.Tree;
import io.github.some_example_name.common.model.products.TreesAndFruitsAndSeeds.TreeType;
import io.github.some_example_name.common.model.relations.Player;
import io.github.some_example_name.common.model.structure.farmInitialElements.Cottage;
import io.github.some_example_name.common.model.structure.farmInitialElements.GreenHouse;
import io.github.some_example_name.common.model.structure.farmInitialElements.HardCodeFarmElements;
import io.github.some_example_name.common.model.structure.farmInitialElements.Lake;
import io.github.some_example_name.server.saveGame.JsonPreparable;
import io.github.some_example_name.server.saveGame.ObjectWrapper;
import io.github.some_example_name.common.utils.App;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.function.Consumer;

@Getter
@Setter
@NoArgsConstructor
@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class, property = "@id")
public class Farm implements JsonPreparable {
    //    @JsonManagedReference
    private List<Tile> tiles = new ArrayList<>();
    //    @JsonManagedReference
    private List<Player> players = new ArrayList<>();
    //    @JsonManagedReference
    private List<Structure> structures = new ArrayList<>();
    private final Queue<Runnable> pendingStructureChanges = new ConcurrentLinkedQueue<>();
    private Integer xCenter;
    private Integer yCenter;
    //    @JsonManagedReference
    private FarmType farmType;
    private Integer farmXStart;
    private Integer farmYStart;
    private Integer farmXEnd;
    private Integer farmYEnd;
    private Boolean crowAttackToday = false;
    private Integer farmIndex = 0;
    //    @JsonManagedReference
    private Fridge fridge = new Fridge();
    private HarvestAbleProduct attackedProduct;


    @JsonProperty("structureWrappers")
    private List<ObjectWrapper> structureWrappers;

    @Override
    public void prepareForSave(ObjectMapper mapper) {
        structureWrappers = new ArrayList<>();
        for (Structure s : structures) {
            structureWrappers.add(new ObjectWrapper(s, mapper));
        }

        if (fridge != null && fridge instanceof JsonPreparable preparableFridge) {
            preparableFridge.prepareForSave(mapper);
        }

    }

    @Override
    public void unpackAfterLoad(ObjectMapper mapper) {
        structures = new ArrayList<>();
        for (ObjectWrapper wrapper : structureWrappers) {
            structures.add((Structure) wrapper.toObject(mapper));
        }

        if (fridge != null && fridge instanceof JsonPreparable preparableFridge) {
            preparableFridge.unpackAfterLoad(mapper);
        }
    }

    public void forEachStructure(Consumer<Structure> action) {
        List<Structure> copy;
        synchronized (this.getStructures()) {
            copy = new ArrayList<>(this.getStructures());
        }
        for (Structure s : copy) {
            action.accept(s);
        }
    }

    public void applyPendingChanges() {
        while (!pendingStructureChanges.isEmpty()) {
            pendingStructureChanges.poll().run();
        }
    }

    public void addStructure(Structure s) {
        pendingStructureChanges.add(() -> {
            synchronized (this.getStructures()) {
                this.getStructures().add(s);
            }
        });
    }

    public void removeStructure(Structure s) {
        pendingStructureChanges.add(() -> {
            synchronized (this.getStructures()) {
                this.getStructures().remove(s);
            }
        });
    }

    public List<Structure> getStructuresSnapshot() {
        synchronized (this.getStructures()) {
            return new ArrayList<>(this.getStructures());
        }
    }

    public Farm(Player player, FarmType farmType) {
        if (null != player) {
            this.players.add(player);
        }
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
        getcopy();
        setFence();
    }

    public void setFarmTiles() {
        for (int i = farmXStart; i < farmXEnd; i++) {
            tiles.addAll(Arrays.asList(App.getInstance().getCurrentGame().tiles[i]).subList(farmYStart, farmYEnd));
        }
    }

    public void getcopy() {
        farmType.initial();
        for (HardCodeFarmElements structure : farmType.getStructures()) {
            HardCodeFarmElements copyEl = structure.copyEl();
            transmission(copyEl);
            structures.add(copyEl);
        }
    }

    public void transmission(HardCodeFarmElements farmElements) {
        for (Pair pair : farmElements.getTilePairList()) {
            Tile tile = App.getInstance().getCurrentGame().tiles[pair.getX() + farmXStart][pair.getY() + farmYStart];
            tile.setIsFilled(true);
            if (farmElements instanceof Lake || farmElements instanceof ShippingBin) {
                tile.setIsPassable(false);
            }
            farmElements.getTiles().add(tile);
        }
    }

    public void setFence() {
        for (int i = farmXStart; i <= farmXEnd; i++) {
            App.getInstance().getCurrentGame().getTiles()[i][farmYStart].setTileType(TileType.FENCE);
            App.getInstance().getCurrentGame().getTiles()[i][farmYStart].setIsFilled(true);
            App.getInstance().getCurrentGame().getTiles()[i][farmYEnd].setTileType(TileType.FENCE);
            App.getInstance().getCurrentGame().getTiles()[i][farmYEnd].setIsFilled(true);
        }
        for (int i = farmYStart; i <= farmYEnd; i++) {
            App.getInstance().getCurrentGame().getTiles()[farmXStart][i].setTileType(TileType.FENCE);
            App.getInstance().getCurrentGame().getTiles()[farmXStart][i].setIsFilled(true);
            App.getInstance().getCurrentGame().getTiles()[farmXEnd][i].setTileType(TileType.FENCE);
            App.getInstance().getCurrentGame().getTiles()[farmXEnd][i].setIsFilled(true);
        }
    }

    public void setDoor(int xRand, int yRand) {
        xRand = farmXStart + 1 + xRand % (farmXEnd - farmXStart - 2);
        yRand = farmYStart + 1 + yRand % (farmYEnd - farmYStart - 2);
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
        App.getInstance().getCurrentGame().getTiles()[xRand][yConst].setTileType(TileType.DOOR);
        App.getInstance().getCurrentGame().getTiles()[xConst][yRand].setTileType(TileType.DOOR);
    }

    public void generateRandomStructures() {
        Random random = new Random();
        int trunkRand = random.nextInt(10, 12);
        int stoneRand = random.nextInt(12, 15);
        int foragingRand = random.nextInt(14, 18);
        int foragingRand2 = random.nextInt(12, 16);
        int foragingRand3 = random.nextInt(10, 12);
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
            setStructurePlace(trunk, trunk.getTrunkType().getLength(), trunk.getTrunkType().getWidth(), true, false);
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
            setStructurePlace(stone, stone.getStoneType().getLength(), stone.getStoneType().getWidth(), true, false);
        }

        for (int i = 0; i < foragingRand; i++) {
            int foragingRandSeed = random.nextInt(0, 41);
            Seed seed = new Seed(SeedType.values()[foragingRandSeed]);
            seed.setIsPickable(true);
            setStructurePlace(seed, 1, 1, true, true);
            int cropRand = random.nextInt(41, 62);
            Crop crop = new Crop(CropType.values()[cropRand]);
            setStructurePlace(crop, 1, 1, true, true);
        }
        for (int i = 0; i < foragingRand2; i++) {
            int foragingRandTree = random.nextInt(0, 4);
            Tree tree = new Tree(TreeType.values()[foragingRandTree]);
            setStructurePlace(tree, 1, 1, true, false);
        }
        for (int i = 0; i < foragingRand3; i++) {
            int mineralRand = random.nextInt(0, 21);
            Mineral mineral = new Mineral(MineralType.values()[mineralRand]);
            setStructurePlace(mineral, 1, 1, true, false);
        }
    }

    public void setStructurePlace(Structure structure, int length, int width, boolean isFilled, boolean isPassible) {
        Tile[][] tiles1 = App.getInstance().getCurrentGame().tiles;
        List<Tile> tiles2 = new ArrayList<>();
        boolean flag = true;
        Random random = new Random();
        int randX = -1;
        int randY = -1;
        for (int i = 0; i < 30; i++) {
            flag = true;
            randX = random.nextInt(farmXStart, farmXEnd);
            randY = random.nextInt(farmYStart, farmYEnd);
            for (int j = randX; flag && j < randX + length; j++) {
                for (int k = randY; flag && k < randY + width; k++) {
                    if (tiles1[j][k].getIsFilled()) {
                        flag = false;
                    } else {
                        tiles2.add(tiles1[j][k]);
                    }
                }
            }
            if (flag && !tiles2.isEmpty()) {
                if (!isPassible) {
                    for (Tile tile : tiles2) {
                        tile.setIsPassable(false);
                        GameClient.getInstance().updateTileState(tile);
                    }
                }
                if (isFilled) {
                    for (Tile tile : tiles2) {
                        tile.setIsFilled(true);
                        GameClient.getInstance().updateTileState(tile);
                    }
                }
                structure.getTiles().addAll(tiles2);
                addStructure(structure);
                GameClient.getInstance().updateStructureState(structure, StructureUpdateState.ADD, true, null);
                return;
            }
            tiles2.clear();
        }
    }

    public void generateRandomForaging() {
        Random random = new Random();
        int foragingRand = random.nextInt(14, 18);
        int foragingRand2 = random.nextInt(12, 16);
        int foragingRand3 = random.nextInt(10, 12);
        for (int i = 0; i < foragingRand; i++) {
            int foragingRandSeed = random.nextInt(0, 41);
            Seed seed = new Seed(SeedType.values()[foragingRandSeed]);
            if (seed.getSeedType().getSeason().equals(App.getInstance().getCurrentGame().getTimeAndDate().getSeason())) {
                seed.setIsPickable(true);
                setForagingPlace(seed, 1, 1);
            }
        }
        for (int i = 0; i < foragingRand2; i++) {
            int cropRand = random.nextInt(41, 62);
            Crop crop = new Crop(CropType.values()[cropRand]);
            if (crop.getCropType().getSeasons().contains(App.getInstance().getCurrentGame().getTimeAndDate().getSeason())) {
                crop.setIsPickable(true);
                setForagingPlace(crop, 1, 1);
            }
        }
        for (int i = 0; i < foragingRand3; i++) {
            int mineralRand = random.nextInt(0, 21);
            Mineral mineral = new Mineral(MineralType.values()[mineralRand]);
            setStructurePlace(mineral, 1, 1, true, false);
        }
    }

    private void setForagingPlace(Structure structure, int length, int width) {
        Tile[][] tiles1 = App.getInstance().getCurrentGame().tiles;
        List<Tile> tiles2 = new ArrayList<>();
        boolean flag = true;
        Random random = new Random();
        int randX = -1;
        int randY = -1;
        for (int i = 0; i < this.tiles.size(); i++) {
            flag = true;
            randX = random.nextInt(farmXStart, farmXEnd);
            randY = random.nextInt(farmYStart, farmYEnd);
            for (int j = randX; flag && j < randX + length; j++) {
                for (int k = randY; flag && k < randY + width; k++) {
                    if (tiles1[j][k].getIsFilled() ||
                        !tiles1[j][k].getTileType().equals(TileType.PLOWED)) {
                        flag = false;
                    } else {
                        int rand = random.nextInt(1, 100);
                        if (rand == 1) {
                            tiles1[j][k].setIsFilled(true);
                            tiles2.add(tiles1[j][k]);
                        }
                    }
                }
            }
            if (flag && !tiles2.isEmpty()) {
                structure.getTiles().addAll(tiles2);
                addStructure(structure);
                GameClient.getInstance().updateStructureState(structure, StructureUpdateState.ADD, true, null);
                return;
            }
            tiles2.clear();
        }
    }

    public Cottage getCottage() {
        for (Structure structure : structures) {
            if (structure instanceof Cottage) return (Cottage) structure;
        }
        return null;
    }

    public GreenHouse getGreenHouse() {
        for (Structure structure : structures) {
            if (structure instanceof GreenHouse) return (GreenHouse) structure;
        }
        return null;
    }

    public boolean isPairInFarm(Pair pair) {
        return Math.abs(xCenter - pair.getX()) <= (farmType.getLength() / 2) &&
            Math.abs(yCenter - pair.getY()) <= (farmType.getWidth() / 2);
    }
}

















