package io.github.some_example_name.model.structure.stores;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import io.github.some_example_name.model.Pair;
import io.github.some_example_name.model.Tile;
import io.github.some_example_name.model.source.Source;
import io.github.some_example_name.model.structure.Structure;

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
