package forestry.client.gui;

import io.github.astrarre.gui.v1.api.component.slot.SlotKey;
import io.github.astrarre.itemview.v0.fabric.ItemKey;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;

public class TemplateSlotKey extends SlotKey {

    public TemplateSlotKey(Inventory inventory, int inventoryId, int slotIndex) {
        super(inventory, inventoryId, slotIndex);
    }

    @Override
    public int extract(ItemKey key, int count, boolean simulate) {
        setStack(ItemStack.EMPTY);
        return 0;
    }

    @Override
    public int insert(ItemKey key, int count, boolean simulate) {
        setStack(key.createItemStack(1));
        return 0;
    }
}
