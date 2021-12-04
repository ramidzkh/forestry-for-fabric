package forestry.recipe;

import com.google.gson.JsonObject;
import forestry.util.RecipeSerializers;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.packet.s2c.play.SynchronizeRecipesS2CPacket;
import net.minecraft.recipe.CraftingRecipe;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeManager;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;

public record CarpenterRecipe(Identifier id, int packagingTime,
                              FluidVariant fluid, long fluidAmount,
                              Ingredient box, CraftingRecipe recipe,
                              ItemStack output) implements ForestryRecipe {
    @Override
    public Identifier getId() {
        return id;
    }

    @Override
    public ForestryRecipes.Type<?> getRecipeType() {
        return ForestryRecipes.CARPENTER;
    }

    static class Serializer implements RecipeSerializer<CarpenterRecipe> {

        @Override
        public CarpenterRecipe read(Identifier recipeId, JsonObject json) {
            int packagingTime = JsonHelper.getInt(json, "time");
            FluidVariant fluid = json.has("fluid") ? RecipeSerializers.deserializeFluid(JsonHelper.getObject(json, "fluid")) : FluidVariant.blank();
            long fluidAmount = JsonHelper.getLong(json, "fluidAmount");
            Ingredient box = RecipeSerializers.deserialize(json.get("box"));
            CraftingRecipe internal = (CraftingRecipe) RecipeManager.deserialize(recipeId, JsonHelper.getObject(json, "recipe"));
            ItemStack result = json.has("result") ? RecipeSerializers.item(JsonHelper.getObject(json, "result")) : internal.getOutput();

            return new CarpenterRecipe(recipeId, packagingTime, fluid, fluidAmount, box, internal, result);
        }

        @Override
        public CarpenterRecipe read(Identifier recipeId, PacketByteBuf buffer) {
            int packagingTime = buffer.readVarInt();
            FluidVariant fluid = FluidVariant.fromPacket(buffer);
            long fluidAmount = buffer.readVarLong();
            Ingredient box = Ingredient.fromPacket(buffer);
            CraftingRecipe internal = (CraftingRecipe) SynchronizeRecipesS2CPacket.readRecipe(buffer);
            ItemStack result = buffer.readItemStack();

            return new CarpenterRecipe(recipeId, packagingTime, fluid, fluidAmount, box, internal, result);
        }

        @Override
        public void write(PacketByteBuf buffer, CarpenterRecipe recipe) {
            buffer.writeVarInt(recipe.packagingTime);
            recipe.fluid.toPacket(buffer);
            buffer.writeVarLong(recipe.fluidAmount);
            recipe.box.write(buffer);
            SynchronizeRecipesS2CPacket.writeRecipe(buffer, recipe.recipe);
            buffer.writeItemStack(recipe.output);
        }
    }
}
