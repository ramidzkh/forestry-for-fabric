package forestry.item;

import net.fabricmc.fabric.api.tag.TagFactory;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.tag.Tag;
import net.minecraft.util.Identifier;

public interface ForestryTags {

    // <editor-fold desc="Blocks">
    Tag.Identified<Block> TIN_ORES = TagFactory.BLOCK.create(new Identifier("c", "tin_ores"));
    Tag.Identified<Block> BRONZE_ORES = TagFactory.BLOCK.create(new Identifier("c", "bronze_ores"));
    Tag.Identified<Block> APATITE_ORES = TagFactory.BLOCK.create(new Identifier("c", "apatite_ores"));

    Tag.Identified<Block> TIN_BLOCKS = TagFactory.BLOCK.create(new Identifier("c", "tin_blocks"));
    Tag.Identified<Block> BRONZE_BLOCKS = TagFactory.BLOCK.create(new Identifier("c", "bronze_blocks"));
    Tag.Identified<Block> APATITE_BLOCKS = TagFactory.BLOCK.create(new Identifier("c", "apatite_blocks"));
    // </editor-fold>

    // <editor-fold desc="Block items">
    Tag.Identified<Item> TIN_ORES_ITEM = TagFactory.ITEM.create(new Identifier("c", "tin_ores"));
    Tag.Identified<Item> BRONZE_ORES_ITEM = TagFactory.ITEM.create(new Identifier("c", "bronze_ores"));
    Tag.Identified<Item> APATITE_ORES_ITEM = TagFactory.ITEM.create(new Identifier("c", "apatite_ores"));

    Tag.Identified<Item> TIN_BLOCKS_ITEM = TagFactory.ITEM.create(new Identifier("c", "tin_blocks"));
    Tag.Identified<Item> BRONZE_BLOCKS_ITEM = TagFactory.ITEM.create(new Identifier("c", "bronze_blocks"));
    Tag.Identified<Item> APATITE_BLOCKS_ITEM = TagFactory.ITEM.create(new Identifier("c", "apatite_blocks"));
    // </editor-fold>

    // <editor-fold desc="Items">
    Tag.Identified<Item> TIN_INGOTS = TagFactory.ITEM.create(new Identifier("c", "tin_ingots"));
    Tag.Identified<Item> BRONZE_INGOTS = TagFactory.ITEM.create(new Identifier("c", "bronze_ingots"));
    Tag.Identified<Item> APATITE = TagFactory.ITEM.create(new Identifier("c", "apatite"));

    Tag.Identified<Item> TIN_NUGGETS = TagFactory.ITEM.create(new Identifier("c", "tin_nuggets"));
    Tag.Identified<Item> BRONZE_NUGGETS = TagFactory.ITEM.create(new Identifier("c", "bronze_nuggets"));
    // </editor-fold>

    static void initialize() {
    }
}
