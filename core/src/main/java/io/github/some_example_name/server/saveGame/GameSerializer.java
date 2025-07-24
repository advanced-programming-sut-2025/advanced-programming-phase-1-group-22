package io.github.some_example_name.server.saveGame;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import io.github.some_example_name.common.model.*;
import io.github.some_example_name.common.model.relations.*;
import io.github.some_example_name.common.model.source.*;
import io.github.some_example_name.common.model.structure.Stone;
import io.github.some_example_name.common.model.structure.StoneType;
import io.github.some_example_name.common.model.structure.Trunk;
import io.github.some_example_name.common.model.structure.TrunkType;
import io.github.some_example_name.common.model.structure.stores.*;
import io.github.some_example_name.common.model.tools.*;
import io.github.some_example_name.common.model.abilitiy.Ability;
import io.github.some_example_name.common.model.enums.Gender;
import io.github.some_example_name.common.model.enums.Season;
import io.github.some_example_name.common.model.enums.Weather;
import io.github.some_example_name.common.model.gameSundry.Sundry;
import io.github.some_example_name.common.model.gameSundry.SundryType;
import io.github.some_example_name.common.model.products.TreesAndFruitsAndSeeds.Tree;
import io.github.some_example_name.common.model.products.TreesAndFruitsAndSeeds.TreeType;
import io.github.some_example_name.common.model.receipe.CookingRecipe;
import io.github.some_example_name.common.model.receipe.CraftingRecipe;
import io.github.some_example_name.common.model.shelter.FarmBuildingType;
import io.github.some_example_name.common.model.structure.farmInitialElements.Cottage;
import io.github.some_example_name.common.model.structure.farmInitialElements.GreenHouse;
import io.github.some_example_name.common.model.structure.farmInitialElements.Lake;
import io.github.some_example_name.common.model.structure.farmInitialElements.Quarry;
import io.github.some_example_name.client.view.Menu;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GameSerializer {
    private static Kryo createKryo() {
        Kryo kryo = new Kryo();
        kryo.setReferences(true);
        kryo.setRegistrationRequired(true);
        kryo.register(Game.class);
        kryo.register(Village.class);
        kryo.register(Player.class);
        kryo.register(NPC.class);
        kryo.register(NPCType.class);
        kryo.register(Friendship.class);
        kryo.register(TimeAndDate.class);
        kryo.register(Tile.class);
        kryo.register(ArrayList.class);
        kryo.register(List.class);
        kryo.register(Tile[][].class);
        kryo.register(Tile[].class);
        kryo.register(Double.class);
        kryo.register(Integer.class);
        kryo.register(HashMap.class);
        kryo.register(Ability.class);
        kryo.register(Account.class);
        kryo.register(CookingRecipe.class);
        kryo.register(CraftingRecipe.class);
        kryo.register(Menu.class);
        kryo.register(FarmType.class);
        kryo.register(BackPack.class);
        kryo.register(BackPackType.class);
        kryo.register(TrashCan.class);
        kryo.register(Pickaxe.class);
        kryo.register(Flower.class);
        kryo.register(Axe.class);
        kryo.register(Axe.class);
        kryo.register(WateringCan.class);
        kryo.register(WateringCanType.class);
        kryo.register(Sundry.class);
        kryo.register(SundryType.class);
        kryo.register(Hoe.class);
        kryo.register(MilkPail.class);
        kryo.register(FishingPole.class);
        kryo.register(Scythe.class);
        kryo.register(Shear.class);
        kryo.register(StoreType.class);
        kryo.register(BlackSmithStuff.class);
        kryo.register(BlackSmithUpgrade.class);
        kryo.register(CarpenterShopFarmBuildings.class);
        kryo.register(CarpenterShopMineralStuff.class);
        kryo.register(FishShopStuff.class);
        kryo.register(JojaMartShopSeed.class);
        kryo.register(MarnieShopAnimal.class);
        kryo.register(MarnieShopAnimalRequierment.class);
        kryo.register(PierreShop.class);
        kryo.register(MarnieShopAnimalRequierment.class);
        kryo.register(TheStardropSaloonStuff.class);
        kryo.register(Tile.class);
        kryo.register(TileType.class);
        kryo.register(User.class);
        kryo.register(Gender.class);
        kryo.register(Season.class);
        kryo.register(Farm.class);
        kryo.register(FarmType.class);
        kryo.register(Fridge.class);
        kryo.register(Fountain.class);
        kryo.register(Buff.class);
        kryo.register(Direction.class);
        kryo.register(Cottage.class);
        kryo.register(Direction.class);
        kryo.register(FarmBuildingType.class);
        kryo.register(Quarry.class);
        kryo.register(Pair.class);
        kryo.register(GreenHouse.class);
        kryo.register(Lake.class);
        kryo.register(Trunk.class);
        kryo.register(TrunkType.class);
        kryo.register(Stone.class);
        kryo.register(StoneType.class);
        kryo.register(Seed.class);
        kryo.register(SeedType.class);
        kryo.register(Mineral.class);
        kryo.register(MineralType.class);
        kryo.register(Crop.class);
        kryo.register(CropType.class);
        kryo.register(MixedSeeds.class);
        kryo.register(MixedSeedsType.class);
        kryo.register(Tree.class);
        kryo.register(TreeType.class);
        kryo.register(Store.class);
        kryo.register(NPCHouse.class);
        kryo.register(Weather.class);

        return kryo;
    }


    public static void saveGame(Game game, String filePath) {
        Kryo kryo = createKryo();
        try (Output output = new Output(new FileOutputStream(filePath))) {
            kryo.writeObject(output, game);
        } catch (IOException e) {
            throw new RuntimeException("Failed to save game", e);
        }
    }

    public static Game loadGame(String filePath) {
        Kryo kryo = createKryo();
        try (Input input = new Input(new FileInputStream(filePath))) {
            return kryo.readObject(input, Game.class);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load game", e);
        }
    }
}
