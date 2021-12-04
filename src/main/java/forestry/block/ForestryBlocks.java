package forestry.block;

import forestry.Forestry;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.util.registry.Registry;

public interface ForestryBlocks {

    Block CREATIVE_ENERGY = register("creative_energy", new CreativeEnergy(FabricBlockSettings.of(Material.METAL).hardness(-1)));

    Block CARPENTER = register("carpenter", new CarpenterBlock(FabricBlockSettings.of(Material.METAL)));

    static void initialize() {
        // Done in class initialization
    }

    private static Block register(String id, Block block) {
        return Registry.register(Registry.BLOCK, Forestry.id(id), block);
    }
}
