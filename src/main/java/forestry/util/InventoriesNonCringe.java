package forestry.util;

import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.collection.DefaultedList;

public class InventoriesNonCringe {

    public static NbtCompound write(NbtCompound compound, Inventory inventory) {
        DefaultedList<ItemStack> list = DefaultedList.ofSize(inventory.size(), ItemStack.EMPTY);

        for (int i = 0; i < inventory.size(); i++) {
            list.set(i, inventory.getStack(i));
        }

        return Inventories.writeNbt(compound, list);
    }

    public static void read(NbtCompound compound, Inventory inventory) {
        DefaultedList<ItemStack> list = DefaultedList.ofSize(inventory.size(), ItemStack.EMPTY);
        Inventories.readNbt(compound, list);

        for (int i = 0; i < inventory.size(); i++) {
            inventory.setStack(i, list.get(i));
        }
    }
}
