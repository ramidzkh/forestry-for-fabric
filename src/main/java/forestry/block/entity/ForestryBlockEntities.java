package forestry.block.entity;

import forestry.Forestry;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.registry.Registry;

public interface ForestryBlockEntities {

    BlockEntityType<CreativeEnergyBlockEntity> CREATIVE_ENERGY = register("creative_energy", CreativeEnergyBlockEntity::new);

    BlockEntityType<CarpenterBlockEntity> CARPENTER = register("carpenter", CarpenterBlockEntity::new);

    static void initialize() {
        // Done in class initialization
    }

    private static <E extends BlockEntity> BlockEntityType<E> register(String id, FabricBlockEntityTypeBuilder.Factory<E> factory, Block... blocks) {
        return Registry.register(Registry.BLOCK_ENTITY_TYPE, Forestry.id(id), FabricBlockEntityTypeBuilder.create(factory, blocks).build());
    }
}
