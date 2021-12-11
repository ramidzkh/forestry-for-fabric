package forestry.block.entity;

import net.fabricmc.fabric.api.transfer.v1.fluid.FluidStorage;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.item.ItemStorage;
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.Storage;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.Packet;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;
import team.reborn.energy.api.EnergyStorage;

@SuppressWarnings("UnstableApiUsage")
public abstract class MachineBlockEntity extends BlockEntity {

    public MachineBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    public static void initialize() {
        ItemStorage.SIDED.registerFallback((world, pos, state, blockEntity, context) -> {
            if (blockEntity instanceof MachineBlockEntity machineBlock) {
                return machineBlock.getItemStorage(context);
            } else {
                return null;
            }
        });

        FluidStorage.SIDED.registerFallback((world, pos, state, blockEntity, context) -> {
            if (blockEntity instanceof MachineBlockEntity machineBlock) {
                return machineBlock.getFluidStorage(context);
            } else {
                return null;
            }
        });

        EnergyStorage.SIDED.registerFallback((world, pos, state, blockEntity, context) -> {
            if (blockEntity instanceof MachineBlockEntity machineBlock) {
                return machineBlock.getEnergyStorage(context);
            } else {
                return null;
            }
        });
    }

    @ApiStatus.OverrideOnly
    protected Storage<ItemVariant> getItemStorage(Direction direction) {
        return null;
    }

    @ApiStatus.OverrideOnly
    protected Storage<FluidVariant> getFluidStorage(Direction direction) {
        return null;
    }

    @ApiStatus.OverrideOnly
    protected EnergyStorage getEnergyStorage(Direction direction) {
        return null;
    }

    @ApiStatus.OverrideOnly
    public void tick(World world, BlockPos blockPos, BlockState blockState) {
    }

    @Nullable
    @Override
    public Packet<ClientPlayPacketListener> toUpdatePacket() {
        return BlockEntityUpdateS2CPacket.create(this);
    }

    @Override
    public NbtCompound toInitialChunkDataNbt() {
        NbtCompound nbt = super.toInitialChunkDataNbt();
        writeNbt(nbt);
        return nbt;
    }

    public void sync() {
        if (world instanceof ServerWorld serverWorld) {
            serverWorld.getChunkManager().markForUpdate(getPos());
        } else {
            throw new RuntimeException("Tried syncing on client side");
        }
    }

    @ApiStatus.OverrideOnly
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (openGui(player)) {
            return ActionResult.SUCCESS;
        }

        return ActionResult.PASS;
    }

    @ApiStatus.OverrideOnly
    protected boolean openGui(PlayerEntity player) {
        return false;
    }
}
