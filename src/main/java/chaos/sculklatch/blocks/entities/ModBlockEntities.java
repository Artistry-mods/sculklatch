package chaos.sculklatch.blocks.entities;

import chaos.sculklatch.SculkLatch;
import chaos.sculklatch.blocks.ModBlocks;
import chaos.sculklatch.blocks.entities.custom.SculkChestBlockEntity;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModBlockEntities {
    public static final BlockEntityType<SculkChestBlockEntity> SCULK_CHEST_BLOCK_ENTITY_TYPE = Registry.register(
            Registries.BLOCK_ENTITY_TYPE,
            Identifier.of(SculkLatch.MOD_ID, "sculk_chest_entity"),
            FabricBlockEntityTypeBuilder.create(SculkChestBlockEntity::new, ModBlocks.SCULK_CHEST).build()
    );

    public static void registerModBlockEntities() {

    }
}
