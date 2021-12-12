package forestry.util;

import forestry.mixins.accessor.SimpleInventoryAccessor;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.nbt.NbtCompound;

public class InventoriesNonCringe {

    public static NbtCompound write(NbtCompound compound, SimpleInventory inventory) {
        return Inventories.writeNbt(compound, ((SimpleInventoryAccessor) inventory).getStacks());
    }

    public static void read(NbtCompound compound, SimpleInventory inventory) {
        Inventories.readNbt(compound, ((SimpleInventoryAccessor) inventory).getStacks());
    }
}
