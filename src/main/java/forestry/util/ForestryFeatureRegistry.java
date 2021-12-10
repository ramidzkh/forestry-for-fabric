package forestry.util;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.*;
import java.util.function.Function;

public record ForestryFeatureRegistry(Identifier prefix) implements FeatureRegistry {

    private Identifier id(String id) {
        if (prefix.getPath().isEmpty()) {
            return new Identifier(prefix.getNamespace(), id.toLowerCase(Locale.ROOT));
        } else {
            return new Identifier(prefix.getNamespace(), prefix.getPath() + "/" + id.toLowerCase(Locale.ROOT));
        }
    }

    @Override
    public Item item(String id) {
        return Registry.register(Registry.ITEM, id(id), new Item(new Item.Settings()));
    }

    @Override
    public <T extends Item> T item(String id, T value) {
        return Registry.register(Registry.ITEM, id(id), value);
    }

    @Override
    public <T extends Item, V> Variants<T, V> itemGroup(String base, Function<V, T> producer, V... variants) {
        Map<V, T> map = new HashMap<>();

        if (!base.isEmpty()) {
            base += "/";
        }

        for (V variant : variants) {
            map.put(variant, item(base + variant.toString(), producer.apply(variant)));
        }

        return new Variants<>(map);
    }

    @Override
    public Block block(String id) {
        return block(true, id);
    }

    @Override
    public <T extends Block> T block(String id, T value) {
        return block(true, id, value);
    }

    @Override
    public <T extends Block, V> Variants<T, V> blockGroup(String base, Function<V, T> producer, V... variants) {
        return blockGroup(true, base, producer, variants);
    }

    @Override
    public Block blockWithoutItem(String id) {
        return block(false, id);
    }

    @Override
    public <T extends Block> T blockWithoutItem(String id, T value) {
        return block(false, id, value);
    }

    @Override
    public <T extends Block, V> Variants<T, V> blockGroupWithoutItem(String base, Function<V, T> producer, V... variants) {
        return blockGroup(false, base, producer, variants);
    }

    @Override
    public void freeze() {
        // TODO: Freeze this feature registry. We also could register here instead
    }

    private Block block(boolean item, String id) {
        Block block = Registry.register(Registry.BLOCK, id(id), new Block(FabricBlockSettings.of(Material.METAL)));

        if (item) {
            Registry.register(Registry.ITEM, id(id), new BlockItem(block, new Item.Settings()));
        }

        return block;
    }

    private <T extends Block> T block(boolean item, String id, T value) {
        Registry.register(Registry.BLOCK, id(id), value);

        if (item) {
            Registry.register(Registry.ITEM, id(id), new BlockItem(value, new Item.Settings()));
        }

        return value;
    }

    private <T extends Block, V> Variants<T, V> blockGroup(boolean item, String base, Function<V, T> producer, V[] variants) {
        Map<V, T> map = new HashMap<>();

        if (!base.isEmpty()) {
            base += "/";
        }

        for (V variant : variants) {
            map.put(variant, block(item, base + variant.toString(), producer.apply(variant)));
        }

        return new Variants<>(map);
    }

    private record Variants<T, V>(Map<V, T> map) implements FeatureRegistry.Variants<T, V> {
        @Override
        public T get(V variant) {
            return Objects.requireNonNull(map.get(variant), "Variant " + variant + " not present");
        }

        @Override
        public Collection<V> getVariants() {
            return map.keySet();
        }

        @Override
        public Collection<T> getInstances() {
            return map.values();
        }
    }
}
