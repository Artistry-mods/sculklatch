package chaos.sculklatch.recipes;

import chaos.sculklatch.SculkLatch;
import chaos.sculklatch.recipes.custom.SculkBudelRecipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.SpecialRecipeSerializer;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModRecipes {
    public static RecipeSerializer<SculkBudelRecipe> SCULK_LATCH_BUNDEL = register("crafting_special_sculkbudel", new SpecialRecipeSerializer<>(SculkBudelRecipe::new));

    private static RecipeSerializer<SculkBudelRecipe> register(String name, SpecialRecipeSerializer<SculkBudelRecipe> serializer) {
        return Registry.register(Registries.RECIPE_SERIALIZER, new Identifier(SculkLatch.MOD_ID, name), serializer);
    }

    public static void registerModRecipes() {

    }
}
