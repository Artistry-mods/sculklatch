package chaos.sculklatch;

import chaos.sculklatch.blocks.ModBlocks;
import chaos.sculklatch.game_event.ModGameEvents;
import chaos.sculklatch.items.ModItems;
import chaos.sculklatch.recipes.ModRecipes;
import chaos.sculklatch.tags.ModTags;
import net.fabricmc.api.ModInitializer;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SculkLatch implements ModInitializer {
    public static final Logger LOGGER = LoggerFactory.getLogger("Sculk latch");
    public static final String MOD_ID = "sculk-latch";

    public static final int AMETHYST_BELL_COOLDOWN = 5 * 20; //How long the amethyst bell goes on to cooldown (sec * tickPerSec)
    public static final int SCARED_TIMER = 3 * 20; //The time the sculk chest is scared (sec * tickPerSec)
    public static final int SCULK_CHEST_RANGE = 8; //The distance the chest can get stunned from
    public static final SpriteIdentifier FULLY_SCULKED_CHEST_TEXTURE = new SpriteIdentifier(new Identifier("textures/atlas/chest.png"), new Identifier("sculk-latch", "entity/chest/sculk_chest_full_sculked"));
    public static final SpriteIdentifier SCARED_SCULKED_CHEST_TEXTURE = new SpriteIdentifier(new Identifier("textures/atlas/chest.png"), new Identifier("sculk-latch", "entity/chest/sculk_chest_scared_sculked"));
    public static final int SCULK_JAW_PLACE_TRIES = 10; // the amount of tries the program gets to place the jaw
    public static final int SCULK_JAW_HEALTH = 30; // The health for the sculk jaw



    @Override
    public void onInitialize() {
        ModTags.registerModTags();
        ModItems.registerModItems();
        ModBlocks.registerModItems();
        ModRecipes.registerModRecipes();
        ModGameEvents.registerModGameEvents();

        LOGGER.info("Hello Sculky world");
    }
}