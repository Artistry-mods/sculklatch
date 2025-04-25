package chaos.sculklatch.recipes;

import chaos.sculklatch.SculkLatch;
import chaos.sculklatch.recipes.custom.SculkBundleRecipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.SpecialCraftingRecipe;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModRecipes {
    public static RecipeSerializer<SculkBundleRecipe> SCULK_LATCH_BUNDLE = register("crafting_special_sculkbundle", new SpecialCraftingRecipe.SpecialRecipeSerializer<>(SculkBundleRecipe::new));

    private static RecipeSerializer<SculkBundleRecipe> register(String name, SpecialCraftingRecipe.SpecialRecipeSerializer<SculkBundleRecipe> serializer) {
        return Registry.register(Registries.RECIPE_SERIALIZER, Identifier.of(SculkLatch.MOD_ID, name), serializer);
    }

    public static void registerModRecipes() {

    }
}
