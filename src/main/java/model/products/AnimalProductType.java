package model.products;

public enum AnimalProductType implements Product {
    HEN_EGG(50), HEN_BIG_EGG(95), DUCK_EGG(95), DUCK_FEATHER(250),
    RABBIT_WOOL(340), RABBIT_LEG(565), DINOSAUR_EGG(350), MILK(125), BIG_MILK(190)
    , GOAT_MILK(225), BIG_GOAT_MILK(345), SHEEP_WOOL(340), TRUFFLE(625);
    private final Integer price;

    AnimalProductType(Integer price) {
        this.price = price;
    }

    @Override
    public int getSellPrice() {
        return price;
    }
}
