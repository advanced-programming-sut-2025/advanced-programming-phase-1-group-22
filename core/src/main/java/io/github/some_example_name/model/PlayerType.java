package io.github.some_example_name.model;

import com.badlogic.gdx.graphics.Texture;
import io.github.some_example_name.model.enums.Gender;
import io.github.some_example_name.utils.GameAsset;
import lombok.Getter;

@Getter
public enum PlayerType {
    ABIGAIL("Abigail", GameAsset.ABIGAIL_FULL, Gender.FEMALE),
    ALEX("Alex", GameAsset.ALEX_FULL, Gender.MALE),
    EMILY("Emily", GameAsset.EMILY_FULL, Gender.FEMALE),
    ELLIOT("Elliot", GameAsset.ELLIOT_FULL, Gender.MALE),
    HALEY("Haley", GameAsset.HALEY_FULL, Gender.FEMALE),
    HARVEY("Harvey", GameAsset.HARVEY_FULL, Gender.MALE),
    LEAH("Leah", GameAsset.LEAH_FULL, Gender.FEMALE),
    MARU("Maru", GameAsset.MARU_FULL, Gender.FEMALE),
    PENNY("Penny", GameAsset.PENNY_FULL, Gender.FEMALE),
    SAM("Sam", GameAsset.SAM_FULL, Gender.MALE),
    SHANE("Shane", GameAsset.SHANE_FULL, Gender.MALE),
    SEBASTIAN("Sebastian", GameAsset.SEBASTIAN_FULL, Gender.MALE);

    private final Texture texture;
    private final Gender gender;
    private final String name;
    PlayerType(String name, Texture texture, Gender gender) {
        this.texture = texture;
        this.name = name;
        this.gender = gender;
    }
}
