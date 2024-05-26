package chaos.sculklatch.items.custom;

import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.item.ModelPredicateProviderRegistry;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.BundleItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

import java.util.Optional;
import java.util.stream.Stream;

public class SculkBundleItem extends BundleItem {
    private static final String ITEMS_KEY = "OverFilledItems";
    private static final int MAX_OVER_FILL_STORAGE = 64 * 10;

    public SculkBundleItem(Settings settings) {
        super(settings);

        if (FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT) {
            this.RegisterModelPredicate();
        }
    }

    private void RegisterModelPredicate() {
        ModelPredicateProviderRegistry.register(new Identifier("filled"), (itemStack, clientWorld, livingEntity, i) -> {
            return SculkBundleItem.getAmountFilled(itemStack);
        });
        ModelPredicateProviderRegistry.register(new Identifier("over_filled"), (itemStack, clientWorld, livingEntity, i) -> {
            return SculkBundleItem.getAmountOverFilled(itemStack);
        });
    }
    public static float getAmountOverFilled(ItemStack stack) {
        return (float)getOverFillBundleOccupancy(stack) / MAX_OVER_FILL_STORAGE;
    }
    public static int getOverFillBundleOccupancy(ItemStack stack) {
        return SculkBundleItem.getBundledStacks(stack).mapToInt(itemStack -> SculkBundleItem.getOverFillItemOccupancy(itemStack) * itemStack.getCount()).sum();
    }
    private static Stream<ItemStack> getBundledStacks(ItemStack stack) {
        NbtCompound nbtCompound = stack.getNbt();
        if (nbtCompound == null) {
            return Stream.empty();
        }
        NbtList nbtList = nbtCompound.getList(ITEMS_KEY, NbtElement.COMPOUND_TYPE);
        return nbtList.stream().map(NbtCompound.class::cast).map(ItemStack::fromNbt);
    }
    private static Optional<NbtCompound> canMergeStack(NbtList items) {
        return items.stream().filter(NbtCompound.class::isInstance).map(NbtCompound.class::cast).filter(item -> false).findFirst();
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        super.inventoryTick(stack, world, entity, slot, selected);
        NbtCompound nbtCompound = stack.getOrCreateNbt();
        if (nbtCompound.contains(ITEMS_KEY)) {
            NbtList nbtList = nbtCompound.getList(ITEMS_KEY, NbtElement.COMPOUND_TYPE);
            if (!nbtList.isEmpty()) {
                removeFirstStack(stack).ifPresent(removedStack -> {
                    int firstEmptySlot = ((PlayerEntity)entity).getInventory().getEmptySlot();
                    if (firstEmptySlot != -1) {
                        ((PlayerEntity) entity).getInventory().setStack(firstEmptySlot, removedStack);
                    }
                });
            }
        }
    }

    private static Optional<ItemStack> removeFirstStack(ItemStack stack) {
        NbtCompound nbtCompound = stack.getOrCreateNbt();
        if (!nbtCompound.contains(ITEMS_KEY)) {
            return Optional.empty();
        }
        NbtList nbtList = nbtCompound.getList(ITEMS_KEY, NbtElement.COMPOUND_TYPE);
        if (nbtList.isEmpty()) {
            return Optional.empty();
        }
        NbtCompound nbtCompound2 = nbtList.getCompound(0);
        ItemStack itemStack = ItemStack.fromNbt(nbtCompound2);
        nbtList.remove(0);
        if (nbtList.isEmpty()) {
            stack.removeSubNbt(ITEMS_KEY);
        }
        return Optional.of(itemStack);
    }

    private static int getOverFillItemOccupancy(ItemStack stack) {
        if (stack.getItem() instanceof BundleItem) {
            return 4 + getOverFillBundleOccupancy(stack);
        } else {
            if ((stack.isOf(Items.BEEHIVE) || stack.isOf(Items.BEE_NEST)) && stack.hasNbt()) {
                NbtCompound nbtCompound = BlockItem.getBlockEntityNbt(stack);
                if (nbtCompound != null && !nbtCompound.getList("Bees", 10).isEmpty()) {
                    return MAX_OVER_FILL_STORAGE;
                }
            }

            return MAX_OVER_FILL_STORAGE / (stack.getMaxCount() * 10);
        }
    }

    public static int overFillBundle(ItemStack bundle, ItemStack stack) {
        if (stack.isEmpty() || !stack.getItem().canBeNested() || stack.getItem() instanceof SculkBundleItem) {
            return 0;
        }
        NbtCompound nbtCompound = bundle.getOrCreateNbt();
        if (!nbtCompound.contains(ITEMS_KEY)) {
            nbtCompound.put(ITEMS_KEY, new NbtList());
        }
        int i = SculkBundleItem.getOverFillBundleOccupancy(bundle);
        int j = SculkBundleItem.getOverFillItemOccupancy(stack);
        int k = Math.min(stack.getCount(), (MAX_OVER_FILL_STORAGE - i) / j);
        if (k == 0) {
            return 0;
        }
        NbtList nbtList = nbtCompound.getList(ITEMS_KEY, NbtElement.COMPOUND_TYPE);
        Optional<NbtCompound> optional = SculkBundleItem.canMergeStack(nbtList);
        if (optional.isPresent()) {
            NbtCompound nbtCompound2 = optional.get();
            ItemStack itemStack = ItemStack.fromNbt(nbtCompound2);
            itemStack.increment(k);
            itemStack.writeNbt(nbtCompound2);
            nbtList.remove(nbtCompound2);
            nbtList.add(0, nbtCompound2);
        } else {
            ItemStack itemStack2 = stack.copyWithCount(k);
            NbtCompound nbtCompound3 = new NbtCompound();
            itemStack2.writeNbt(nbtCompound3);
            nbtList.add(0, nbtCompound3);
        }
        return k;
    }
}
