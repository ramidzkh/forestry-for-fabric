package forestry.data.factory;

import com.google.gson.JsonObject;
import forestry.recipe.ForestryRecipes;
import forestry.util.RecipeSerializers;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.minecraft.advancement.criterion.ImpossibleCriterion;
import net.minecraft.data.server.recipe.RecipeJsonProvider;
import net.minecraft.data.server.recipe.ShapedRecipeJsonFactory;
import net.minecraft.data.server.recipe.ShapelessRecipeJsonFactory;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public class CarpenterRecipeJsonFactory {

    private int packagingTime;
    private FluidVariant fluid;
    private long fluidQuantity;
    private Ingredient box;
    private RecipeJsonProvider recipe;
    @Nullable
    private ItemStack result;

    public CarpenterRecipeJsonFactory setPackagingTime(int packagingTime) {
        this.packagingTime = packagingTime;
        return this;
    }

    public CarpenterRecipeJsonFactory setFluid(FluidVariant fluid, long fluidQuantity) {
        this.fluid = fluid;
        this.fluidQuantity = fluidQuantity;
        return this;
    }

    public CarpenterRecipeJsonFactory setBox(Ingredient box) {
        this.box = box;
        return this;
    }

    public CarpenterRecipeJsonFactory recipe(ShapedRecipeJsonFactory recipe) {
        Holder<RecipeJsonProvider> holder = new Holder<>();
        recipe.criterion("impossible", new ImpossibleCriterion.Conditions()).offerTo(holder::set);
        this.recipe = holder.get();
        return this;
    }

    public CarpenterRecipeJsonFactory recipe(ShapelessRecipeJsonFactory recipe) {
        Holder<RecipeJsonProvider> holder = new Holder<>();
        recipe.criterion("impossible", new ImpossibleCriterion.Conditions()).offerTo(holder::set);
        this.recipe = holder.get();
        return this;
    }

    /**
     * In case the recipe should create an item stack, and not a basic item without NBT
     *
     * @param result The result to override {@link #recipe(ShapelessRecipeJsonFactory)}
     * @return This builder for chaining
     */
    public CarpenterRecipeJsonFactory override(ItemStack result) {
        this.result = result;
        return this;
    }

    public void build(Consumer<RecipeJsonProvider> consumer, Identifier id) {
        consumer.accept(new Result(id, packagingTime, fluid, fluidQuantity, box, recipe, result));
    }

    private record Result(Identifier id, int packagingTime,
                          FluidVariant fluid, long fluidQuantity,
                          Ingredient box, RecipeJsonProvider recipe,
                          @Nullable ItemStack result) implements RecipeJsonProvider {
        public Result(Identifier id, int packagingTime, FluidVariant fluid, long fluidQuantity, Ingredient box, RecipeJsonProvider recipe, @Nullable ItemStack result) {
            this.id = id;
            this.packagingTime = packagingTime;
            this.fluid = fluid;
            this.fluidQuantity = fluidQuantity;
            this.box = box;
            this.recipe = recipe;
            this.result = result;
        }

        @Override
        public void serialize(JsonObject json) {
            json.addProperty("time", packagingTime);

            if (fluid != null) {
                json.add("fluid", RecipeSerializers.serializeFluid(fluid));
                json.addProperty("fluidQuantity", fluidQuantity);
            }

            json.add("box", box.toJson());
            json.add("recipe", recipe.toJson());

            if (result != null) {
                json.add("result", RecipeSerializers.item(result));
            }
        }

        @Override
        public Identifier getRecipeId() {
            return id;
        }

        @Override
        public RecipeSerializer<?> getSerializer() {
            return ForestryRecipes.CARPENTER.serializer();
        }

        @Nullable
        @Override
        public JsonObject toAdvancementJson() {
            return null;
        }

        @Override
        public Identifier getAdvancementId() {
            return null;
        }
    }
}
