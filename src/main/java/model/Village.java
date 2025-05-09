package model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import model.animal.Animal;
import model.animal.Fish;
import model.craft.Craft;
import model.enums.Weather;
import model.products.AnimalProduct;
import model.products.TreesAndFruitsAndSeeds.Tree;
import model.relations.NPC;
import model.relations.NPCType;
import model.relations.Player;
import model.shelter.FarmBuilding;
import model.shelter.ShippingBin;
import model.source.*;
import model.structure.NPCHouse;
import model.structure.Stone;
import model.structure.Structure;
import model.structure.Trunk;
import model.structure.farmInitialElements.Cottage;
import model.structure.farmInitialElements.GreenHouse;
import model.structure.farmInitialElements.Lake;
import model.structure.farmInitialElements.Quarry;
import model.structure.stores.Store;
import model.structure.stores.StoreType;
import model.tools.Tool;
import utils.App;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Getter
@Setter
@ToString
public class Village implements Serializable {
    private List<Tile> tiles;
    private List<Farm> farms = new ArrayList<>();
    private List<Structure> structures = new ArrayList<>();
    private Weather weather = Weather.SUNNY;
    private Weather tomorrowWeather = Weather.SUNNY;
    @JsonIgnore
    private App app = App.getInstance();

    public Village() {
        fillStructures();
    }

    public void shuffleFarms() {
        Random random = new Random();
        for (int i = 3; i > 0; i--) {
             int j = random.nextInt(i + 1);
            Farm temp = farms.get(i);
            farms.set(i, farms.get(j));
            farms.set(j, temp);
        }
    }

    public void fillFarms() {
       // shuffleFarms();
        for (int i = 0; i < farms.size(); i++) {
            Farm farm = farms.get(i);
            farm.fillFarmType(i);
            farm.generateRandomStructures();
        }
    }

    public void fillStructures() {
        StoreType fishShop = StoreType.FISH_SHOP;
        int randomNumber = getRandomNumber(1, 10);
        addStores(fishShop, 63, randomNumber, true);

        StoreType blackSmith = StoreType.BLACK_SMITH;
        randomNumber = getRandomNumber(71, 88);
        addStores(blackSmith, 63, randomNumber, true);

        StoreType carpenterShop = StoreType.CARPENTER_SHOP;
        randomNumber = getRandomNumber(90, 106);
        addStores(carpenterShop, 88, randomNumber, true);

        StoreType jojaMart = StoreType.JOJA_MART;
        randomNumber = getRandomNumber(27, 37);
        addStores(jojaMart, 88, randomNumber, true);

        StoreType stardropsalon = StoreType.STARDROPSALON;
        randomNumber = getRandomNumber(1, 10);
        addStores(stardropsalon, randomNumber, 49, false);

        StoreType pierreShop = StoreType.PIERRE_SHOP;
        randomNumber = getRandomNumber(130, 140);
        addStores(pierreShop, randomNumber, 49, false);

        StoreType marnieShop = StoreType.MARNIE_SHOP;
        randomNumber = getRandomNumber(1, 9);
        addStores(marnieShop, randomNumber, 63, false);

        NPCType lia = NPCType.LIA;
        randomNumber = getRandomNumber(29, 37);
        addNPC(lia, randomNumber, 66, false);


        NPCType ebigil = NPCType.EBIGIL;
        randomNumber = getRandomNumber(100, 112);
        addNPC(ebigil, randomNumber, 66, false);

        NPCType harvey = NPCType.HARVEY;
        randomNumber = getRandomNumber(132, 145);
        addNPC(harvey, randomNumber, 66, false);

        NPCType rabin = NPCType.RABIN;
        randomNumber = getRandomNumber(30, 40);
        addNPC(rabin, randomNumber, 49, false);

        NPCType sebastian = NPCType.SEBASTIAN;
        randomNumber = getRandomNumber(100, 110);
        addNPC(sebastian, randomNumber, 49, false);


        setPath();

        Fountain fountain = new Fountain();
        for (int i = 57; i < 63; i++) {
            for (int j = 77; j < 83; j++) {
                fountain.getTiles().add(app.getCurrentGame().tiles[j][i]);
                app.getCurrentGame().tiles[j][i].setIsFilled(true);
            }
        }
        structures.add(fountain);

    }

