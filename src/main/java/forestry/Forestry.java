package forestry;

import forestry.block.ForestryBlocks;
import forestry.block.entity.ForestryBlockEntities;
import forestry.item.ForestryItems;
import forestry.recipe.ForestryRecipes;
import net.minecraft.util.Identifier;

public interface Forestry {

    String MOD_ID = "forestry";

    static Identifier id(String path) {
        return new Identifier(MOD_ID, path);
    }

    static Identifier id(String... path) {
        return new Identifier(MOD_ID, String.join("/", path));
    }

    static void initialize() {
        ForestryBlocks.initialize();
        ForestryBlockEntities.initialize();
        ForestryItems.initialize();
        ForestryRecipes.initialize();
    }
}
