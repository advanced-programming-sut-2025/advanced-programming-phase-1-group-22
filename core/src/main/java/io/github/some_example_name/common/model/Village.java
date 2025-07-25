package io.github.some_example_name.common.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonArray;
import io.github.some_example_name.client.GameClient;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import io.github.some_example_name.common.model.enums.Weather;
import io.github.some_example_name.common.model.relations.NPC;
import io.github.some_example_name.common.model.relations.NPCType;
import io.github.some_example_name.common.model.relations.NPCHouse;
import io.github.some_example_name.common.model.structure.Structure;
import io.github.some_example_name.common.model.structure.stores.Store;
import io.github.some_example_name.common.model.structure.stores.StoreType;
import io.github.some_example_name.server.saveGame.JsonPreparable;
import io.github.some_example_name.server.saveGame.ObjectWrapper;
import io.github.some_example_name.common.utils.App;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.UnaryOperator;

@Getter
@Setter
@ToString
public class Village implements JsonPreparable {
    private List<Tile> tiles;
    @JsonManagedReference
    private List<Farm> farms = new ArrayList<>();
    private List<Structure> structures = new ArrayList<>();
    private Weather weather = Weather.SUNNY;
    private Weather tomorrowWeather = Weather.SUNNY;
    @JsonProperty("structureWrappers")
    private List<ObjectWrapper> structureWrappers;

    public Village() {
    }


