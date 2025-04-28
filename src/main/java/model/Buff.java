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
    private TimeAndDate buffImpact;

    private Buff(Integer maxPower, Ability ability, TimeAndDate buffImpact) {
        this.maxPower = maxPower;
        this.ability = ability;
        this.buffImpact = buffImpact;
    }
    public Buff(Integer buffImpact, Object object) {
        this.buffImpact = new TimeAndDate(0,buffImpact);
        if (object instanceof Ability) {
            this.ability = (Ability) object;
        }
        if (object instanceof Integer) {
            this.maxPower = (Integer) object;
        }
    }

    @Override
    public Object clone() {
        return new Buff(maxPower, ability, buffImpact);
    }
    //TODO implement buff
}