    private void setPath() {
        for (int i = 57; i < 63; i++) {
            for (int j = 0; j < 160; j++) {
                Tile tile = app.getCurrentGame().tiles[j][i];
                tile.setTileType(TileType.PATH);
            }
        }
        for (int i = 0; i < 120; i++) {
            for (int j = 77; j < 83; j++) {
                Tile tile = app.getCurrentGame().tiles[j][i];
                tile.setTileType(TileType.PATH);
            }
        }
    }

    public void addStores(StoreType storeType, int xStart, int yStart, boolean vertical) {
        Store store = new Store(storeType);
        if (vertical) {
            setTileOfStore(store, xStart, yStart, xStart + 8, yStart + 12);
        } else {
            setTileOfStore(store, xStart, yStart, xStart + 12, yStart + 8);
        }
        structures.add(store);
    }

    public void addNPC(NPCType NPCType, int xStart, int yStart, boolean vertical) {
//        NPCType.setMissions();
        NPC npc = new NPC(NPCType);
        NPCHouse npcHouse = new NPCHouse(npc);
        if (vertical) {
            setTileOfNPCHouse(npcHouse, xStart, yStart, xStart + 4, yStart + 6);
        } else {
            setTileOfNPCHouse(npcHouse, xStart, yStart, xStart + 6, yStart + 4);
        }
        App.getInstance().getCurrentGame().getNpcs().add(npc);
        Tile tileByXAndY = getTileByXAndY(npcHouse.getTiles().getFirst().getX(), npcHouse.getTiles().getFirst().getY() - 1);
        tileByXAndY.setIsFilled(true);
        tileByXAndY.setIsPassable(false);
        npc.getTiles().add(tileByXAndY);
        structures.add(npcHouse);
        structures.add(npc);
    }


    private Tile getTileByXAndY(int x, int y) {
        for (Tile[] tile : App.getInstance().getCurrentGame().tiles) {
            for (Tile tile1 : tile) {
                if (tile1.getX() == x && tile1.getY() == y) {
                    return tile1;
                }
            }
        }
        return null;
    }

    public int getRandomNumber(int start, int end) {
        Random random = new Random();
        return random.nextInt(end - start + 1) + start;
    }

    public void setTileOfStore(Store store, int xStart, int yStart, int xEnd, int yEnd) {
        for (int i = xStart; i < xEnd; i++) {
            for (int j = yStart; j < yEnd; j++) {
                store.getTiles().add(app.getCurrentGame().tiles[i][j]);
                app.getCurrentGame().tiles[i][j].setIsFilled(true);
            }
        }
    }

    public void setTileOfNPCHouse(NPCHouse npcHouse, int xStart, int yStart, int xEnd, int yEnd) {
        for (int i = xStart; i < xEnd; i++) {
            for (int j = yStart; j < yEnd; j++) {
                npcHouse.getTiles().add(app.getCurrentGame().tiles[i][j]);
                app.getCurrentGame().tiles[i][j].setIsFilled(true);
                app.getCurrentGame().tiles[i][j].setIsPassable(false);
            }
        }
    }

    public ArrayList<Structure> findStructuresByTile(Tile tile) {
        ArrayList<Structure> structures = new ArrayList<>();
        for (Structure structure : this.structures) {
            if (structure.getTiles().contains(tile)) structures.add(structure);
        }
        for (Farm farm : farms) {
            for (Structure structure : farm.getStructures()) {
                if (structure.getTiles().contains(tile)) structures.add(structure);
            }
        }
        return structures;
    }

