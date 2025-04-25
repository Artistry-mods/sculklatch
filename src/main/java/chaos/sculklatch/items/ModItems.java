package chaos.sculklatch.items;

import chaos.sculklatch.SculkLatch;
import chaos.sculklatch.blocks.ModBlocks;
import chaos.sculklatch.items.custom.AmethystBellItem;
import chaos.sculklatch.items.custom.components.ModDataComponentTypes;
import chaos.sculklatch.items.custom.components.custom.OverfilledBundleContentComponent;
import chaos.sculklatch.items.custom.SculkBundleItem;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroupEntries;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.block.Block;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.BundleContentsComponent;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;

import javax.xml.crypto.Data;
import java.util.function.BiFunction;
import java.util.function.Function;

public class ModItems {
    public static final Item SCULK_LATCH = registerItem("sculk_latch", Item::new,
            new Item.Settings().maxCount(16));

    public static final Item SCULK_BUNDLE = registerItem("sculk_bundle",
            SculkBundleItem::new, new Item.Settings().maxCount(1).fireproof().rarity(Rarity.EPIC)
                    .component(DataComponentTypes.BUNDLE_CONTENTS, BundleContentsComponent.DEFAULT).component(ModDataComponentTypes.OVERFILLED_BUNDLE_CONTENTS, OverfilledBundleContentComponent.DEFAULT));

    public static final Item AMETHYST_BELL = registerItem("amethyst_bell",
            AmethystBellItem::new, new Item.Settings().maxCount(1));

    public static final Item SCULK_JAW = registerBlockItem("sculk_jaw",
            ModBlocks.SCULK_JAW, BlockItem::new, new Item.Settings());

    public static Item registerItem(String id, Function<Item.Settings, Item> factory, Item.Settings settings) {
        RegistryKey<Item> key = RegistryKey.of(RegistryKeys.ITEM, Identifier.of(SculkLatch.MOD_ID, id));
        Item item = factory.apply(settings.registryKey(key));
        return Registry.register(Registries.ITEM, key, item);
    }

    public static Item registerBlockItem(String id, Block block, BiFunction<Block, Item.Settings, Item> factory, Item.Settings settings) {
        RegistryKey<Block> blockKey = block.getRegistryEntry().registryKey();
        RegistryKey<Item> key = RegistryKey.of(RegistryKeys.ITEM, blockKey.getValue());
        Item item = factory.apply(block, settings.registryKey(key).useBlockPrefixedTranslationKey());
        return Registry.register(Registries.ITEM, key, item);
    }

    private static void addItemsToIngredientsTabItemGroup(FabricItemGroupEntries entries) {
        entries.addAfter(Items.ECHO_SHARD, SCULK_LATCH);
    }

    private static void addItemsToToolsTabItemGroup(FabricItemGroupEntries entries) {
        entries.addBefore(Items.ENDER_PEARL, AMETHYST_BELL);
        entries.addAfter(Items.BUNDLE, SCULK_BUNDLE);
    }

    private static void addItemsToNatureTabItemGroup(FabricItemGroupEntries entries) {
        entries.addBefore(Items.SCULK_CATALYST, SCULK_JAW);
    }

    public static void registerModItems() {
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.INGREDIENTS).register(ModItems::addItemsToIngredientsTabItemGroup);
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.TOOLS).register(ModItems::addItemsToToolsTabItemGroup);
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.NATURAL).register(ModItems::addItemsToNatureTabItemGroup);
        SculkLatch.LOGGER.info("Registering Items for Mod " + SculkLatch.MOD_ID);
    }

}