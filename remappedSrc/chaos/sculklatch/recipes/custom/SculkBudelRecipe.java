package chaos.sculklatch.recipes.custom;

import chaos.sculklatch.items.ModItems;
import chaos.sculklatch.recipes.ModRecipes;
import net.minecraft.inventory.RecipeInputInventory;
import net.minecraft.item.*;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.SpecialCraftingRecipe;
import net.minecraft.recipe.book.CraftingRecipeCategory;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

public class SculkBudelRecipe extends SpecialCraftingRecipe {
    public SculkBudelRecipe(Identifier id, CraftingRecipeCategory category) {
        super(id, category);
    }

    @Override
    public boolean matches(RecipeInputInventory inventory, World world) {
        int sculkLatchCount = 0;
        for (int i = 0; i < inventory.size(); ++i) {
            ItemStack itemStack2 = inventory.getStack(i);
            if (!itemStack2.isEmpty()) {
                if (itemStack2.getItem() == ModItems.SCULK_LATCH) {
                    sculkLatchCount ++;
                }
            }
        }
        return sculkLatchCount == 1 && inventory.count(Items.BUNDLE) == 1;
    }

    @Override
    public ItemStack craft(RecipeInputInventory inventory, DynamicRegistryManager registryManager) {
        System.out.println("crafting");
        ItemStack itemStack = ItemStack.EMPTY;

        for(int i = 0; i < inventory.size(); ++i) {
            ItemStack itemStack2 = inventory.getStack(i);
            if (!itemStack2.isEmpty()) {
                Item item = itemStack2.getItem();
                if (item instanceof BundleItem) {
                    System.out.println(item);
                    itemStack = itemStack2;
                }
            }
        }
        System.out.println(itemStack.getNbt());

        ItemStack resultStack = ModItems.SCULK_BUNDLE.getDefaultStack();
        if (itemStack.getNbt() != null && itemStack != ItemStack.EMPTY) {
            resultStack.setNbt(itemStack.getNbt().copy());
        }

        return resultStack;
    }

    @Override
    public boolean fits(int width, int height) {
        return width * height >= 2;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModRecipes.SCULK_LATCH_BUNDEL;
    }
}
