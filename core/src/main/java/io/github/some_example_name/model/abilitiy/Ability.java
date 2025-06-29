package io.github.some_example_name.model.abilitiy;

import lombok.Getter;

@Getter
public enum Ability{
    FARMING(5),
    FISHING(5),
    FORAGING(10),
    MINING(10);

    private final Integer upgradeAbility;

    Ability(Integer upgradeAbility) {
        this.upgradeAbility = upgradeAbility;
    }
}
