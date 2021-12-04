package forestry.block.entity;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.ApiStatus;

public abstract class MachineBlockEntity extends BlockEntity {

    public MachineBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @ApiStatus.OverrideOnly
    public void tick(World world, BlockPos blockPos, BlockState blockState) {
    }
}
