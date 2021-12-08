package forestry;

import forestry.block.ForestryBlocks;
import forestry.block.entity.ForestryBlockEntities;
import forestry.feature.Features;
import forestry.item.ForestryItems;
import forestry.recipe.ForestryRecipes;
import forestry.util.ForestryFeatureRegistry;
import net.minecraft.util.Identifier;

public interface Forestry {

    String MOD_ID = "forestry";

    static Identifier id(String path) {
        return new Identifier(MOD_ID, path);
    }

    static Identifier id(String... path) {
        return new Identifier(MOD_ID, String.join("/", path));
    }

    static ForestryFeatureRegistry create(String prefix) {
        return new ForestryFeatureRegistry(id(prefix));
    }

    static void initialize() {
        Features.initialize();
        ForestryBlocks.initialize();
        ForestryBlockEntities.initialize();
        ForestryItems.initialize();
        ForestryRecipes.initialize();
    }
}
