package model.products.TreesAndFruitsAndSeeds;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import model.structure.Structure;
@Getter
@Setter
@ToString
public class Tree extends Structure {
    TreeType treeType;

    public Tree(TreeType treeType) {
        this.treeType = treeType;
    }

    public void burn() {
        //TODO
    }
}
