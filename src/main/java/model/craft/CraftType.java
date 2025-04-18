package model.craft;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import model.Salable;
import model.abilitiy.Ability;
import model.products.AnimalProduct;
import model.products.AnimalProductType;
import model.products.Product;
import model.products.TreesAndFruitsAndSeeds.MadeProductType;
import model.products.TreesAndFruitsAndSeeds.TreeType;
import model.source.CropType;
import model.source.MineralType;
import model.source.SeedType;
import model.source.Source;
import model.structure.stores.StoreType;

import java.util.Map;
@Getter
@ToString
public enum CraftType implements Product{
    CHERRY_BOMB("هرچیز در شعاع ۳ تایلی را نابود میکند",Map.of(MineralType.COPPER_ORE,4,MineralType.COAL,1),50,Map.of(Ability.MINING,1)),
    BOMB("هرچیز در شعاع  ۵ تایلی را نابود میکند",Map.of(MineralType.IRON_ORE,4,MineralType.COAL,1),50,Map.of(Ability.MINING,2)),
    MEGA_BOMB("هرچیز در شعاع ۷ تایلی را نابود میکند",Map.of(MineralType.GOLD_ORE,4,MineralType.COAL,1),50,Map.of(Ability.MINING,3)),
    SPRINKLER("به ۴ تایل مجاور آب میدهد",Map.of(MadeProductType.IRON_BAR,1,MadeProductType.COPPER_BAR,1),0,Map.of(Ability.FARMING,1)),
    QUALITY_SPRINKLER("به ۸ تایل مجاور آب میدهد",Map.of(MadeProductType.IRON_BAR,1,MadeProductType.GOLD_BAR,1),0,Map.of(Ability.FARMING,2))
    ,IRIDIUM_SPRINKLER("به ۲۴ تایل مجاور آب میدهد",Map.of(MadeProductType.IRIDIUM_BAR,1,MadeProductType.GOLD_BAR,1),0,Map.of(Ability.FARMING,3)),
    CHARCOAL_KLIN("۱۰ چوب را تبدیل به ۱ ذغال میکند",Map.of(MineralType.WOOD,20,MadeProductType.COPPER_BAR,2),0,Map.of(Ability.FORAGING,1)),
    FORNACE("کانی ها و ذغال را تبدیل به شمش میکند",Map.of(MineralType.COPPER_ORE,20,MineralType.STONE,25),0,Map.of()),
    SCARE_CROW("از حمله کلاغ ها تا شعاع ۸ تایلی جلوگیری میکند",Map.of(MineralType.COAL,1,MineralType.WOOD,50,MineralType.FIBER,20),0,Map.of()),
    DELUXE_SCARECROW("از حمله کلاغ ها تا شعاع 12 تایلی جلوگیری میکند",Map.of(MineralType.WOOD,50,MineralType.COAL,1,MineralType.FIBER,20,MineralType.IRIDIUM_ORE,1),0,Map.of(Ability.FARMING,2)),
    BEE_HOUSE("اگر در مزرعه گذاشته شود عسل تولید میکند",Map.of(MineralType.WOOD,40,MineralType.COAL,8,MadeProductType.IRON_BAR,1),0,Map.of(Ability.FARMING,1)),
    CHEESE_PRERSS("شیر را به پنیر تبدیل میکند",Map.of(MineralType.WOOD,45,MineralType.STONE,45,MadeProductType.COPPER_BAR,1),0,Map.of(Ability.FARMING,2)),
    KEG("میوه و سبزیجات را به نوشیدنی تبدیل میکند",Map.of(MineralType.WOOD,30,MadeProductType.COPPER_BAR,1,MadeProductType.IRON_BAR,1),0,Map.of(Ability.FARMING,3)),
    LOOM("پشم را به پارچه تبدیل میکند",Map.of(MineralType.WOOD,60,MineralType.FIBER,30),0,Map.of(Ability.FARMING,3)),
    MAYONNAISE_MACHINE("تخم مرغ را به سس مایونز تبدیل میکند",Map.of(MineralType.WOOD,15,MineralType.STONE,15,MadeProductType.COPPER_BAR,1),0,Map.of()),
    OIL_MAKER("truffle را به روغن تبدیل میکند",Map.of(MineralType.WOOD,100,MadeProductType.IRON_BAR,1,MadeProductType.GOLD_BAR,1),0,Map.of(Ability.FARMING,3)),
    PRESERVES_JAR("سبزیجات را به ترشی و میوه ها را به مربا تبدیل میکند",Map.of(MineralType.WOOD,50,MineralType.STONE,40,MineralType.COAL,8),0,Map.of(Ability.FARMING,2)),
    DEHYDRATOR("میوه یا قارچ را خشک میکند",Map.of(MineralType.WOOD,30,MineralType.STONE,20,MineralType.FIBER,30),0,StoreType.PIERRE_SHOP),
    FISH_SMOKER("هر ماهی را با یک ذغال با حفظ کیفیتش تبدیل به ماهی دودی میکند",Map.of(MineralType.WOOD,50,MineralType.COAL,10,MadeProductType.IRON_BAR,3),0,StoreType.FISH_SHOP),
    MYSTIC_TREE_SEED("میتواند کاشته شود تا mystic tree رشد کند",Map.of(TreeType.PINE_CONES,5,TreeType.MAHOGANY_SEEDS,5,TreeType.ACORNS,5,TreeType.MAPLE_SEEDS,5),100,Map.of(Ability.MINING,4));

    CraftType(String description, Map<Salable, Integer> products, Integer sellPrice, Map<Ability, Integer> abilities) {
        this.description = description;
        this.products = products;
        this.sellPrice = sellPrice;
        this.abilities = abilities;
    }

    CraftType(String description, Map<Salable, Integer> products, Integer sellPrice, StoreType storeType) {
        this.description = description;
        this.products = products;
        this.sellPrice = sellPrice;
        this.storeType = storeType;
    }

    private final String description;
    private final Map<Salable, Integer> products;
    private final Integer sellPrice;
    private Map<Ability, Integer> abilities;
    private StoreType storeType;

    public int getSellPrice(){
        return this.sellPrice;
    }
}