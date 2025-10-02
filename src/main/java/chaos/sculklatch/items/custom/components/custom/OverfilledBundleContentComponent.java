package chaos.sculklatch.items.custom.components.custom;

import chaos.sculklatch.SculkLatch;
import chaos.sculklatch.items.ModItems;
import chaos.sculklatch.items.custom.components.ModDataComponentTypes;
import com.google.common.collect.Lists;
import com.mojang.serialization.Codec;
//import dev.emi.trinkets.api.SlotReference;
//import dev.emi.trinkets.api.TrinketComponent;
//import dev.emi.trinkets.api.TrinketsApi;
import dev.emi.trinkets.api.SlotReference;
import dev.emi.trinkets.api.TrinketComponent;
import dev.emi.trinkets.api.TrinketsApi;
import net.minecraft.block.entity.BeehiveBlockEntity;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.BeesComponent;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipData;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.util.Pair;
import org.apache.commons.lang3.math.Fraction;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.stream.Stream;

public class OverfilledBundleContentComponent implements TooltipData {
    public static final OverfilledBundleContentComponent DEFAULT = new OverfilledBundleContentComponent(List.of());
    public static final Codec<OverfilledBundleContentComponent> CODEC;
    public static final PacketCodec<RegistryByteBuf, OverfilledBundleContentComponent> PACKET_CODEC;
    private static final Fraction NESTED_BUNDLE_OCCUPANCY;
    private static final int MAX_ITEMS = 32;  // Maximum number of items in the bundle
    final List<ItemStack> stacks;
    final Fraction occupancy;

    OverfilledBundleContentComponent(List<ItemStack> stacks, Fraction occupancy) {
        this.stacks = stacks;
        this.occupancy = occupancy;
    }

    public OverfilledBundleContentComponent(List<ItemStack> stacks) {
        this(stacks, calculateOccupancy(stacks));
    }

    private static Fraction calculateOccupancy(List<ItemStack> stacks) {
        Fraction fraction = Fraction.ZERO;

        ItemStack itemStack;
        for(Iterator<ItemStack> var2 = stacks.iterator(); var2.hasNext(); fraction = fraction.add(getOccupancy(itemStack).multiplyBy(Fraction.getFraction(itemStack.getCount(), 1)))) {
            itemStack = var2.next();
        }

        return fraction;
    }

    static Fraction getOccupancy(ItemStack stack) {
        OverfilledBundleContentComponent overfilledBundleContentsComponent = stack.get(ModDataComponentTypes.OVERFILLED_BUNDLE_CONTENTS);
        if (overfilledBundleContentsComponent != null) {
            return NESTED_BUNDLE_OCCUPANCY.add(overfilledBundleContentsComponent.getOccupancy());
        } else {
            List<BeehiveBlockEntity.BeeData> list = (stack.getOrDefault(DataComponentTypes.BEES, BeesComponent.DEFAULT)).bees();
            return !list.isEmpty() ? Fraction.ONE : Fraction.getFraction(1, stack.getMaxCount() * 10); // muahahahahahahhhh higher capacity size upon ye
        }
    }

    public ItemStack get(int index) {
        return this.stacks.get(index);
    }

    public Stream<ItemStack> stream() {
        return this.stacks.stream().map(ItemStack::copy);
    }

    public Iterable<ItemStack> iterate() {
        return this.stacks;
    }

    public Iterable<ItemStack> iterateCopy() {
        return Lists.transform(this.stacks, ItemStack::copy);
    }

    public int size() {
        return this.stacks.size();
    }

    public Fraction getOccupancy() {
        return this.occupancy;
    }

    public boolean isEmpty() {
        return this.stacks.isEmpty();
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (!(o instanceof OverfilledBundleContentComponent bundleContentsComponent)) {
            return false;
        } else {
            return this.occupancy.equals(bundleContentsComponent.occupancy) && ItemStack.stacksEqual(this.stacks, bundleContentsComponent.stacks);
        }
    }

    public int hashCode() {
        return ItemStack.listHashCode(this.stacks);
    }

    public String toString() {
        return "OverFilledBundleContents" + this.stacks;
    }

    static {
        CODEC = ItemStack.CODEC.listOf().xmap(OverfilledBundleContentComponent::new, (component) -> component.stacks);
        PACKET_CODEC = ItemStack.PACKET_CODEC.collect(PacketCodecs.toList()).xmap(OverfilledBundleContentComponent::new, (component) -> component.stacks);
        NESTED_BUNDLE_OCCUPANCY = Fraction.getFraction(1, MAX_ITEMS);
    }

