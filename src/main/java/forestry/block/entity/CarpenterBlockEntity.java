package forestry.block.entity;

import forestry.util.Storages;
import io.github.astrarre.gui.v1.api.component.ACenteringPanel;
import io.github.astrarre.gui.v1.api.component.AGrid;
import io.github.astrarre.gui.v1.api.component.AIcon;
import io.github.astrarre.gui.v1.api.component.AList;
import io.github.astrarre.gui.v1.api.component.slot.ASlot;
import io.github.astrarre.gui.v1.api.component.slot.SlotKey;
import io.github.astrarre.gui.v1.api.server.ServerPanel;
import io.github.astrarre.itemview.v0.fabric.ItemKey;
import io.github.astrarre.rendering.v1.api.plane.icon.backgrounds.ContainerBackgroundIcon;
import io.github.astrarre.rendering.v1.api.space.Transform3d;
import io.github.astrarre.rendering.v1.api.util.Axis2d;
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
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import team.reborn.energy.api.EnergyStorage;
import team.reborn.energy.api.base.SimpleEnergyStorage;

import java.util.ArrayList;
import java.util.List;

public class CarpenterBlockEntity extends MachineBlockEntity {

    // 3x3 crafting template + 1 box slot
    private final SimpleInventory template = new SimpleInventory(10);

    // 9x2 crafting inputs
    private final SimpleInventory inputs = new SimpleInventory(18);

    // 1 fluid input exchange slot
    private final SimpleInventory fluidInputs = new SimpleInventory(1);

    // 1 crafting output
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
        template.readNbtList(nbt.getList("Template", NbtType.COMPOUND));
        inputs.readNbtList(nbt.getList("Inputs", NbtType.COMPOUND));
        fluidInputs.readNbtList(nbt.getList("FluidInputs", NbtType.COMPOUND));
        outputs.readNbtList(nbt.getList("Outputs", NbtType.COMPOUND));
        fluid.variant = FluidVariant.fromNbt(nbt.getCompound("Fluid"));
        fluid.amount = nbt.getLong("FluidAmount");
        energyStorage.amount = nbt.getLong("EnergyAmount");
    }

    @Override
    protected void writeNbt(NbtCompound nbt) {
        nbt.put("Template", template.toNbtList());
        nbt.put("Inputs", inputs.toNbtList());
        nbt.put("FluidInputs", fluidInputs.toNbtList());
        nbt.put("Outputs", outputs.toNbtList());
        nbt.put("Fluid", fluid.variant.toNbt());
        nbt.putLong("FluidAmount", fluid.amount);
        nbt.putLong("EnergyAmount", energyStorage.amount);
    }

    @Override
    protected boolean openGui(PlayerEntity player) {
        List<SlotKey> playerKeys = SlotKey.player(player, 0);
        List<SlotKey> templateKeys = new ArrayList<>(9);
        List<SlotKey> inputKeys = SlotKey.inv(inputs, 2);

        for (int i = 0; i < 9; i++) {
            TemplateSlotKey e = new TemplateSlotKey(1, i);
            templateKeys.add(e);
            e.link(e);
        }

        for (SlotKey key : playerKeys) {
            key.linkAllPre(inputKeys);
        }

        for (SlotKey key : inputKeys) {
            key.linkAllPre(playerKeys);
        }

        ServerPanel.openHandled(player, (communication, panel) -> {
            panel.darkBackground(true);

            ACenteringPanel center = new ACenteringPanel(panel);
            panel.add(center);

            center.add(new AIcon(new ContainerBackgroundIcon(175, 217)));

            AList everything = new AList(Axis2d.Y);

            {
                AList top = new AList(Axis2d.X);

                {
                    AGrid plan = new AGrid(18, 3, 3);

                    for (SlotKey key : templateKeys) {
                        plan.add(new ASlot(communication, panel, key));
                    }

                    top.add(plan);
                }

                everything.add(top);
            }

            {
                AGrid inputs = new AGrid(18, 9, 2);

                for (SlotKey key : inputKeys) {
                    inputs.add(new ASlot(communication, panel, key));
                }

                everything.add(inputs
                    .with(Transform3d.translate(0, 9, 0)));
            }

            {
                everything.add(ASlot.playerInv(communication, panel, playerKeys)
                    .with(Transform3d.translate(0, 9, 0)));
            }

            center.add(everything.with(Transform3d.translate(6, 15, 0)));
        }, (communication, panel) -> {
            for (SlotKey key : playerKeys) {
                key.sync(communication, panel);
            }

            for (SlotKey key : templateKeys) {
                key.sync(communication, panel);
            }

            for (SlotKey key : inputKeys) {
                key.sync(communication, panel);
            }
        });

        return true;
    }

    private class TemplateSlotKey extends SlotKey {
        private final int slotIndex;

        /**
         * @param inventoryId a unique int id for this inventory, this is used to link the inventory to it's serverside counterpart
         * @param slotIndex   the slot (index in inventory) this slot represents
         */
        public TemplateSlotKey(int inventoryId, int slotIndex) {
            super(template, inventoryId, slotIndex);
            this.slotIndex = slotIndex;
        }

        @Override
        public int extract(ItemKey key, int count, boolean simulate) {
            return 0;
        }

        @Override
        public int insert(ItemKey key, int count, boolean simulate) {
            template.setStack(slotIndex, key.createItemStack(1));
            return 0;
        }
    }
}
