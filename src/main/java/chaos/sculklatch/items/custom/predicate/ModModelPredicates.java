package chaos.sculklatch.items.custom.predicate;

import chaos.sculklatch.SculkLatch;
import chaos.sculklatch.items.ModItems;
import chaos.sculklatch.items.custom.SculkBundleItem;
import net.minecraft.client.item.ModelPredicateProviderRegistry;
import net.minecraft.util.Identifier;

public class ModModelPredicates {
    public static void registerModModelPredicates() {
        ModelPredicateProviderRegistry.register(ModItems.SCULK_BUNDLE, Identifier.of(SculkLatch.MOD_ID,"filled"), (stack, world, entity, seed) -> {
            return SculkBundleItem.getAmountFilled(stack);
        });
        ModelPredicateProviderRegistry.register(ModItems.SCULK_BUNDLE, Identifier.of(SculkLatch.MOD_ID,"over_filled"), (stack, world, entity, seed) -> {
            return SculkBundleItem.getAmountOverFilled(stack);
        });
    }
}
