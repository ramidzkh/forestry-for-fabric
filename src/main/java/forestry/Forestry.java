package forestry;

import forestry.block.ForestryBlocks;
import forestry.block.entity.ForestryBlockEntities;
import forestry.feature.Features;
import forestry.item.ForestryItems;
import forestry.item.ForestryTags;
import forestry.recipe.ForestryRecipes;
import forestry.util.ForestryFeatureRegistry;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.Identifier;

public interface Forestry {

    String MOD_ID = "forestry";

    static Identifier id(String path) {
        return new Identifier(MOD_ID, path);
    }

    static Identifier id(String... path) {
        return new Identifier(MOD_ID, String.join("/", path));
    }

    static ForestryFeatureRegistry create(String prefix, ItemGroup group) {
        return new ForestryFeatureRegistry(id(prefix), () -> new Item.Settings().group(group));
    }

    static void initialize() {
        ForestryTags.initialize();
        Features.initialize();
        ForestryBlocks.initialize();
        ForestryBlockEntities.initialize();
        ForestryItems.initialize();
        ForestryRecipes.initialize();
    }
}
