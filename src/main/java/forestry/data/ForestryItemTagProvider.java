package forestry.data;

import forestry.feature.core.CoreFeatures;
import forestry.feature.core.ResourceType;
import forestry.item.ForestryTags;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import org.jetbrains.annotations.Nullable;

public class ForestryItemTagProvider extends FabricTagProvider.ItemTagProvider {

    public ForestryItemTagProvider(FabricDataGenerator dataGenerator, @Nullable BlockTagProvider blockTagProvider) {
        super(dataGenerator, blockTagProvider);
    }

    @Override
    protected void generateTags() {
        copy(ForestryTags.TIN_ORES, ForestryTags.TIN_ORES_ITEM);
        copy(ForestryTags.BRONZE_ORES, ForestryTags.BRONZE_ORES_ITEM);
        copy(ForestryTags.APATITE_ORES, ForestryTags.APATITE_ORES_ITEM);
        copy(ForestryTags.TIN_BLOCKS, ForestryTags.TIN_BLOCKS_ITEM);
        copy(ForestryTags.BRONZE_BLOCKS, ForestryTags.BRONZE_BLOCKS_ITEM);
        copy(ForestryTags.APATITE_BLOCKS, ForestryTags.APATITE_BLOCKS_ITEM);

        getOrCreateTagBuilder(ForestryTags.TIN_INGOTS).add(CoreFeatures.RESOURCE_INGOT.get(ResourceType.TIN));
        getOrCreateTagBuilder(ForestryTags.BRONZE_INGOTS).add(CoreFeatures.RESOURCE_INGOT.get(ResourceType.BRONZE));
        getOrCreateTagBuilder(ForestryTags.TIN_INGOTS).add(CoreFeatures.RESOURCE_INGOT.get(ResourceType.APATITE));
    }
}
