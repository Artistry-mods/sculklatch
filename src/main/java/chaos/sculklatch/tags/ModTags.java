package chaos.sculklatch.tags;

import chaos.sculklatch.SculkLatch;
import net.minecraft.entity.EntityType;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;

public class ModTags {
    public static final TagKey<EntityType<?>> SCULK_JAW_IMMUNE = TagKey.of(RegistryKeys.ENTITY_TYPE, new Identifier(SculkLatch.MOD_ID, "sculk_jaw_immune"));

    public static void registerModTags() {

    }
}
