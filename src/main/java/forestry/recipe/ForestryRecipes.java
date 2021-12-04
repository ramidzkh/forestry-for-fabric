package forestry.recipe;

import forestry.Forestry;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public interface ForestryRecipes {

    Type<CarpenterRecipe> CARPENTER = create("carpenter", new CarpenterRecipe.Serializer());

    static void initialize() {
        // Done in class initialization
    }

    record Type<T extends Recipe<?>>(RecipeType<T> type, RecipeSerializer<T> serializer) {
    }

    private static <T extends Recipe<?>> Type<T> create(String id, RecipeSerializer<T> serializer) {
        RecipeType<T> type = new RecipeType<>() {
            @Override
            public String toString() {
                return id;
            }
        };

        Identifier identifier = Forestry.id(id);
        Registry.register(Registry.RECIPE_TYPE, identifier, type);
        Registry.register(Registry.RECIPE_SERIALIZER, identifier, serializer);

        return new Type<>(type, serializer);
    }
}
