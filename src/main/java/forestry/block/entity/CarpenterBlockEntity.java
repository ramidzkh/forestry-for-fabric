package forestry.block.entity;

import forestry.client.gui.FluidTank;
import forestry.client.gui.OutputSlotKey;
import forestry.client.gui.TemplateSlotKey;
import forestry.client.gui.ViewSlotKey;
import forestry.recipe.CarpenterRecipe;
import forestry.recipe.ForestryRecipes;
import forestry.util.InventoriesNonCringe;
import forestry.util.Storages;
import io.github.astrarre.gui.v1.api.comms.PacketKey;
import io.github.astrarre.gui.v1.api.component.*;
import io.github.astrarre.gui.v1.api.component.slot.ASlot;
import io.github.astrarre.gui.v1.api.component.slot.SlotKey;
import io.github.astrarre.gui.v1.api.server.ServerPanel;
import io.github.astrarre.itemview.v0.fabric.FabricViews;
import io.github.astrarre.rendering.v1.api.plane.icon.Icon;
import io.github.astrarre.rendering.v1.api.plane.icon.backgrounds.ContainerBackgroundIcon;
import io.github.astrarre.rendering.v1.api.space.Transform3d;
import io.github.astrarre.rendering.v1.api.util.Axis2d;
import io.github.astrarre.util.v0.api.Val;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidConstants;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.item.InventoryStorage;
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.Storage;
import net.fabricmc.fabric.api.transfer.v1.storage.base.CombinedStorage;
import net.fabricmc.fabric.api.transfer.v1.storage.base.FilteringStorage;
import net.fabricmc.fabric.api.transfer.v1.storage.base.ResourceAmount;
import net.fabricmc.fabric.api.transfer.v1.storage.base.SingleVariantStorage;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import net.fabricmc.fabric.api.transfer.v1.transaction.TransactionContext;
import net.fabricmc.fabric.api.util.NbtType;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.InventoryChangedListener;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import team.reborn.energy.api.EnergyStorage;
import team.reborn.energy.api.base.DelegatingEnergyStorage;
import team.reborn.energy.api.base.SimpleEnergyStorage;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("UnstableApiUsage")
public class CarpenterBlockEntity extends MachineBlockEntity implements InventoryChangedListener {

    // 3x3 crafting template + 1 box slot + 1 output slot
    private final SimpleInventory template = new SimpleInventory(11);

    // 9x2 crafting inputs
    private final SimpleInventory inputs = new SimpleInventory(18);

    // 1 fluid input exchange slot
    private final SimpleInventory fluidInput = new SimpleInventory(1);

    // 1 crafting output
    private final SimpleInventory outputs = new SimpleInventory(1);

    private final Storage<ItemVariant> inputStorage = InventoryStorage.of(inputs, null);
    private final Storage<ItemVariant> outputStorage = InventoryStorage.of(outputs, null);
    private final SingleVariantStorage<FluidVariant> fluid = Storages.fluidStorage(10 * FluidConstants.BUCKET);
    private final SimpleEnergyStorage energyStorage = new SimpleEnergyStorage(1000, 100, 1000);
    private final Storage<ItemVariant> exposedItem = new CombinedStorage<>(List.of(
        FilteringStorage.insertOnlyOf(inputStorage),
        FilteringStorage.extractOnlyOf(outputStorage)
    ));
    private final Storage<FluidVariant> exposedFluid = FilteringStorage.insertOnlyOf(fluid);
    private final EnergyStorage exposedEnergy = new DelegatingEnergyStorage(energyStorage, null) {
        @Override
        public long extract(long maxAmount, TransactionContext transaction) {
            return 0;
        }
    };

    private int progress;

    public CarpenterBlockEntity(BlockEntityType<? extends CarpenterBlockEntity> type, BlockPos pos, BlockState state) {
        super(type, pos, state);

        template.addListener(this);
        inputs.addListener(this);
        fluidInput.addListener(this);
        outputs.addListener(this);
    }

    public CarpenterBlockEntity(BlockPos pos, BlockState state) {
        this(ForestryBlockEntities.CARPENTER, pos, state);
    }

    // TODO: In-game configurability, thermal style?
    @Override
    protected Storage<ItemVariant> getItemStorage(Direction direction) {
        return exposedItem;
    }

    @Override
    protected Storage<FluidVariant> getFluidStorage(Direction direction) {
        return exposedFluid;
    }

    @Override
    protected EnergyStorage getEnergyStorage(Direction direction) {
        return exposedEnergy;
    }

