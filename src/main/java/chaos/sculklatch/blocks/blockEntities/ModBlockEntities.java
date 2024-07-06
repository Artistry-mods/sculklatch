package chaos.sculklatch.blocks.blockEntities;

import chaos.sculklatch.blocks.ModBlocks;
import chaos.sculklatch.blocks.blockEntities.custom.SculkChestBlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModBlockEntities {
    public static final BlockEntityType<SculkChestBlockEntity> SCULK_CHEST_BLOCK_ENTITY_TYPE = Registry.register(
            Registries.BLOCK_ENTITY_TYPE,
            Identifier.of("sculk-latch", "sculk_chest_entity"),
            BlockEntityType.Builder.create(SculkChestBlockEntity::new, ModBlocks.SCULK_CHEST).build()
    );
}
