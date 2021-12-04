package forestry.item;

import forestry.Forestry;
import forestry.block.ForestryBlocks;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.util.registry.Registry;

public interface ForestryItems {

    Item CREATIVE_ENERGY = register("creative_energy", new BlockItem(ForestryBlocks.CARPENTER, new Item.Settings()));

    Item CARPENTER = register("carpenter", new BlockItem(ForestryBlocks.CARPENTER, new Item.Settings()));

    static void initialize() {
        // Done in class initialization
    }

    private static Item register(String id, Item block) {
        return Registry.register(Registry.ITEM, Forestry.id(id), block);
    }
}
