package forestry.block;

import forestry.block.entity.MachineBlockEntity;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public abstract class MachineBlock<T extends MachineBlockEntity> extends BlockWithEntity {

    private final BlockEntityType<T> type;

    protected MachineBlock(Settings settings, BlockEntityType<T> type) {
        super(settings);
        this.type = type;
    }

    public BlockEntityType<T> getType() {
        return type;
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return getType().instantiate(pos, state);
    }

    @Nullable
    @Override
    public <B extends BlockEntity> BlockEntityTicker<B> getTicker(World world, BlockState state, BlockEntityType<B> type) {
        return world.isClient() ? null : checkType(type, getType(), (w, blockPos, blockState, entity) -> {
            entity.tick(w, blockPos, blockState);
        });
    }
}
