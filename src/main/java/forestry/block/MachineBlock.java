package forestry.block;

import forestry.block.entity.MachineBlockEntity;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public abstract class MachineBlock<T extends MachineBlockEntity> extends BlockWithEntity {

    protected MachineBlock(Settings settings) {
        super(settings);
    }

    public abstract BlockEntityType<T> getType();

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

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (world.getBlockEntity(pos) instanceof MachineBlockEntity machineBlock) {
            return machineBlock.onUse(state, world, pos, player, hand, hit);
        } else {
            return ActionResult.FAIL;
        }
    }
}
