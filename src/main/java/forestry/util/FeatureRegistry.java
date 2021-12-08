package forestry.util;

import net.minecraft.block.Block;
import net.minecraft.item.Item;

import java.util.function.Function;

public interface FeatureRegistry {

    Item item(String id);

    <T extends Item> T item(String id, T value);

    <T extends Item, V> Variants<T, V> itemGroup(String base, Function<V, T> producer, V... variants);

    Block block(String id);

    <T extends Block> T block(String id, T value);

    <T extends Block, V> Variants<T, V> blockGroup(String base, Function<V, T> producer, V... variants);

    Block blockWithoutItem(String id);

    <T extends Block> T blockWithoutItem(String id, T value);

    <T extends Block, V> Variants<T, V> blockGroupWithoutItem(String base, Function<V, T> producer, V... variants);

    void freeze();

    interface Variants<T, V> {
        T get(V variant);
    }
}
