package chaos.sculklatch;

import chaos.sculklatch.blocks.blockEntities.ModBlockEntities;
import chaos.sculklatch.blocks.blockEntities.custom.renderers.SculkChestBlockEntityRenderer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.BlockEntityRendererRegistry;
import net.minecraft.client.render.block.entity.ChestBlockEntityRenderer;

public class SculkLatchClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        BlockEntityRendererRegistry.register(ModBlockEntities.SCULK_CHEST_BLOCK_ENTITY_TYPE, SculkChestBlockEntityRenderer::new);
    }
}
