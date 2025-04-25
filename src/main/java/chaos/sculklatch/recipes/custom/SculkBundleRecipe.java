package chaos.sculklatch.recipes.custom;

import chaos.sculklatch.items.ModItems;
import chaos.sculklatch.recipes.ModRecipes;
import net.minecraft.item.BundleItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.SpecialCraftingRecipe;
import net.minecraft.recipe.book.CraftingRecipeCategory;
import net.minecraft.recipe.input.CraftingRecipeInput;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.world.World;

public class SculkBundleRecipe extends SpecialCraftingRecipe {
    public SculkBundleRecipe(CraftingRecipeCategory category) {
        super(category);
    }

    @Override
    public boolean matches(CraftingRecipeInput input, World world) {
        int sculkLatchCount = 0;
        int bundles = 0;
        for(int k = 0; k < input.getStackCount(); ++k) {
            ItemStack itemStack = input.getStackInSlot(k);
            if (!itemStack.isEmpty()) {
                if (itemStack.getItem() == ModItems.SCULK_LATCH) {
                    sculkLatchCount ++;
                }
                if (itemStack.getItem() == Items.BUNDLE) {
                    bundles ++;
                }
            }
        }
        return sculkLatchCount == 1 && bundles == 1;
    }

    @Override
    public ItemStack craft(CraftingRecipeInput input, RegistryWrapper.WrapperLookup lookup) {
        ItemStack itemStack = ItemStack.EMPTY;

        for(int i = 0; i < input.getStackCount(); ++i) {
            ItemStack itemStack2 = input.getStackInSlot(i);
            if (!itemStack2.isEmpty()) {
                Item item = itemStack2.getItem();
                if (item instanceof BundleItem) {
                    itemStack = itemStack2;
                }
            }
        }

        return itemStack.copyComponentsToNewStack(ModItems.SCULK_BUNDLE, 1);
    }

    @Override
    public RecipeSerializer<? extends SculkBundleRecipe> getSerializer() {
        return ModRecipes.SCULK_LATCH_BUNDLE;
    }
}
