package model.structure.stores;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import model.source.Source;
import model.structure.Structure;

import java.util.List;

@Getter
@Setter
@ToString
public class Store extends Structure implements Source {
   private StoreType storeType;

   public Store(StoreType storeType) {
      this.storeType = storeType;
   }
}
