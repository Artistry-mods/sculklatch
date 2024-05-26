package chaos.sculklatch.mixin;

import chaos.sculklatch.items.custom.SculkBundleItem;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemEntity.class)
public abstract class NoSculkBundleDespawnMixin {
    ///data get entity @e[limit=1, type=!minecraft:player, sort=nearest]
    @Shadow public abstract ItemStack getStack();

    @Shadow private int itemAge;

    @Inject(at = @At("HEAD"), method = "tick")
    public void onPlaced(CallbackInfo ci) {
        if (this.getStack().getItem() instanceof SculkBundleItem && this.itemAge > 125) {
            this.itemAge = 0;
        }
    }
}