    public void printMap(int x, int y, int size) {
        Game game = app.getCurrentGame();
        Character[][] str = new Character[160][120];
        Tile[][] tiles = game.tiles;
        for (int i = 0; i < 160; i++) {
            for (int i1 = 0; i1 < 120; i1++) {
                str[i][i1] = tiles[i][i1].getTileType().StringToCharacter();
            }
        }

        for (Farm farm : farms) {
            for (Structure structure : farm.getStructures()) {
                char symbol = ' ';
                if (structure instanceof Cottage) symbol = 'c';
                else if (structure instanceof Lake) symbol = 'L';
                else if (structure instanceof Quarry) symbol = 'Q';
                else if (structure instanceof GreenHouse) {
                    if (((GreenHouse) structure).isBuilt()) symbol = 'g';
                    else symbol = '-';
                } else if (structure instanceof Trunk) symbol = 't';
                else if (structure instanceof Tree) symbol = 'T';
                else if (structure instanceof Stone) symbol = '*';
                else if (structure instanceof Animal) symbol = 'a';
                else if (structure instanceof Fish) symbol = 'x';
                else if (structure instanceof Craft) symbol = '&';
                else if (structure instanceof AnimalProduct) symbol = '^';
                else if (structure instanceof FarmBuilding) symbol = '#';
                else if (structure instanceof ShippingBin) symbol = 'O';
                else if (structure instanceof Crop) symbol = 'r';
                else if (structure instanceof Mineral) symbol = '=';
                else if (structure instanceof MixedSeeds) symbol = '$';
                else if (structure instanceof Seed) symbol = 'z';
                else if (structure instanceof Tool) symbol = '\\';

                if (symbol != ' ') {
                    for (Tile tile : structure.getTiles()) {
                        str[tile.getX()][tile.getY()] = symbol;
                    }
                }
            }
        }
        for (Structure structure : structures) {
            char symbol = ' ';

            if (structure instanceof NPC) symbol = '?';
            else if (structure instanceof Player) symbol = '!';
            else if (structure instanceof Store) symbol = 's';
            else if (structure instanceof NPCHouse) symbol = 'N';
            else if (structure instanceof Fountain) symbol = 'f';

            if (symbol != ' ') {
                for (Tile tile : structure.getTiles()) {
                    str[tile.getX()][tile.getY()] = symbol;
                }
            }
        }
        Player player = app.getCurrentGame().getCurrentPlayer();
        str[player.getTiles().get(0).getX()][player.getTiles().get(0).getY()] = '@';

        int xStart = x - size / 2;
        int xEnd = x + size / 2;
        int yStart = y - size / 2;
        int yEnd = y + size / 2;
        if (xStart < 0) xStart = 0;
        if (yStart < 0) yStart = 0;
        if (xEnd >= 160) xEnd = 160;
        if (yEnd >= 120) yEnd = 120;


        for (int i = yEnd - 1; i >= yStart; i--) {
            for (int j1 = xStart; j1 < xEnd; j1++) {
                System.out.print(str[j1][i]);
            }
            System.out.println();
        }
       // printMa(0,0,2000);
    }

//    public void printMa(int x, int y, int size) {
//        Game game = app.getCurrentGame();
//        Character[][] str = new Character[160][120];
//        Tile[][] tiles = game.tiles;
//        for (int i = 0; i < 160; i++) {
//            for (int i1 = 0; i1 < 120; i1++) {
//                if (tiles[i][i1].getIsFilled()){
//                    str[i][i1] = '1';
//                }
//                else {
//                    str[i][i1] = ' ';
//                }
//            }
//        }
//
//        int xStart = x - size / 2;
//        int xEnd = x + size / 2;
//        int yStart = y - size / 2;
//        int yEnd = y + size / 2;
//        if (xStart < 0) xStart = 0;
//        if (yStart < 0) yStart = 0;
//        if (xEnd >= 160) xEnd = 160;
//        if (yEnd >= 120) yEnd = 120;
//
//
//        for (int i = yEnd - 1; i >= yStart; i--) {
//            for (int j1 = xStart; j1 < xEnd; j1++) {
//                System.out.print(str[j1][i]);
//            }
//            System.out.println();
//        }
//    }

    public void removeStructure(Structure structure) {
        this.getStructures().remove(structure);
        for (Farm farm : this.getFarms()) {
            farm.getStructures().remove(structure);
        }
        for (Tile tile : structure.getTiles()) {
            tile.setIsFilled(false);
        }
    }
}