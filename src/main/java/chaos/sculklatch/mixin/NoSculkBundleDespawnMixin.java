package chaos.sculklatch.mixin;

import chaos.sculklatch.items.ModItems;
import chaos.sculklatch.items.custom.SculkBundleItem;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.tag.DamageTypeTags;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemEntity.class)
public abstract class NoSculkBundleDespawnMixin {
    @Shadow
    private int itemAge;

    ///data get entity @e[limit=1, type=!minecraft:player, sort=nearest]
    @Shadow
    public abstract ItemStack getStack();

    @Inject(at = @At("HEAD"), method = "tick")
    public void onPlaced(CallbackInfo ci) {
        if (this.getStack().getItem() instanceof SculkBundleItem && this.itemAge > 125) {
            this.itemAge = 0;
        }
    }

    @Inject(at = @At("HEAD"), method = "damage", cancellable = true)
    public void damage(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        if (!this.getStack().isEmpty() && this.getStack().isOf(ModItems.SCULK_BUNDLE) && source.isIn(DamageTypeTags.IS_EXPLOSION)) {
            cir.setReturnValue(false);
        }
    }
}
