package model.craft;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import model.Salable;
import model.structure.Structure;

@Getter
@Setter
@ToString
public class Craft extends Structure implements Salable {
    private Integer id;
    private CraftType craftType;
}
