package forestry.data;

import forestry.data.builder.CarpenterRecipeJsonFactory;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipesProvider;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.minecraft.data.server.recipe.RecipeJsonProvider;
import net.minecraft.data.server.recipe.ShapedRecipeJsonFactory;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.Items;
import net.minecraft.recipe.Ingredient;

import java.util.function.Consumer;

import static forestry.Forestry.id;

public class ForestryMachineRecipeProvider extends FabricRecipesProvider {

    public ForestryMachineRecipeProvider(FabricDataGenerator dataGenerator) {
        super(dataGenerator);
    }

    @Override
    protected void generateRecipes(Consumer<RecipeJsonProvider> exporter) {
        new CarpenterRecipeJsonFactory()
            .setPackagingTime(5)
            .setFluid(FluidVariant.of(Fluids.WATER), 81000)
            .setBox(Ingredient.EMPTY)
            .recipe(ShapedRecipeJsonFactory.create(Items.OAK_BOAT, 2)
                .pattern("#S#")
                .pattern("###")
                .input('#', Items.OAK_LOG)
                .input('S', Items.WOODEN_SHOVEL))
            .build(exporter, id("carpenter", "example"));
    }
}
