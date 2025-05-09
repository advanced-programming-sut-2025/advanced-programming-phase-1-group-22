package model.gameSundry;

import lombok.Getter;
import lombok.Setter;
import model.Salable;

import java.io.Serializable;

@Getter
@Setter
public class Sundry implements Salable, Serializable {
    private SundryType sundryType;

    public Sundry(SundryType sundryType) {
        this.sundryType = sundryType;
    }

    public Sundry() {
    }

    @Override
    public String getName() {
        return sundryType.getName();
    }

    @Override
    public int getSellPrice() {
        return sundryType.getPrice();
    }

    @Override
    public Integer getContainingEnergy() {return sundryType.getContainingEnergy();}
}
