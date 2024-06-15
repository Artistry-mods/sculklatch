package chaos.sculklatch.mixin;

import chaos.sculklatch.blocks.ModBlocks;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.WardenEntity;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public class SculkJawPullEntitiesDownMixin {

    @Inject(at = @At("RETURN"), method = "tickMovement")
    public void tickMovement(CallbackInfo ci) {
        LivingEntity livingEntity = (LivingEntity) (Object) this;
        if (!(livingEntity instanceof WardenEntity)) {
            if (livingEntity.getWorld().getBlockState(livingEntity.getBlockPos().down()).getBlock() == ModBlocks.SCULK_JAW && !livingEntity.isSneaking()) {
                livingEntity.addVelocity(livingEntity.getBlockPos().down().toCenterPos().add(0, -0.3, 0).subtract(livingEntity.getPos()).multiply(0.3));
                livingEntity.slowMovement(livingEntity.getWorld().getBlockState(livingEntity.getBlockPos().down()), new Vec3d(0.5, 1, 0.5));
            }
        }
    }
}
