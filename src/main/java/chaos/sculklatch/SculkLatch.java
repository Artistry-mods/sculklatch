package chaos.sculklatch;

import chaos.sculklatch.blocks.ModBlocks;
import chaos.sculklatch.blocks.entities.ModBlockEntities;
import chaos.sculklatch.commands.ModCommands;
import chaos.sculklatch.game_event.ModGameEvents;
import chaos.sculklatch.items.ModItems;
import chaos.sculklatch.items.custom.components.ModDataComponentTypes;
import chaos.sculklatch.recipes.ModRecipes;
import chaos.sculklatch.tags.ModTags;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SculkLatch implements ModInitializer {
    public static final Logger LOGGER = LoggerFactory.getLogger("Sculk latch");
    public static final String MOD_ID = "sculk-latch";

    public static final int AMETHYST_BELL_COOLDOWN = 5 * 20; //How long the amethyst bell goes on to cooldown (sec * tickPerSec)
    public static final int SCARED_TIMER = 3 * 20; //The time the sculk chest is scared (sec * tickPerSec)
    public static final int SCULK_CHEST_RANGE = 8; //The distance the chest can get stunned from
    public static final int SCULK_JAW_PLACE_TRIES = 10; // the amount of tries the program gets to place the jaw
    public static final int SCULK_JAW_HEALTH = 30; // The health for the sculk jaw

    public static boolean isTrinketsLoaded = false; // cached because calling isModLoaded every time is a tiny bit expensive

    @Override
    public void onInitialize() {
        ModTags.registerModTags();
        ModItems.registerModItems();
        ModBlocks.registerModItems();
        ModRecipes.registerModRecipes();
        ModGameEvents.registerModGameEvents();
        ModBlockEntities.registerModBlockEntities();
        ModDataComponentTypes.registerModDataComponentTypes();

        isTrinketsLoaded = FabricLoader.getInstance().isModLoaded("trinkets");
        if (FabricLoader.getInstance().isDevelopmentEnvironment()) {
            ModCommands.registerModCommands();
        }


        LOGGER.info("Hello Sculky world");
    }
}