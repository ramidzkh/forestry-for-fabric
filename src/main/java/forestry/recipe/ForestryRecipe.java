package forestry.recipe;

import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;

public interface ForestryRecipe extends Recipe<Inventory> {

    // <editor-fold desc="Ignore these methods, we just piggyback off Minecraft's system for recipe sync">
    @Deprecated
    @Override
    default boolean matches(Inventory inv, World worldIn) {
        return false;
    }

    @Deprecated
    @Override
    default ItemStack craft(Inventory inventory) {
        return ItemStack.EMPTY;
    }

    @Deprecated
    @Override
    default boolean fits(int width, int height) {
        return false;
    }

    @Deprecated
    @Override
    default ItemStack getOutput() {
        return ItemStack.EMPTY;
    }

    @Deprecated
    @Override
    default DefaultedList<ItemStack> getRemainder(Inventory inv) {
        return DefaultedList.of();
    }

    @Deprecated
    @Override
    default DefaultedList<Ingredient> getIngredients() {
        return DefaultedList.of();
    }

    @Deprecated
    @Override
    default boolean isIgnoredInRecipeBook() {
        return true;
    }

    @Deprecated
    @Override
    default String getGroup() {
        return "forestry";
    }

    @Override
    default ItemStack createIcon() {
        return ItemStack.EMPTY;
    }
    // </editor-fold>

    @Override
    Identifier getId();

    ForestryRecipes.Type<?> getRecipeType();

    @Override
    default RecipeSerializer<?> getSerializer() {
        return getRecipeType().serializer();
    }

    @Override
    default RecipeType<?> getType() {
        return getRecipeType().type();
    }
}
