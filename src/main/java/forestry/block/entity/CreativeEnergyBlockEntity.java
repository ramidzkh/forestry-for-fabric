package forestry.block.entity;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;

public class CreativeEnergyBlockEntity extends BlockEntity {

    public CreativeEnergyBlockEntity(BlockPos pos, BlockState state) {
        super(ForestryBlockEntities.CREATIVE_ENERGY, pos, state);
    }
}