    public static class Builder {
        private final List<ItemStack> stacks;
        private Fraction occupancy;

        public Builder(OverfilledBundleContentComponent base) {
            this.stacks = new ArrayList<>(base.stacks);
            this.occupancy = base.occupancy;
        }

        public OverfilledBundleContentComponent.Builder clear() {
            this.stacks.clear();
            this.occupancy = Fraction.ZERO;
            return this;
        }

        private int addInternal(ItemStack stack) {
            if (stack.isStackable()) {
                for (int i = 0; i < this.stacks.size(); ++i) {
                    if (ItemStack.areItemsAndComponentsEqual(this.stacks.get(i), stack)) {
                        return i;
                    }
                }
            }
            return -1;
        }

        private int getMaxAllowed(ItemStack stack) {
            Fraction fraction = Fraction.ONE.subtract(this.occupancy);
            return Math.max(fraction.divideBy(OverfilledBundleContentComponent.getOccupancy(stack)).intValue(), 0);
        }

        private int add(ItemStack stack) {
            if (!stack.isEmpty() && stack.getItem().canBeNested() && !stack.isOf(ModItems.SCULK_BUNDLE)) {
                int i = Math.min(stack.getCount(), this.getMaxAllowed(stack));
                if (i == 0) {
                    return 0;
                } else {
                    this.occupancy = this.occupancy.add(OverfilledBundleContentComponent.getOccupancy(stack).multiplyBy(Fraction.getFraction(i, 1)));
                    /*
                    int j = this.addInternal(stack);
                    if (j != -1) {
                        ItemStack itemStack = this.stacks.remove(j);
                        ItemStack itemStack2 = itemStack.copyWithCount(itemStack.getCount() + i);
                        stack.decrement(i);

                        this.stacks.addFirst(itemStack2);
                    } else {
                        this.stacks.addFirst(stack.split(i));
                    }
                     */
                    ItemStack spiltStack = stack.split(i);
                    this.stacks.addFirst(spiltStack);

                    return i;
                }
            } else {
                return 0;
            }
        }

        public void saveAll(PlayerEntity player) {
            List<ItemStack> itemStacks = new ArrayList<>();
            for (int i = 0; i < player.getInventory().size(); i++) {
                ItemStack slotStack = player.getInventory().getStack(i);
                if (slotStack.isEmpty()) continue;
                itemStacks.add(slotStack);
            }

            if (SculkLatch.isTrinketsLoaded) {
                Optional<TrinketComponent> trinketComponent = TrinketsApi.getTrinketComponent(player);
                if (trinketComponent.isEmpty()) {
                    return;
                }
                for (Pair<SlotReference, ItemStack> slotReferenceItemStackPair : trinketComponent.get().getAllEquipped()) {
                    itemStacks.add(slotReferenceItemStackPair.getRight());
                }
            }

            // Sort for rarity
            itemStacks.sort(Comparator.comparing((stack) -> stack.getRarity().ordinal() - 3));

            // Sort for named items
            itemStacks.sort((stack, stack2) -> stack.getName() != stack.getItemName() ? 1 : (stack2.getName() != stack2.getItemName() ? -1 : 0));

            // Sort for enchantments
            itemStacks.sort(Comparator.comparingInt(stack -> stack.getEnchantments().getEnchantments().size()));



            for (ItemStack itemStack : itemStacks.reversed()) {
                this.add(itemStack);
            }

        }

        public void ejectAll(PlayerEntity player) {
            if (player.getWorld().isClient() || this.stacks.isEmpty()) return;
            for (ItemStack stack: this.stacks) {

                if (!player.getInventory().insertStack(stack)) {
                    player.getWorld().spawnEntity(new ItemEntity(player.getWorld(), player.getX(), player.getY(), player.getZ(), stack));
                }
            }

            this.clear();
        }

        @Nullable
        public ItemStack removeFirst() {
            if (this.stacks.isEmpty()) {
                return null;
            } else {
                ItemStack itemStack = this.stacks.removeFirst().copy();
                this.occupancy = this.occupancy.subtract(OverfilledBundleContentComponent.getOccupancy(itemStack).multiplyBy(Fraction.getFraction(itemStack.getCount(), 1)));
                return itemStack;
            }
        }

        public Fraction getOccupancy() {
            return this.occupancy;
        }

        public OverfilledBundleContentComponent build() {
            return new OverfilledBundleContentComponent(List.copyOf(this.stacks), this.occupancy);
        }
    }
}