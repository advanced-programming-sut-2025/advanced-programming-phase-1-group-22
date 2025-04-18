package model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import model.abilitiy.Ability;
@Getter
@Setter
@ToString
public class Buff {
    private Integer id;
    private Integer maxPower;
    private Ability ability;
    private Integer buffImpact;
    public Buff(Integer buffImpact, Object object) {
        this.buffImpact = buffImpact;
        if (object instanceof Ability) {
            this.ability = (Ability) object;
        }
        if (object instanceof Integer) {
            this.maxPower = (Integer) object;
        }
    }
}
