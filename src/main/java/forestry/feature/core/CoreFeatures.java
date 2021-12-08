package forestry.feature.core;

import forestry.Forestry;
import forestry.feature.core.item.*;
import forestry.util.FeatureRegistry;
import net.minecraft.item.Item;

import java.util.function.Function;

public interface CoreFeatures {

    FeatureRegistry REGISTRY = Forestry.create("");

    Item COMPOST = REGISTRY.item("compost");
    Item FERTILIZER = REGISTRY.item("fertilizer", settings(FertilizerItem::new));

    Item APATITE = REGISTRY.item("apatite");

    Item RESEARCH_NOTE = REGISTRY.item("research_note", settings(ResearchNoteItem::new));
    Item PORTABLE_ANALYZER = REGISTRY.item("portable_analyzer", settings(AnalyzerItem::new));

    Item INGOT_TIN = REGISTRY.item("ingot_tin");
    Item INGOT_BRONZE = REGISTRY.item("ingot_bronze");

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
    FeatureRegistry.Variants<Item, CircuitBoardType> CIRCUIT_BOARDS = REGISTRY.itemGroup("soldering_iron", $ -> settings(Item::new), CircuitBoardType.values());
    FeatureRegistry.Variants<Item, ElectronTube> ELECTRON_TUBES = REGISTRY.itemGroup("soldering_iron", $ -> settings(Item::new), ElectronTube.values());

    // TODO: Naturalist helmet???
    Item NATURALIST_HELMET = REGISTRY.item("naturalist_helmet");

    // TODO: Burn time
    Item PEAT = REGISTRY.item("peat");
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

    static void initialize() {
        REGISTRY.freeze();
    }

    private static Item settings(Function<Item.Settings, Item> producer) {
        return producer.apply(new Item.Settings());
    }
}
