package forestry.block.entity;

import forestry.util.Storages;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidConstants;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidStorage;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.item.InventoryStorage;
import net.fabricmc.fabric.api.transfer.v1.item.ItemStorage;
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.Storage;
import net.fabricmc.fabric.api.transfer.v1.storage.base.CombinedStorage;
import net.fabricmc.fabric.api.transfer.v1.storage.base.FilteringStorage;
import net.fabricmc.fabric.api.transfer.v1.storage.base.SingleVariantStorage;
import net.fabricmc.fabric.api.util.NbtType;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import team.reborn.energy.api.EnergyStorage;
import team.reborn.energy.api.base.SimpleEnergyStorage;

import java.util.List;

public class CarpenterBlockEntity extends MachineBlockEntity {

    private final SimpleInventory inputs = new SimpleInventory(18);
    private final SimpleInventory outputs = new SimpleInventory(1);

    private final Storage<ItemVariant> inputStorage = InventoryStorage.of(inputs, null);
    private final Storage<ItemVariant> outputStorage = InventoryStorage.of(outputs, null);
    private final SingleVariantStorage<FluidVariant> fluid = Storages.fluidStorage(10 * FluidConstants.BUCKET);
    private final SimpleEnergyStorage energyStorage = new SimpleEnergyStorage(1000, 100, 0);
    private final Storage<ItemVariant> exposedItem = new CombinedStorage<>(List.of(
        FilteringStorage.insertOnlyOf(inputStorage),
        FilteringStorage.extractOnlyOf(outputStorage)
    ));
    private final Storage<FluidVariant> exposedFluid = FilteringStorage.insertOnlyOf(fluid);

    public CarpenterBlockEntity(BlockEntityType<? extends CarpenterBlockEntity> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    public CarpenterBlockEntity(BlockPos pos, BlockState state) {
        this(ForestryBlockEntities.CARPENTER, pos, state);
    }

    public static void initialize() {
        // TODO: In-game configurability, thermal style?
        ItemStorage.SIDED.registerForBlockEntity((blockEntity, direction) -> {
            return blockEntity.exposedItem;
        }, ForestryBlockEntities.CARPENTER);

        FluidStorage.SIDED.registerForBlockEntity((blockEntity, direction) -> {
            return blockEntity.exposedFluid;
        }, ForestryBlockEntities.CARPENTER);

        EnergyStorage.SIDED.registerForBlockEntity((blockEntity, direction) -> {
            return blockEntity.energyStorage;
        }, ForestryBlockEntities.CARPENTER);
    }

    @Override
    public void tick(World world, BlockPos blockPos, BlockState blockState) {
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        inputs.readNbtList(nbt.getList("Inputs", NbtType.COMPOUND));
        outputs.readNbtList(nbt.getList("Outputs", NbtType.COMPOUND));
        fluid.variant = FluidVariant.fromNbt(nbt.getCompound("Fluid"));
        fluid.amount = nbt.getLong("FluidAmount");
        energyStorage.amount = nbt.getLong("EnergyAmount");
    }

    @Override
    protected void writeNbt(NbtCompound nbt) {
        nbt.put("Inputs", inputs.toNbtList());
        nbt.put("Outputs", outputs.toNbtList());
        nbt.put("Fluid", fluid.variant.toNbt());
        nbt.putLong("FluidAmount", fluid.amount);
        nbt.putLong("EnergyAmount", energyStorage.amount);
    }
}
