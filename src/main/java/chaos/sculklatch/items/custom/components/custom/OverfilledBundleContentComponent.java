package chaos.sculklatch.items.custom.components.custom;

import chaos.sculklatch.items.custom.components.ModDataComponentTypes;
import com.google.common.collect.Lists;
import com.mojang.serialization.Codec;
import net.minecraft.block.entity.BeehiveBlockEntity;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipData;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.screen.slot.Slot;
import org.apache.commons.lang3.math.Fraction;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;

public class OverfilledBundleContentComponent implements TooltipData {
    public static final OverfilledBundleContentComponent DEFAULT = new OverfilledBundleContentComponent(List.of());
    public static final Codec<OverfilledBundleContentComponent> CODEC;
    public static final PacketCodec<RegistryByteBuf, OverfilledBundleContentComponent> PACKET_CODEC;
    private static final Fraction NESTED_BUNDLE_OCCUPANCY;
    private static final int MAX_ITEMS = 16;  // Maximum number of items in the bundle
    private static final int ADD_TO_NEW_SLOT = -1;
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
        OverfilledBundleContentComponent bundleContentsComponent = stack.get(ModDataComponentTypes.OVERFILLED_BUNDLE_CONTENTS);
        if (bundleContentsComponent != null) {
            return NESTED_BUNDLE_OCCUPANCY.add(bundleContentsComponent.getOccupancy());
        } else {
            List<BeehiveBlockEntity.BeeData> list = stack.getOrDefault(DataComponentTypes.BEES, List.of());
            return !list.isEmpty() ? Fraction.ONE : Fraction.getFraction(1, stack.getMaxCount() * 1);
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
        CODEC = ItemStack.CODEC.listOf().xmap(OverfilledBundleContentComponent::new, (component) -> {
            return component.stacks;
        });
        PACKET_CODEC = ItemStack.PACKET_CODEC.collect(PacketCodecs.toList()).xmap(OverfilledBundleContentComponent::new, (component) -> {
            return component.stacks;
        });
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
            if (!stack.isEmpty() && stack.getItem().canBeNested()) {
                int i = Math.min(stack.getCount(), this.getMaxAllowed(stack));
                if (i == 0) {
                    return 0;
                } else {
                    this.occupancy = this.occupancy.add(OverfilledBundleContentComponent.getOccupancy(stack).multiplyBy(Fraction.getFraction(i, 1)));
                    int j = this.addInternal(stack);
                    if (j != -1) {
                        ItemStack itemStack = this.stacks.remove(j);
                        ItemStack itemStack2 = itemStack.copyWithCount(itemStack.getCount() + i);
                        stack.decrement(i);
                        this.stacks.addFirst(itemStack2);
                    } else {
                        this.stacks.addFirst(stack.split(i));
                    }

                    return i;
                }
            } else {
                return 0;
            }
        }

        public int add(Slot slot, PlayerEntity player) {
            ItemStack itemStack = slot.getStack();
            int i = this.getMaxAllowed(itemStack);
            return this.add(slot.takeStackRange(itemStack.getCount(), i, player));
        }

        public void saveAll(PlayerEntity player) {
            for (int i = 0; i < player.getInventory().size(); i++) {
                ItemStack itemStack = player.getInventory().getStack(i);
                this.add(itemStack);
            }
        }

        public void ejectAll(PlayerEntity player) {
            if (this.stacks.isEmpty()) return;
            System.out.println("ejecting");
            for (ItemStack stack: this.stacks) {
                System.out.println(stack);
                System.out.println(stack.getMaxCount());
                System.out.println(stack.getCount());
                System.out.println(stack.split(stack.getMaxCount()));
                /*
                while (stack.getMaxCount() < stack.getCount()) {
                    System.out.println("is bigger");
                    player.getInventory().setStack(player.getInventory().getEmptySlot(), stack.split(stack.getMaxCount()));
                }

                 */
                player.getInventory().setStack(player.getInventory().getEmptySlot(), stack);
            }
            this.stacks.clear();
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
