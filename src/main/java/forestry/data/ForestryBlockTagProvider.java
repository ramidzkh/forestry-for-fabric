package forestry.data;

import forestry.feature.core.CoreFeatures;
import forestry.feature.core.ResourceType;
import forestry.item.ForestryTags;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;

public class ForestryBlockTagProvider extends FabricTagProvider.BlockTagProvider {

    public ForestryBlockTagProvider(FabricDataGenerator dataGenerator) {
        super(dataGenerator);
    }

    @Override
    protected void generateTags() {
        getOrCreateTagBuilder(ForestryTags.TIN_ORES).add(CoreFeatures.RESOURCE_ORE.get(ResourceType.TIN));
        getOrCreateTagBuilder(ForestryTags.BRONZE_ORES).add(CoreFeatures.RESOURCE_ORE.get(ResourceType.BRONZE));
        getOrCreateTagBuilder(ForestryTags.APATITE_ORES).add(CoreFeatures.RESOURCE_ORE.get(ResourceType.APATITE));

        getOrCreateTagBuilder(ForestryTags.TIN_BLOCKS).add(CoreFeatures.RESOURCE_BLOCK.get(ResourceType.TIN));
        getOrCreateTagBuilder(ForestryTags.BRONZE_BLOCKS).add(CoreFeatures.RESOURCE_BLOCK.get(ResourceType.BRONZE));
        getOrCreateTagBuilder(ForestryTags.APATITE_BLOCKS).add(CoreFeatures.RESOURCE_BLOCK.get(ResourceType.APATITE));
    }
}