    public void initAfterLoad() {
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
            farm.generateRandomStructures(); //todo must be moved to gameServer
            if (farm.getCottage() != null) {
                farm.getFridge().getTiles().add(farm.getCottage().getTiles().get(2));
            }
        }
    }

    public void fillStructures() {
        setPath();

        Fountain fountain = new Fountain();
        for (int i = 57; i < 63; i++) {
            for (int j = 77; j < 83; j++) {
                fountain.getTiles().add(App.getInstance().getCurrentGame().tiles[j][i]);
                App.getInstance().getCurrentGame().tiles[j][i].setIsFilled(true);
                App.getInstance().getCurrentGame().tiles[j][i].setIsPassable(false);
            }
        }
        structures.add(fountain);
        GameClient.getInstance().enterRoom(100); //todo different room id
    }

    private void setPath() {
        for (int i = 57; i < 63; i++) {
            for (int j = 0; j < 160; j++) {
                Tile tile = App.getInstance().getCurrentGame().tiles[j][i];
                tile.setTileType(TileType.PATH);
            }
        }
        for (int i = 0; i < 120; i++) {
            for (int j = 77; j < 83; j++) {
                Tile tile = App.getInstance().getCurrentGame().tiles[j][i];
                tile.setTileType(TileType.PATH);
            }
        }
    }

    public void addStoresAndNpcs(JsonArray bodyArray) {
        int i = 0;
        StoreType fishShop = StoreType.FISH_SHOP;
        int randomNumber = bodyArray.get(i++).getAsInt();
        addStores(fishShop, 63, randomNumber, 8, 8);

        StoreType blackSmith = StoreType.BLACK_SMITH;
        randomNumber = bodyArray.get(i++).getAsInt();
        addStores(blackSmith, 63, randomNumber, 12, 14);

        StoreType carpenterShop = StoreType.CARPENTER_SHOP;
        randomNumber = bodyArray.get(i++).getAsInt();
        addStores(carpenterShop, 88, randomNumber, 13, 10);

        StoreType jojaMart = StoreType.JOJA_MART;
        randomNumber = bodyArray.get(i++).getAsInt();
        addStores(jojaMart, 88, randomNumber, 12, 12);

        StoreType stardropsalon = StoreType.STARDROPSALON;
        randomNumber = bodyArray.get(i++).getAsInt();
        addStores(stardropsalon, randomNumber, 49, 12, 9);

        StoreType pierreShop = StoreType.PIERRE_SHOP;
        randomNumber = bodyArray.get(i++).getAsInt();
        addStores(pierreShop, randomNumber, 49, 12, 9);

        StoreType marnieShop = StoreType.MARNIE_SHOP;
        randomNumber = bodyArray.get(i++).getAsInt();
        addStores(marnieShop, randomNumber, 63, 15, 10);

        NPCType lia = NPCType.LIA;
        randomNumber = bodyArray.get(i++).getAsInt();
        addNPC(lia, randomNumber, 66);


        NPCType ebigil = NPCType.ABIGIL;
        randomNumber = bodyArray.get(i++).getAsInt();
        addNPC(ebigil, randomNumber, 66);

        NPCType harvey = NPCType.HARVEY;
        randomNumber = bodyArray.get(i++).getAsInt();
        addNPC(harvey, randomNumber, 66);

        NPCType rabin = NPCType.RABIN;
        randomNumber = bodyArray.get(i++).getAsInt();
        addNPC(rabin, randomNumber, 49);

        NPCType sebastian = NPCType.SEBASTIAN;
        randomNumber = bodyArray.get(i++).getAsInt();
        addNPC(sebastian, randomNumber, 49);
    }

    public void addStores(StoreType storeType, int xStart, int yStart, int width, int height) {
        Store store = new Store(storeType, width, height);
        setTileOfStore(store, xStart, yStart, xStart + width, yStart + height);
        structures.add(store);
    }

    public void addNPC(NPCType NPCType, int xStart, int yStart) {
//        NPCType.setMissions();
        NPC npc = new NPC(NPCType);
        NPCHouse npcHouse = new NPCHouse(npc);

        setTileOfNPCHouse(npcHouse, xStart, yStart, xStart + npcHouse.getWidth(), yStart + npcHouse.getHeight());
        App.getInstance().getCurrentGame().getNpcs().add(npc);
        Tile tileByXAndY = getTileByXAndY(npcHouse.getTiles().get(0).getX(), npcHouse.getTiles().get(0).getY() - 1);
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
                store.getTiles().add(App.getInstance().getCurrentGame().tiles[i][j]);
                App.getInstance().getCurrentGame().tiles[i][j].setIsFilled(true);
            }
        }
    }

    public void setTileOfNPCHouse(NPCHouse npcHouse, int xStart, int yStart, int xEnd, int yEnd) {
        for (int i = xStart; i < xEnd; i++) {
            for (int j = yStart; j < yEnd; j++) {
                npcHouse.getTiles().add(App.getInstance().getCurrentGame().tiles[i][j]);
                App.getInstance().getCurrentGame().tiles[i][j].setIsFilled(true);
                App.getInstance().getCurrentGame().tiles[i][j].setIsPassable(false);
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

//    public void printMap(int x, int y, int size) {
//        Game game = App.getInstance().getCurrentGame();
//        String[][] str = new String[160][120];
//        Tile[][] tiles = game.tiles;
//        for (int i = 0; i < 160; i++) {
//            for (int i1 = 0; i1 < 120; i1++) {
//                str[i][i1] = tiles[i][i1].getTileType().StringToCharacter();
//            }
//        }
//
//        for (Farm farm : farms) {
//            for (Structure structure : farm.getStructures()) {
//                String emoji = "  ";
//                if (structure instanceof Cottage) emoji = "🏠";
//                else if (structure instanceof Lake) emoji = "🌊";
//                else if (structure instanceof Quarry) emoji = "⬛";
//                else if (structure instanceof GreenHouse)
//                    emoji = ((GreenHouse) structure).isBuilt() ? "🏡" : "🚧";
//                else if (structure instanceof Trunk) emoji = "🌳";
//                else if (structure instanceof Tree) emoji = "🌲";
//                else if (structure instanceof Stone) emoji = "🪨";
//                else if (structure instanceof Animal) emoji = "🐄";
//                else if (structure instanceof Craft) emoji = "🔨";
//                else if (structure instanceof AnimalProduct) emoji = "🥚";
//                else if (structure instanceof FarmBuilding) emoji = "🏚️";
//                else if (structure instanceof ShippingBin) emoji = "📦 ";
//                else if (structure instanceof Crop) emoji = "🌾";
//                else if (structure instanceof Mineral) emoji = "🔷";
//                else if (structure instanceof MixedSeeds) emoji = "🌱";
//                else if (structure instanceof Seed) emoji = "🫘";
//                //emoji = String.format("%-2s", emoji);
//                if (emoji != "  ") {
//                    for (Tile tile : structure.getTiles()) {
//                        str[tile.getX()][tile.getY()] = emoji;
//                    }
//                }
//            }
//        }
//        for (Structure structure : structures) {
//            String emoji = "  ";
//
//            if (structure instanceof NPC) emoji = "👴";
//            else if (structure instanceof Player) emoji = "🧍";
//            else if (structure instanceof Store) emoji = "🏬";
//            else if (structure instanceof NPCHouse) emoji = "🏘️";
//            else if (structure instanceof Fountain) emoji = "⛲";
//            //emoji = String.format("%-2s", emoji);
//            if (emoji != "  ") {
//                for (Tile tile : structure.getTiles()) {
//                    str[tile.getX()][tile.getY()] = emoji;
//                }
//            }
//        }
//        Player player = App.getInstance().getCurrentGame().getCurrentPlayer();
//        str[player.getTiles().getFirst().getX()][player.getTiles().getFirst().getY()] = "🧍";
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
//       // printMa(0,0,2000);
//    }

    public void printMap(int x, int y, int size) {

        Game game = App.getInstance().getCurrentGame();
        UnaryOperator<String> pad2 = s ->
                (s == null ? "  " : String.format("%-2s", s)).substring(0, 2);

//        String[][] str = new String[160][120];
//        Tile[][] tiles = game.tiles;
//        for (int i = 0; i < 160; i++) {
//            for (int j = 0; j < 120; j++) {
//                str[i][j] = pad2.apply(tiles[i][j].getTileType().StringToCharacter());
//            }
//        }
//
//        for (Farm farm : farms) {
//            for (Structure structure : farm.getStructures()) {
//                String emoji = switch (structure) {
//                    case Cottage c          -> "🏡";
//                    case Lake l             -> "🌊";
//                    case Quarry q           ->  "🗿";
//                    case GreenHouse g       -> g.isBuilt() ?  "🏢" : "🚧";
//                    case Trunk t            -> "🌳";
//                    case Tree t             -> "🌲";
//                    case Stone s            -> "🗿";
//                    case Animal a           -> "🐄";
//                    case Craft c            -> "🔨";
//                    case AnimalProduct ap   -> "🥚";
//                    case FarmBuilding fb    -> "🏚️";
//                    case ShippingBin sb     -> "🗳️";
//                    case Crop c             -> "🌾";
//                    case Mineral m          -> "🔷";
//                    case MixedSeeds ms      -> "🌱";
//                    case Seed s             -> "🫘";
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
//        for (Structure structure : structures) {
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
//
//        Player player = game.getCurrentPlayer();
//        str[player.getTiles().getFirst().getX()][player.getTiles().getFirst().getY()] = pad2.apply("🧍");
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
            tile.setIsPassable(true);
        }
    }

    @Override
    public void prepareForSave(ObjectMapper mapper) {
        structureWrappers = new ArrayList<>();
        for (Structure s : structures) {
            structureWrappers.add(new ObjectWrapper(s, mapper));
        }
        for (Farm farm : farms) {
            farm.prepareForSave(mapper);
        }
    }

    @Override
    public void unpackAfterLoad(ObjectMapper mapper) {
        structures = new ArrayList<>();
        for (ObjectWrapper wrapper : structureWrappers) {
            structures.add((Structure) wrapper.toObject(mapper));
        }
        for (Farm farm : farms) {
            farm.unpackAfterLoad(mapper);
        }
    }

}
