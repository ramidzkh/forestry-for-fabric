package forestry.data;

import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;

public class ForestryDataGenerator {

    public static void initialize(FabricDataGenerator dataGenerator) {
        ForestryBlockTagProvider blockTagProvider = dataGenerator.addProvider(ForestryBlockTagProvider::new);
        dataGenerator.addProvider(new ForestryItemTagProvider(dataGenerator, blockTagProvider));
        dataGenerator.addProvider(ForestryMachineRecipeProvider::new);
        dataGenerator.addProvider(ForestryRecipeProvider::new);
    }
}
