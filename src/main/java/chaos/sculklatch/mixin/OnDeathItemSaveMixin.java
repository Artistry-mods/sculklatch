package chaos.sculklatch.mixin;

import chaos.sculklatch.items.ModItems;
import chaos.sculklatch.items.custom.SculkBundleItem;
import net.fabricmc.loader.impl.lib.sat4j.core.Vec;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.GameRules;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayerEntity.class)
public class OnDeathItemSaveMixin {

	@Inject(at = @At("HEAD"), method = "onDeath", cancellable = true)
    public void onDeath(DamageSource damageSource, CallbackInfo ci) {
        ServerPlayerEntity entity = (ServerPlayerEntity) (Object) this;
        if (!entity.getWorld().getGameRules().getBoolean(GameRules.KEEP_INVENTORY)) {
            fillSculkBundles(entity);
        }
    }
    private static Vec<Integer> getSculkBundles(Inventory inventory) {
        Vec<Integer> slots = new Vec<>();
        for(int j = 0; j < inventory.size(); ++j) {
            ItemStack itemStack = inventory.getStack(j);
            if (itemStack.getItem().equals(ModItems.SCULK_BUNDLE)) {
                slots.push(j);
            }
        }
        return slots;
    }

    private static void fillSculkBundles(ServerPlayerEntity player) {
        int sculk_bundles = player.getInventory().count(ModItems.SCULK_BUNDLE);
        if (sculk_bundles != 0) {
            Vec<Integer> sculkBundleSlots = getSculkBundles(player.getInventory());
            for (int i = 0; i < sculkBundleSlots.size(); i++) {
                ItemStack sculkBundle = player.getInventory().getStack(sculkBundleSlots.get(i));
                for(int j = 0; j < player.getInventory().size(); ++j) {
                    ItemStack itemStack = player.getInventory().getStack(j);
                    int removedItems = SculkBundleItem.overFillBundle(sculkBundle, itemStack);
                    itemStack.decrement(removedItems);
                }
            }
        }
    }
}