package chaos.sculklatch.items.custom;

import chaos.sculklatch.items.custom.components.ModDataComponentTypes;
import chaos.sculklatch.items.custom.components.custom.OverfilledBundleContentComponent;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.BundleContentsComponent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BundleItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class SculkBundleItem extends BundleItem {
    public SculkBundleItem(Item.Settings settings) {
        super(settings);
    }

    public static float getAmountOverFilled(ItemStack stack) {
        OverfilledBundleContentComponent overfilledBundleContentComponent = stack.getOrDefault(ModDataComponentTypes.OVERFILLED_BUNDLE_CONTENTS, OverfilledBundleContentComponent.DEFAULT);
        return overfilledBundleContentComponent.getOccupancy().floatValue();
    }

    @Override
    public void inventoryTick(ItemStack stack, ServerWorld world, Entity entity, @Nullable EquipmentSlot slot) {
        if (getAmountOverFilled(stack) != 0 && entity instanceof PlayerEntity && !((PlayerEntity) entity).isDead() && entity.isAlive() ) {
            OverfilledBundleContentComponent overfilledBundleContentComponent = stack.get(ModDataComponentTypes.OVERFILLED_BUNDLE_CONTENTS);
            if (overfilledBundleContentComponent == null) return;
            OverfilledBundleContentComponent.Builder builder = new OverfilledBundleContentComponent.Builder(overfilledBundleContentComponent);
            builder.ejectAll((PlayerEntity) entity);
            stack.set(ModDataComponentTypes.OVERFILLED_BUNDLE_CONTENTS, builder.build());
        }
    }
}