    @Override
    public void tick(World world, BlockPos blockPos, BlockState blockState) {
        if (world.isClient()) {
            return;
        }

        CarpenterRecipe recipe = findRecipe();

        if (recipe != null) {
            // Don't packet spam
            if (template.getStack(10) != recipe.output()) {
                template.setStack(10, recipe.output());
            }

            // Verify we have everything for this recipe, otherwise progress = 0
            try (Transaction transaction = Transaction.openOuter()) {
                for (int i = 0; i < 9; i++) {
                    ItemVariant variant = ItemVariant.of(template.getStack(i));

                    if (!variant.isBlank() && inputStorage.extract(variant, 1, transaction) != 1) {
                        progress = 0;
                        return;
                    }

                    Item remainder = variant.getItem().getRecipeRemainder();

                    if (remainder != null) {
                        if (inputStorage.insert(ItemVariant.of(remainder), 1, transaction) != 1) {
                            progress = 0;
                            return;
                        }
                    }
                }

                ItemVariant box = ItemVariant.of(template.getStack(9));

                if (!box.isBlank() && inputStorage.extract(box, 1, transaction) != 1) {
                    progress = 0;
                    return;
                }

                if (fluid.extract(recipe.fluid(), recipe.fluidAmount(), transaction) != recipe.fluidAmount()) {
                    progress = 0;
                    return;
                }

                // 5EU/tick
                if (energyStorage.extract(5, transaction) != 5) {
                    return;
                }

                // Non-transactionally increase progress, so if the output slot is blocked, progress is not lost
                if (++progress >= recipe.packagingTime()) {
                    if (outputStorage.insert(ItemVariant.of(recipe.output()), recipe.output().getCount(), transaction) == recipe.output().getCount()) {
                        transaction.commit();
                        markDirty();
                    }
                }

                // Don't overflow lol
                progress = Math.min(progress, recipe.packagingTime());
            }
        } else {
            // Don't packet spam
            if (!template.getStack(10).isEmpty()) {
                template.setStack(10, ItemStack.EMPTY);
            }

            progress = 0;
        }
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        InventoriesNonCringe.read(nbt.getCompound("Template"), template);
        inputs.readNbtList(nbt.getList("Inputs", NbtType.COMPOUND));
        fluidInput.readNbtList(nbt.getList("FluidInputs", NbtType.COMPOUND));
        outputs.readNbtList(nbt.getList("Outputs", NbtType.COMPOUND));
        fluid.variant = FluidVariant.fromNbt(nbt.getCompound("Fluid"));
        fluid.amount = nbt.getLong("FluidAmount");
        energyStorage.amount = nbt.getLong("EnergyAmount");
        progress = nbt.getInt("Progress");
    }

    @Override
    protected void writeNbt(NbtCompound nbt) {
        nbt.put("Template", InventoriesNonCringe.write(new NbtCompound(), template));
        nbt.put("Inputs", inputs.toNbtList());
        nbt.put("FluidInputs", fluidInput.toNbtList());
        nbt.put("Outputs", outputs.toNbtList());
        nbt.put("Fluid", fluid.variant.toNbt());
        nbt.putLong("FluidAmount", fluid.amount);
        nbt.putLong("EnergyAmount", energyStorage.amount);
        nbt.putInt("Progress", progress);
    }

    @Nullable
    private CarpenterRecipe findRecipe() {
        World world = getWorld();

        if (world == null) {
            return null;
        }

        CraftingInventory inventory = FakeCraftingInventory.of(template);

        for (CarpenterRecipe recipe : world.getRecipeManager().listAllOfType(ForestryRecipes.CARPENTER.type())) {
            if (recipe.recipe().matches(inventory, world) && (recipe.box().isEmpty() || recipe.box().test(template.getStack(9)))) {
                return recipe;
            }
        }

        return null;
    }

