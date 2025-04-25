package chaos.sculklatch.items.custom.predicate;

import chaos.sculklatch.items.custom.components.ModDataComponentTypes;
import com.mojang.serialization.MapCodec;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.item.property.bool.BooleanProperty;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemDisplayContext;
import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.Nullable;

@Environment(EnvType.CLIENT)
public record OverfilledBundleProperty() implements BooleanProperty {
    public static final MapCodec<OverfilledBundleProperty> CODEC = MapCodec.unit(new OverfilledBundleProperty());

    public OverfilledBundleProperty() {
    }

    public boolean test(ItemStack stack, @Nullable ClientWorld world, @Nullable LivingEntity entity, int seed, ItemDisplayContext displayContext) {
        if (stack.get(ModDataComponentTypes.OVERFILLED_BUNDLE_CONTENTS) != null) {
            return !stack.get(ModDataComponentTypes.OVERFILLED_BUNDLE_CONTENTS).isEmpty();
        }
        return false;
    }

    public MapCodec<OverfilledBundleProperty> getCodec() {
        return CODEC;
    }
}
