package chaos.sculklatch.items;

import chaos.sculklatch.SculkLatch;
import chaos.sculklatch.blocks.ModBlocks;
import chaos.sculklatch.items.custom.SculkBundleItem;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroupEntries;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.BundleItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModItems {
    public static final Item SCULK_LATCH = registerItem("sculk_latch",
            new Item(new FabricItemSettings().maxCount(16)));

    public static final Item SCULK_BUNDLE = registerItem("sculk_bundle",
            new SculkBundleItem(new FabricItemSettings().maxCount(1).fireproof()));

    public static final Item AMETHYST_BELL = registerItem("amethyst_bell",
            new Item(new FabricItemSettings()));

    private static Item registerItem(String name, Item item) {
        return Registry.register(Registries.ITEM, new Identifier(SculkLatch.MOD_ID, name), item);
    }

    private static void addItemsToIngredientsTabItemGroup(FabricItemGroupEntries entries) {
        entries.add(SCULK_LATCH);
    }

    private static void addItemsToToolsTabItemGroup(FabricItemGroupEntries entries) {
        entries.add(AMETHYST_BELL);
        entries.add(SCULK_BUNDLE);
    }

    private static void addItemsToFunctionalTabItemGroup(FabricItemGroupEntries entries) {
    }

    public static void registerModItems() {
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.INGREDIENTS).register(ModItems::addItemsToIngredientsTabItemGroup);
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.TOOLS).register(ModItems::addItemsToToolsTabItemGroup);
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.FUNCTIONAL).register(ModItems::addItemsToFunctionalTabItemGroup);
        SculkLatch.LOGGER.info("Registering Items for Mod " + SculkLatch.MOD_ID);
    }
}