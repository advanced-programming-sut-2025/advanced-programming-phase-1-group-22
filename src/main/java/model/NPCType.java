package model;

import lombok.Getter;
import lombok.ToString;
import model.animal.FishType;
import model.cook.FoodType;
import model.craft.CraftType;
import model.enums.Season;
import model.products.AnimalProductType;
import model.products.TreesAndFruitsAndSeeds.MadeProductType;
import model.receipe.Recipe;
import model.source.CropType;
import model.source.MineralType;
import model.structure.NPCHouse;
import model.tools.WateringCan;

import java.util.List;
import java.util.Map;

@Getter
@ToString
public enum NPCType {
    SEBASTIAN("sebastian","", List.of(AnimalProductType.SHEEP_WOOL, FoodType.PUMPKIN_PIE,FoodType.PIZZA),
            List.of(new Mission(NPCType.valueOf("SEBASTIAN"),
                            Map.of(MineralType.IRON,50),
                            Map.of(MineralType.DIAMOND,2),0),
                    new Mission(NPCType.valueOf("SEBASTIAN"),
                            Map.of(FoodType.PUMPKIN_PIE,1),
                            Map.of(MineralType.GOLD,5000),0),
                    new Mission(NPCType.valueOf("SEBASTIAN"),
                            Map.of(MineralType.STONE,150),
                            Map.of(MineralType.QUARTZ,50), Season.FALL))),
    EBIGIL("ebigel","",List.of(MineralType.IRON_ORE,MineralType.STONE, MadeProductType.COFFE),
            List.of(new Mission(NPCType.valueOf("EBIGIL"),
                    Map.of(MadeProductType.GOLD_BAR,1),
                    Map.of(null,1), 0),
                    new Mission(NPCType.valueOf("EBIGIL"),
                            Map.of(CropType.PUMPKIN,1),
                            Map.of(MineralType.GOLD,500), 1),
                    new Mission(NPCType.valueOf("EBIGIL"),
                            Map.of(CropType.WHEAT,50),
                            Map.of(WateringCan.IRIDIUM,1), Season.WINTER))),
    HARVEY("harvey","",List.of(MadeProductType.PICKLES,MadeProductType.WINE,MadeProductType.COFFE),
            List.of(new Mission(NPCType.valueOf("HARVEY"),
                   Map.of(null,12),
                    Map.of(MineralType.GOLD,750),0),
                    new Mission(NPCType.valueOf("HARVEY"),
                            Map.of(FishType.SALMON,1),
                            Map.of(null,1),1),
                    new Mission(NPCType.valueOf("HARVEY"),
                            Map.of(MadeProductType.WINE,1),
                            Map.of(FoodType.SALAD,5),Season.WINTER))),
    LIA("lia","",List.of(MadeProductType.WINE,CropType.GRAPE,FoodType.SALAD),
            List.of(new Mission(NPCType.valueOf("LIA"),
                    Map.of(MineralType.HARD_WOOD,10),
                    Map.of(MineralType.GOLD,500),0),
                    new Mission(NPCType.valueOf("LIA"),
                            Map.of(FishType.SALMON,1),
                            Map.of(new  Recipe ("A receipe to make Salmon Dinner",0),1),1),
                    new Mission(NPCType.valueOf("LIA"),
                            Map.of(MineralType.WOOD,200),
                            Map.of(CraftType.DELUXE_SCARECROW,3),Season.SUMMER))),
    RABIN("rabin","",List.of(FoodType.SPAGHETTI,MineralType.WOOD,MadeProductType.IRON_BAR),
            List.of(new Mission(NPCType.valueOf("ROBIN"),
                    Map.of(MineralType.WOOD,80),
                    Map.of(MineralType.GOLD,1000),0),
                    new Mission(NPCType.valueOf("ROBIN"),
                            Map.of(MadeProductType.IRON_BAR,10),
                            Map.of(CraftType.BEE_HOUSE,3),1),
                    new Mission(NPCType.valueOf("ROBIN"),
                            Map.of(MineralType.WOOD,1000),
                            Map.of(MineralType.GOLD,25_000),Season.WINTER)));

    private Integer id;
    private final String name;
    private final String job;
    private final List<Salable> favorites;
    private String personality;
    private NPCHouse NPCHouse;
    private final List<Mission> missions;

    NPCType(String name, String job, List<Salable> favorites, List<Mission> missions) {
        this.name = name;
        this.job = job;
        this.favorites = favorites;
        this.missions = missions;
    }
}
