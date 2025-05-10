package model.structure.stores;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import model.Pair;
import model.Tile;
import model.source.Source;
import model.structure.Structure;

import java.util.List;
import java.util.Objects;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class Store extends Structure{
   private StoreType storeType;

   public Store(StoreType storeType) {
      this.storeType = storeType;
   }

}
