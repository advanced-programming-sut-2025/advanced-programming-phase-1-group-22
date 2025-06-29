package io.github.some_example_name.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import io.github.some_example_name.model.abilitiy.Ability;
import io.github.some_example_name.model.relations.Player;

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

    public boolean nextHour() {
        buffImpact.setHour(buffImpact.getHour() - 1);
        return buffImpact.getHour() != 0;
    }
    public void affectBuff(Player player) {
        if (maxPower != null && maxPower != 0) {
            player.setMaxEnergy(player.getMaxEnergy() + maxPower);
            player.changeEnergy(maxPower);
        }
        if (ability != null) {
            player.setBuffAbility(ability);
        }
    }

    public void defectBuff(Player player) {
        if (maxPower != null && maxPower != 0) {
            player.setMaxEnergy(player.getMaxEnergy() - maxPower);
        }
        if (ability != null) {
            player.setBuffAbility(null);
        }
    }

    @Override
    public Object clone() {
        return new Buff(maxPower, ability, buffImpact);
    }
}
