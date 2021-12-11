package forestry.block.entity;

import forestry.Forestry;
import forestry.block.ForestryBlocks;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.registry.Registry;
import org.apache.commons.lang3.ArrayUtils;

public interface ForestryBlockEntities {

    BlockEntityType<CreativeEnergyBlockEntity> CREATIVE_ENERGY = register("creative_energy", CreativeEnergyBlockEntity::new, ForestryBlocks.CREATIVE_ENERGY);

    BlockEntityType<CarpenterBlockEntity> CARPENTER = register("carpenter", CarpenterBlockEntity::new, ForestryBlocks.CARPENTER);

    static void initialize() {
        MachineBlockEntity.initialize();
    }

    private static <E extends BlockEntity> BlockEntityType<E> register(String id, FabricBlockEntityTypeBuilder.Factory<E> factory, Block block, Block... blocks) {
        return Registry.register(Registry.BLOCK_ENTITY_TYPE, Forestry.id(id), FabricBlockEntityTypeBuilder.create(factory, ArrayUtils.add(blocks, block)).build());
    }
}
