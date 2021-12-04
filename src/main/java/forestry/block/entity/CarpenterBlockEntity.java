package forestry.block.entity;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.math.BlockPos;

public class CarpenterBlockEntity extends MachineBlockEntity {

    public CarpenterBlockEntity(BlockEntityType<? extends CarpenterBlockEntity> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    public CarpenterBlockEntity(BlockPos pos, BlockState state) {
        this(ForestryBlockEntities.CARPENTER, pos, state);
    }
}
