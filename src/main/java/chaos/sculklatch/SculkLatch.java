package chaos.sculklatch;

import chaos.sculklatch.GameEvent.ModGameEvents;
import chaos.sculklatch.blocks.ModBlocks;
import chaos.sculklatch.items.ModItems;
import net.fabricmc.api.ModInitializer;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SculkLatch implements ModInitializer {
    public static final Logger LOGGER = LoggerFactory.getLogger("Sculk latch");
    public static final String MOD_ID = "sculk-latch";

    public static final int AMETHYST_BELL_COOLDOWN = 30 * 20; //How long the amethyst bell goes on to cooldown (sec * tickPerSec)
    public static final int SCARED_TIMER = 3 * 20; //The time the sculk chest is scared (sec * tickPerSec)
    public static final int SCULK_CHEST_RANGE = 8; //The distance the chest can get stunned from
    public static final SpriteIdentifier FULLY_SCULKED_CHEST_TEXTURE = new SpriteIdentifier(new Identifier("textures/atlas/chest.png"), new Identifier("sculk-latch", "entity/chest/sculk_chest_full_sculked"));
    public static final SpriteIdentifier SCARED_SCULKED_CHEST_TEXTURE = new SpriteIdentifier(new Identifier("textures/atlas/chest.png"), new Identifier("sculk-latch", "entity/chest/sculk_chest_scared_sculked"));
    public static final int SCULK_JAW_PLACE_TRIES = 10; // the amount of tries the program gets to place the jaw

    @Override
    public void onInitialize() {
        ModItems.registerModItems();
        ModBlocks.registerModItems();
        ModGameEvents.registerModGameEvents();

        LOGGER.info("Hello Sculky world");
    }
}