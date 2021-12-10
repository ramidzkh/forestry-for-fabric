package forestry.feature.core;

import forestry.Forestry;
import forestry.feature.core.block.AnalyzerBlock;
import forestry.feature.core.block.EscritoireBlock;
import forestry.feature.core.item.*;
import forestry.util.FeatureRegistry;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.item.Item;

import java.util.function.Function;

public interface CoreFeatures {

    FeatureRegistry REGISTRY = Forestry.create("");

    Item COMPOST = REGISTRY.item("compost");
    Item FERTILIZER = REGISTRY.item("fertilizer", settings(FertilizerItem::new));

    Item APATITE = REGISTRY.item("apatite");

    Item RESEARCH_NOTE = REGISTRY.item("research_note", settings(ResearchNoteItem::new));
    Item PORTABLE_ANALYZER = REGISTRY.item("portable_analyzer", settings(AnalyzerItem::new));

    // TODO: Use a wrench tag instead?
    Item WRENCH = REGISTRY.item("wrench", settings(WrenchItem::new));
    Item PIPETTE = REGISTRY.item("pipette", settings(PipetteItem::new));

    Item CARTON = REGISTRY.item("carton");
    Item BROKEN_BRONZE_PICKAXE = REGISTRY.item("broken_bronze_pickaxe");
    Item BROKEN_BRONZE_SHOVEL = REGISTRY.item("broken_bronze_shovel");

    // TODO: Implement tools
    Item BRONZE_PICKAXE = REGISTRY.item("bronze_pickaxe");
    Item BRONZE_SHOVEL = REGISTRY.item("bronze_shovel");

    Item STURDY_CASING = REGISTRY.item("sturdy_casing");
    Item HARDENED_CASING = REGISTRY.item("hardened_casing");
    Item IMPREGNATED_CASING = REGISTRY.item("impregnated_casing");
    Item FLEXIBLE_CASING = REGISTRY.item("flexible_casing");
    Item GEAR_BRONZE = REGISTRY.item("gear_bronze");
    Item GEAR_COPPER = REGISTRY.item("gear_copper");
    Item GEAR_TIN = REGISTRY.item("gear_tin");

    Item SOLDERING_IRON = REGISTRY.item("soldering_iron", settings(SolderingIronItem::new));
    FeatureRegistry.Variants<Item, CircuitBoardType> CIRCUIT_BOARDS = REGISTRY.itemGroup("circuit_boards", $ -> settings(Item::new), CircuitBoardType.values());
    FeatureRegistry.Variants<Item, ElectronTube> ELECTRON_TUBES = REGISTRY.itemGroup("electron_tubes", $ -> settings(Item::new), ElectronTube.values());

    // TODO: Naturalist helmet???
    Item NATURALIST_HELMET = REGISTRY.item("naturalist_helmet");

    Item ASH = REGISTRY.item("ash");
    // TODO: Burn time
    Item BITUMINOUS_PEAT = REGISTRY.item("bituminous_peat");

    Item MOULDY_WHEAT = REGISTRY.item("mouldy_wheat");
    Item DECAYING_WHEAT = REGISTRY.item("decaying_wheat");
    Item MULCH = REGISTRY.item("mulch");

    Item IODINE_CHARGE = REGISTRY.item("iodine_charge");
    Item PHOSPHOR = REGISTRY.item("phosphor");

    // TODO: CRAFTING_MATERIALS
    Item STICK_IMPREGNATED = REGISTRY.item("stick_impregnated");
    Item WOOD_PULP = REGISTRY.item("wood_pulp");
    Item BEESWAX = REGISTRY.item("beeswax");
    Item REFRACTORY_WAX = REGISTRY.item("refractory_wax");
    // TODO: Food
    FeatureRegistry.Variants<Item, Fruit> FRUITS = REGISTRY.itemGroup("fruit", $ -> settings(Item::new), Fruit.values());

    Block ANALYZER = REGISTRY.block("analyzer", new AnalyzerBlock(FabricBlockSettings.of(Material.METAL)));
    Block ESCRITOIRE = REGISTRY.block("escritoire", new EscritoireBlock(FabricBlockSettings.of(Material.METAL)));

    // TODO: Burn time
    Block PEAT = REGISTRY.block("peat");
    Block HUMUS = REGISTRY.block("humus");

    // Just don't generate bronze ore lol
    FeatureRegistry.Variants<Block, ResourceType> RESOURCE_ORE = REGISTRY.blockGroup("ore", $ -> new Block(FabricBlockSettings.of(Material.STONE)), ResourceType.values());
    FeatureRegistry.Variants<Block, ResourceType> RESOURCE_BLOCK = REGISTRY.blockGroup("block", $ -> new Block(FabricBlockSettings.of(Material.METAL)), ResourceType.values());
    FeatureRegistry.Variants<Item, ResourceType> RESOURCE_INGOT = REGISTRY.itemGroup("ingot", $ -> settings(Item::new), ResourceType.values());
    FeatureRegistry.Variants<Item, ResourceType> RESOURCE_NUGGET = REGISTRY.itemGroup("nugget", $ -> settings(Item::new), ResourceType.values());

    static void initialize() {
        REGISTRY.freeze();
    }

    private static Item settings(Function<Item.Settings, Item> producer) {
        return producer.apply(new Item.Settings());
    }
}
