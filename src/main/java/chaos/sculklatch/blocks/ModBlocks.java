package chaos.sculklatch.blocks;

import chaos.sculklatch.SculkLatch;
import chaos.sculklatch.blocks.entities.ModBlockEntities;
import chaos.sculklatch.blocks.custom.SculkChestBlock;
import chaos.sculklatch.blocks.custom.SculkJawBlock;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;

import java.util.function.Function;

public class ModBlocks {
    public static final Block SCULK_CHEST = registerBlock("sculk_chest",
            (settings) -> new SculkChestBlock(() -> ModBlockEntities.SCULK_CHEST_BLOCK_ENTITY_TYPE, settings), AbstractBlock.Settings.create().strength(200.0f, 12.0F).luminance(stat -> 0));

    public static final Block SCULK_JAW = registerBlock("sculk_jaw",
            SculkJawBlock::new, (AbstractBlock.Settings.copy(Blocks.SCULK_CATALYST).strength(20.0f, 3.0f).solid()));

    private static Block registerBlock(String id, Function<AbstractBlock.Settings, Block> factory, AbstractBlock.Settings settings) {
        RegistryKey<Block> key = RegistryKey.of(RegistryKeys.BLOCK, Identifier.of(SculkLatch.MOD_ID, id));
        Block block = factory.apply(settings.registryKey(key));
        return Registry.register(Registries.BLOCK, Identifier.of(SculkLatch.MOD_ID, id), block);
    }

    public static void registerModItems() {
        SculkLatch.LOGGER.info("Registering Blocks for Mod " + SculkLatch.MOD_ID);
    }
}