    @Override
    protected boolean openGui(PlayerEntity player) {
        List<SlotKey> playerKeys = SlotKey.player(player, 0);
        List<SlotKey> templateKeys = new ArrayList<>(10);
        List<SlotKey> inputKeys = SlotKey.inv(inputs, 2);
        SlotKey fluidInputKey = new SlotKey(fluidInput, 3, 0);
        SlotKey outputKey = new OutputSlotKey(outputs, 4, 0);
        outputKey.link(outputKey);

        for (int i = 0; i < 10; i++) {
            TemplateSlotKey key = new TemplateSlotKey(template, 1, i);
            key.link(key);
            templateKeys.add(key);
        }

        ViewSlotKey outputViewKey = new ViewSlotKey(template, 1, 10);
        outputViewKey.link(outputViewKey);
        templateKeys.add(outputViewKey);

        for (SlotKey key : playerKeys) {
            key.linkAllPre(inputKeys);
            key.linkPre(fluidInputKey);
        }

        for (SlotKey key : inputKeys) {
            key.linkAllPre(playerKeys);
        }

        fluidInputKey.linkAllPre(playerKeys);
        outputKey.linkAllPre(playerKeys);

        PacketKey.Int syncProgress = new PacketKey.Int(0);
        PacketKey.Int syncFluid = new PacketKey.Int(1);

        ServerPanel.openHandled(player, (communication, panel) -> {
            panel.darkBackground(true);

            panel.add(new ACenteringPanel(panel) {{
                add(new AIcon(new ContainerBackgroundIcon(176, 218)));
                add(new AList(Axis2d.Y) {{
                    add(new AList(Axis2d.X) {{
                        add(new AGrid(18, 3, 3) {{
                            for (SlotKey key : templateKeys.subList(0, 9)) {
                                add(new ASlot(communication, panel, key));
                            }
                        }});

                        // TODO: Arrow

                        add(new AList(Axis2d.Y) {{
                            add(new ASlot(communication, panel, templateKeys.get(9)));
                            // TODO: Arrow
                            add(new APanel() {{
                                add(new AIcon(Icon.slot(32, 28)));
                                add(new ASlot(communication, panel, outputViewKey)
                                    .with(Transform3d.translate(6, 7, 0)));
                                add(new AIcon(Icon.color(0, 0, 0)) {{
                                    // CarpenterBlockEntity$1$1$1$2$1$1.class :help_me:
                                    communication.listen(syncProgress, view -> setIcon(Icon.color(0xFFEFF000, 2, view.getFloat("progress") * 18)));
                                }});
                            }});
                        }}.with(Transform3d.translate(12, 0, 0)));

                        // TODO: Arrow

                        add(new AList(Axis2d.Y) {{
                            add(new ASlot(communication, panel, fluidInputKey));
                            add(new ASlot(communication, panel, outputKey));
                        }});

                        // TODO: Arrow
                        add(new FluidTank(fluid.getCapacity(), new ResourceAmount<>(fluid.variant, fluid.amount)) {{
                            setBounds(20, 60);
                            communication.listen(syncFluid, view -> setFluid(new ResourceAmount<>(FluidVariant.fromNbt(view.getTag("Fluid").toTag()), view.getLong("Amount"))));
                        }});
                    }});

                    add(new AGrid(18, 9, 2) {{
                        for (SlotKey key : inputKeys) {
                            add(new ASlot(communication, panel, key));
                        }
                    }}.with(Transform3d.translate(0, 9, 0)));

                    add(ASlot.playerInv(communication, panel, playerKeys).with(Transform3d.translate(0, 9, 0)));
                }}.with(Transform3d.translate(0, 19, 0)));
            }});
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

            fluidInputKey.sync(communication, panel);
            outputKey.sync(communication, panel);

            Val<Float> progressTracker = new Val<>();
            Val<ResourceAmount<FluidVariant>> fluidTracker = new Val<>();

            progressTracker.addListener((old, current) -> communication.sendInfo(syncProgress, x -> x.putFloat("progress", current)));
            fluidTracker.addListener((old, current) -> communication.sendInfo(syncFluid, x -> x.putTag("Fluid", FabricViews.view(fluid.variant.toNbt())).putLong("Amount", fluid.amount)));

            panel.addTickListener(() -> {
                CarpenterRecipe recipe = findRecipe();

                if (recipe != null) {
                    progressTracker.set(progress / (float) recipe.packagingTime());
                } else {
                    progressTracker.set(0F);
                }

                fluidTracker.set(new ResourceAmount<>(fluid.variant, fluid.amount));
            });
        });

        return true;
    }

    @Override
    public void onInventoryChanged(Inventory sender) {
        if (getWorld() instanceof ServerWorld) {
            markDirty();
        }
    }

    private static class FakeCraftingInventory {
        private static final ScreenHandler EMPTY_CONTAINER = new ScreenHandler(null, -1) {
            @Override
            public boolean canUse(PlayerEntity player) {
                return true;
            }
        };

        public static CraftingInventory of(Inventory backing) {
            CraftingInventory inventory = new CraftingInventory(EMPTY_CONTAINER, 3, 3);

            for (int i = 0; i < 9; i++) {
                inventory.setStack(i, backing.getStack(i));
            }

            return inventory;
        }
    }
}
