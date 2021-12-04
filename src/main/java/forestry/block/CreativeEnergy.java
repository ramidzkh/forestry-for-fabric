package forestry.block;

import forestry.block.entity.ForestryBlockEntities;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import team.reborn.energy.api.EnergyStorage;

public class CreativeEnergy extends BlockWithEntity {

    public CreativeEnergy(Settings settings) {
        super(settings);
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return ForestryBlockEntities.CREATIVE_ENERGY.instantiate(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return world.isClient ? null : checkType(type, ForestryBlockEntities.CREATIVE_ENERGY, (w, pos, s, blockEntity) -> {
            for (Direction direction : Direction.values()) {
                EnergyStorage storage = EnergyStorage.SIDED.find(w, pos.offset(direction), direction);

                if (storage != null) {
                    try (Transaction transaction = Transaction.openOuter()) {
                        storage.insert(Long.MAX_VALUE, transaction);
                        transaction.commit();
                    }
                }
            }
        });
    }
}
