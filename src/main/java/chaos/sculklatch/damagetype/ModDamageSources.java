package chaos.sculklatch.damagetype;

import net.minecraft.entity.damage.DamageType;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;

public class ModDamageSources {
    public static RegistryKey<DamageType> SCULK_JAW = RegistryKey.of(RegistryKeys.DAMAGE_TYPE, Identifier.of("sculk_jaw"));

}
