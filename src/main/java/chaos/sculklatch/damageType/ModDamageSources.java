package chaos.sculklatch.damageType;

import net.minecraft.entity.damage.DamageType;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;

public class ModDamageSources {
    public static RegistryKey<DamageType> SCULK_LATCH = RegistryKey.of(RegistryKeys.DAMAGE_TYPE, new Identifier("sculk_jaw"));

}
