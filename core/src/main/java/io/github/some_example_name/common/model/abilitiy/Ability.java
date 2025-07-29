package io.github.some_example_name.common.model.abilitiy;

import com.badlogic.gdx.graphics.Texture;
import io.github.some_example_name.common.utils.GameAsset;
import lombok.Getter;

@Getter
public enum Ability{
    FARMING("Farming",5,GameAsset.RADISH,"You can get XP by plant seeds and harvest them"),
    FISHING("Fishing",5,GameAsset.BAMBOO_POLE,"You can improve this ability by fishing!"),
    FORAGING("Foraging",10, GameAsset.FORESTER,"You can improve this ability by spending time in nature!"),
    MINING("Mining",10,GameAsset.PICKAXE,"You have to mine rocks to get XP");

    private final Integer upgradeAbility;
    private final String name;
    private final Texture texture;
    private final String description;

    Ability(String name,Integer upgradeAbility,Texture texture,String description) {
        this.name = name;
        this.upgradeAbility = upgradeAbility;
        this.texture = texture;
        this.description = description;
    }

    public static Ability getFromName(String name){
        for (Ability value : Ability.values()) {
            if (value.name.equals(name)) return value;
        }
        return null;
    }
}
