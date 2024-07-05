package chaos.sculklatch.items.custom.test;

import chaos.sculklatch.items.custom.components.ModDataComponentTypes;
import chaos.sculklatch.items.custom.components.custom.OverfilledBundleContentComponent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BundleItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class SculkBundleItem extends BundleItem {
    public SculkBundleItem(Item.Settings settings) {
        super(settings);
    }

    public static float getAmountOverFilled(ItemStack stack) {
        OverfilledBundleContentComponent bundleContentsComponent = stack.getOrDefault(ModDataComponentTypes.OVERFILLED_BUNDLE_CONTENTS, OverfilledBundleContentComponent.DEFAULT);
        return bundleContentsComponent.getOccupancy().floatValue();
    }


    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        if (getAmountOverFilled(stack) != 0 && entity instanceof PlayerEntity && !((PlayerEntity) entity).isDead() && entity.isAlive() ) {
            OverfilledBundleContentComponent bundleContentsComponent = stack.get(ModDataComponentTypes.OVERFILLED_BUNDLE_CONTENTS);
            if (bundleContentsComponent == null) return;
            OverfilledBundleContentComponent.Builder builder = new OverfilledBundleContentComponent.Builder(bundleContentsComponent);
            builder.ejectAll((PlayerEntity) entity);
            stack.set(ModDataComponentTypes.OVERFILLED_BUNDLE_CONTENTS, builder.build());
        }
    }
}
