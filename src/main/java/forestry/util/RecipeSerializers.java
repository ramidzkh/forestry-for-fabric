package forestry.util;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.serialization.Dynamic;
import com.mojang.serialization.JsonOps;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.Ingredient;
import net.minecraft.util.collection.DefaultedList;

import java.util.function.BiConsumer;
import java.util.function.Function;

public interface RecipeSerializers {

    static <E> void write(PacketByteBuf buffer, DefaultedList<E> list, BiConsumer<PacketByteBuf, E> consumer) {
        buffer.writeVarInt(list.size());

        for (E e : list) {
            consumer.accept(buffer, e);
        }
    }

    static <E> DefaultedList<E> read(PacketByteBuf buffer, Function<PacketByteBuf, E> reader) {
        DefaultedList<E> list = DefaultedList.of();
        int size = buffer.readVarInt();

        for (int i = 0; i < size; i++) {
            list.add(reader.apply(buffer));
        }

        return list;
    }

    static FluidVariant deserializeFluid(JsonObject object) {
        return FluidVariant.fromNbt((NbtCompound) Dynamic.convert(JsonOps.INSTANCE, NbtOps.INSTANCE, object));
    }

    static JsonObject serializeFluid(FluidVariant fluid) {
        return (JsonObject) Dynamic.convert(NbtOps.INSTANCE, JsonOps.INSTANCE, fluid.toNbt());
    }

    static ItemStack item(JsonObject object) {
        return ItemStack.fromNbt((NbtCompound) Dynamic.convert(JsonOps.INSTANCE, NbtOps.INSTANCE, object));
    }

    static JsonObject item(ItemStack stack) {
        return (JsonObject) Dynamic.convert(NbtOps.INSTANCE, JsonOps.INSTANCE, stack.writeNbt(new NbtCompound()));
    }

    static Ingredient deserialize(JsonElement resource) {
        if (resource.isJsonArray() && resource.getAsJsonArray().size() == 0) {
            return Ingredient.EMPTY;
        }

        return Ingredient.fromJson(resource);
    }
}
