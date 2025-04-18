package model.products;

import lombok.Getter;
import model.Salable;
@Getter
public class Hay implements Salable {
    private final Integer price = 50 / 2;
}