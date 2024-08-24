package chaos.sculklatch;

import chaos.sculklatch.blocks.entities.ModBlockEntities;
import chaos.sculklatch.blocks.entities.custom.renderers.SculkChestBlockEntityRenderer;
import chaos.sculklatch.items.custom.predicate.ModModelPredicates;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactories;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.util.Identifier;

public class SculkLatchClient implements ClientModInitializer {
    public static final SpriteIdentifier FULLY_SCULKED_CHEST_TEXTURE = new SpriteIdentifier(Identifier.of("textures/atlas/chest.png"), Identifier.of("sculk-latch", "entity/chest/sculk_chest_full_sculked"));
    public static final SpriteIdentifier SCARED_SCULKED_CHEST_TEXTURE = new SpriteIdentifier(Identifier.of("textures/atlas/chest.png"), Identifier.of("sculk-latch", "entity/chest/sculk_chest_scared_sculked"));

    @Override
    public void onInitializeClient() {
        ModModelPredicates.registerModModelPredicates();
        BlockEntityRendererFactories.register(ModBlockEntities.SCULK_CHEST_BLOCK_ENTITY_TYPE, SculkChestBlockEntityRenderer::new);
    }
}
