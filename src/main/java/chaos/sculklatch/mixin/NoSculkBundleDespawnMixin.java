package chaos.sculklatch.mixin;

import chaos.sculklatch.items.ModItems;
import chaos.sculklatch.items.custom.SculkBundleItem;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageTypes;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.tag.DamageTypeTags;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemEntity.class)
public abstract class NoSculkBundleDespawnMixin {
    @Shadow
    private int itemAge;

    @Shadow
    public abstract ItemStack getStack();

    @Inject(method = "tick", at = @At("TAIL"))
    public void sculkLatch$preventSculkBundleDespawning(CallbackInfo ci) {
        if (this.getStack().getItem() instanceof SculkBundleItem) {
            if (this.itemAge >= 5900) {
                this.itemAge = 0;
            }
        }
    }

    @WrapOperation(at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/ItemEntity;isAlwaysInvulnerableTo(Lnet/minecraft/entity/damage/DamageSource;)Z"), method = "damage")
    public boolean sculkLatch$sculkBundleDamageImmunity(ItemEntity instance, DamageSource source, Operation<Boolean> original) {
        if (instance.getStack().isOf(ModItems.SCULK_BUNDLE) && (source.isIn(DamageTypeTags.IS_EXPLOSION) || source.isOf(DamageTypes.CACTUS))) {
            return true;
        }
        return original.call(instance, source);
    }
}
