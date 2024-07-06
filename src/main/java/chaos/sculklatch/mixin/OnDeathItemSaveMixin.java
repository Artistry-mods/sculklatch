package chaos.sculklatch.mixin;

import chaos.sculklatch.items.ModItems;
import chaos.sculklatch.items.custom.components.ModDataComponentTypes;
import chaos.sculklatch.items.custom.components.custom.OverfilledBundleContentComponent;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.GameRules;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;

@Mixin(ServerPlayerEntity.class)
public class OnDeathItemSaveMixin {

    @Unique
    private static List<Integer> getSculkBundles(Inventory inventory) {
        List<Integer> slots = new ArrayList<>();
        for (int j = 0; j < inventory.size(); ++j) {
            ItemStack itemStack = inventory.getStack(j);
            if (itemStack.getItem().equals(ModItems.SCULK_BUNDLE)) {
                slots.add(j);
            }
        }
        return slots;
    }

    @Unique
    private static void fillSculkBundles(ServerPlayerEntity player) {
        int sculk_bundle_count = player.getInventory().count(ModItems.SCULK_BUNDLE);
        if (sculk_bundle_count != 0) {
            List<Integer> sculkBundleSlots = getSculkBundles(player.getInventory());
            for (Integer sculkBundleSlot : sculkBundleSlots) {
                ItemStack sculkBundle = player.getInventory().getStack(sculkBundleSlot);
                OverfilledBundleContentComponent bundleContentsComponent = sculkBundle.get(ModDataComponentTypes.OVERFILLED_BUNDLE_CONTENTS);
                if (bundleContentsComponent == null) return;
                OverfilledBundleContentComponent.Builder builder = new OverfilledBundleContentComponent.Builder(bundleContentsComponent);
                builder.saveAll(player);
                sculkBundle.set(ModDataComponentTypes.OVERFILLED_BUNDLE_CONTENTS, builder.build());
            }
        }
    }

    @Inject(at = @At("HEAD"), method = "onDeath")
    public void onDeath(DamageSource damageSource, CallbackInfo ci) {
        ServerPlayerEntity entity = (ServerPlayerEntity) (Object) this;
        if (!entity.getWorld().getGameRules().getBoolean(GameRules.KEEP_INVENTORY)) {
            fillSculkBundles(entity);
        }
    }
}