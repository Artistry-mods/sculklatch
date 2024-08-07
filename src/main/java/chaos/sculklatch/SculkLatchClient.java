package chaos.sculklatch;

import chaos.sculklatch.blocks.blockEntities.ModBlockEntities;
import chaos.sculklatch.blocks.blockEntities.custom.renderers.SculkChestBlockEntityRenderer;
import chaos.sculklatch.items.custom.predicate.ModModelPredicates;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactories;

public class SculkLatchClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ModModelPredicates.registerModModelPredicates();
        BlockEntityRendererFactories.register(ModBlockEntities.SCULK_CHEST_BLOCK_ENTITY_TYPE, SculkChestBlockEntityRenderer::new);
    }
}
