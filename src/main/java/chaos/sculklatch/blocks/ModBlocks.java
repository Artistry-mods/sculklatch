package chaos.sculklatch.blocks;

import chaos.sculklatch.SculkLatch;
import chaos.sculklatch.blocks.blockEntities.ModBlockEntities;
import chaos.sculklatch.blocks.custom.SculkChestBlock;
import chaos.sculklatch.blocks.custom.SculkJawBlock;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModBlocks {
    public static final Block SCULK_CHEST = registerBlock("sculk_chest",
            new SculkChestBlock(AbstractBlock.Settings.create().strength(200.0f, 12.0F).luminance(stat -> 0), () -> ModBlockEntities.SCULK_CHEST_BLOCK_ENTITY_TYPE));

    public static final Block SCULK_JAW = registerBlock("sculk_jaw",
            new SculkJawBlock(AbstractBlock.Settings.copy(Blocks.SCULK_CATALYST).strength(20.0f, 3.0f).solid()));

    private static Block registerBlock(String name, Block item) {
        return Registry.register(Registries.BLOCK, new Identifier(SculkLatch.MOD_ID, name), item);
    }

    public static void registerModItems() {
        SculkLatch.LOGGER.info("Registering Blocks for Mod " + SculkLatch.MOD_ID);
    }
}
