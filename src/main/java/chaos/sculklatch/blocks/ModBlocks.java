package chaos.sculklatch.blocks;

import chaos.sculklatch.SculkLatch;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroupEntries;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModBlocks {
    private static Block registerItem(String name, Block item) {
        return Registry.register(Registries.BLOCK, new Identifier(SculkLatch.MOD_ID, name), item);
    }

    public static void registerModItems() {
        SculkLatch.LOGGER.info("Registering Blocks for Mod " + SculkLatch.MOD_ID);
    }
}
