package model.gameSundry;

import lombok.Getter;
import lombok.Setter;
import model.Salable;
@Getter
@Setter
public class Sundry implements Salable {
    private SundryType sundryType;

    public Sundry(SundryType sundryType) {
        this.sundryType = sundryType;
    }

    @Override
    public String getName() {
        return sundryType.getName();
    }

    @Override
    public int getSellPrice() {
        return sundryType.getPrice();
    }
}
