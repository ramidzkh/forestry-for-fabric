package forestry;

import forestry.recipe.ForestryRecipes;
import net.minecraft.util.Identifier;

public class Forestry {

    public static final String MOD_ID = "forestry";

    public static Identifier id(String path) {
        return new Identifier(MOD_ID, path);
    }

    public static Identifier id(String... path) {
        return new Identifier(MOD_ID, String.join("/", path));
    }

    public static void initialize() {
        ForestryRecipes.initialize();
    }
}
