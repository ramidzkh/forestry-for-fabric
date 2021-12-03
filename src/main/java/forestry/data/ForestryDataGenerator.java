package forestry.data;

import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;

public class ForestryDataGenerator {

    public static void initialize(FabricDataGenerator dataGenerator) {
        dataGenerator.addProvider(ForestryRecipeProvider::new);
    }
}
