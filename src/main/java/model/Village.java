package model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import model.structure.Structure;
import model.structure.stores.Store;
import model.structure.stores.StoreType;
import utils.App;

import java.util.List;
import java.util.Random;

@Getter
@Setter
@ToString
public class Village {
    private List<Tile> tiles;
    private List<Farm> farms;
    private List<Structure> structures;
    private App app = App.getInstance();

    public Village() {
        fillStructures();
        fillFarms();
    }

    public void shuffleFarms() {

    }

    public void fillFarms() {
        shuffleFarms();
        for (int i = 0; i < farms.size(); i++) {
            Farm farm = farms.get(i);
            farm.fillFarmType(i);
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
        addStores(marnieShop, randomNumber, 61, false);

        NPCType lia = NPCType.LIA;
        randomNumber = getRandomNumber(29, 37);
        addNPC(lia, randomNumber, 61, false);


        NPCType ebigil = NPCType.EBIGIL;
        randomNumber = getRandomNumber(100, 112);
        addNPC(ebigil, randomNumber, 61, false);

        NPCType harvey = NPCType.HARVEY;
        randomNumber = getRandomNumber(132, 145);
        addNPC(harvey, randomNumber, 61, false);

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
                fountain.getTiles().add(app.getCurrentGame().tiles[i][j]);
            }
        }
        structures.add(fountain);

    }

    private void setPath() {
        for (int i = 57; i < 63; i++) {
            for (int j = 0; j < 160; j++) {
                for (Tile[] tile : app.getCurrentGame().tiles) {
                    for (Tile tile1 : tile) {
                        tile1.setTileType(TileType.PATH);
                    }
                }
            }
        }
        for (int i = 0; i < 120; i++) {
            for (int j = 77; j < 83; j++) {
                for (Tile[] tile : app.getCurrentGame().tiles) {
                    for (Tile tile1 : tile) {
                        tile1.setTileType(TileType.PATH);
                    }
                }
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
        NPC npc = new NPC(NPCType);
        if (vertical) {
            setTileOfNPC(npc, xStart, yStart, xStart + 8, yStart + 12);
        } else {
            setTileOfNPC(npc, xStart, yStart, xStart + 12, yStart + 8);
        }
        structures.add(npc);
    }

    public int getRandomNumber(int start, int end) {
        Random random = new Random();
        return random.nextInt(end - start + 1) + start;
    }

    public void setTileOfStore(Store store, int xStart, int yStart, int xEnd, int yEnd) {
        for (int i = xStart; i < xEnd; i++) {
            for (int j = yStart; j < yEnd; j++) {
                store.getTiles().add(app.getCurrentGame().tiles[i][j]);
            }
        }
    }

    public void setTileOfNPC(NPC NPC, int xStart, int yStart, int xEnd, int yEnd) {
        for (int i = xStart; i < xEnd; i++) {
            for (int j = yStart; j < yEnd; j++) {
                NPC.getTiles().add(app.getCurrentGame().tiles[i][j]);
            }
        }
    }
}
