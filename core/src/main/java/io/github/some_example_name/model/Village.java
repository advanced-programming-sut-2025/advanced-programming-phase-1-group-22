package io.github.some_example_name.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import io.github.some_example_name.model.enums.Weather;
import io.github.some_example_name.model.relations.NPC;
import io.github.some_example_name.model.relations.NPCType;
import io.github.some_example_name.model.relations.NPCHouse;
import io.github.some_example_name.model.structure.Structure;
import io.github.some_example_name.model.structure.stores.Store;
import io.github.some_example_name.model.structure.stores.StoreType;
import io.github.some_example_name.saveGame.JsonPreparable;
import io.github.some_example_name.saveGame.ObjectWrapper;
import io.github.some_example_name.utils.App;

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
            farm.generateRandomStructures();
            if (farm.getCottage() != null) {
                farm.getFridge().getTiles().add(farm.getCottage().getTiles().get(2));
            }
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


        NPCType ebigil = NPCType.ABIGIL;
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
                fountain.getTiles().add(App.getInstance().getCurrentGame().tiles[j][i]);
                App.getInstance().getCurrentGame().tiles[j][i].setIsFilled(true);
                App.getInstance().getCurrentGame().tiles[j][i].setIsPassable(false);
            }
        }
        structures.add(fountain);

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
