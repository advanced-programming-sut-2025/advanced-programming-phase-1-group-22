package model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import model.enums.Weather;
import model.structure.Structure;
import model.structure.stores.Store;
import model.structure.stores.StoreType;
import utils.App;

import java.sql.Time;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Getter
@Setter
@ToString
public class Village {
    private List<Tile> tiles;
    private List<Farm> farms = new ArrayList<>();
    private List<Structure> structures = new ArrayList<>();
    private Weather weather = Weather.SUNNY;
    private Weather tomorrowWeather = Weather.SUNNY;
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
        shuffleFarms();
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
                app.getCurrentGame().tiles[i][j].setIsFilled(true);
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
        NPC npc = new NPC(NPCType);
        if (vertical) {
            setTileOfNPC(npc, xStart, yStart, xStart + 4, yStart + 6);
        } else {
            setTileOfNPC(npc, xStart, yStart, xStart + 6, yStart + 4);
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
                app.getCurrentGame().tiles[i][j].setIsFilled(true);
            }
        }
    }

    public void setTileOfNPC(NPC NPC, int xStart, int yStart, int xEnd, int yEnd) {
        for (int i = xStart; i < xEnd; i++) {
            for (int j = yStart; j < yEnd; j++) {
                NPC.getTiles().add(app.getCurrentGame().tiles[i][j]);
                app.getCurrentGame().tiles[i][j].setIsFilled(true);
            }
        }
    }

    public void startDay() {
        TimeAndDate timeAndDate = app.getCurrentGame().getTimeAndDate();
        weather = tomorrowWeather;
        do {
            tomorrowWeather = Weather.values()[getRandomNumber(0, 3)];
        } while (tomorrowWeather.getSeasons().contains(timeAndDate.getSeason()));
        if (weather == Weather.STORMY) {
            for (Farm farm : farms) {
                for (int i = 0; i < 3; i++) {
                    int randX = getRandomNumber(farm.getFarmXStart(), farm.getFarmXEnd());
                    int randY = getRandomNumber(farm.getFarmYStart(), farm.getFarmYEnd());
                    weather.thunderBolt(randX, randY);
                }
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
}

    public void removeStructure(Structure structure){
		this.getStructures().remove(structure);
        for (Farm farm : this.getFarms()) {
			farm.getStructures().remove(structure);
        }
    }

    public void addStructureToPlayerFarmByPlayerTile(Player player, Structure structure){
        for (Farm farm : this.getFarms()) {
            if (farm.getTiles().contains(player.getTiles().getFirst())){
                farm.getStructures().add(structure);
            }
        }
    }
}