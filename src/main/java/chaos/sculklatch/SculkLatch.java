package chaos.sculklatch;

import chaos.sculklatch.GameEvent.ModGameEvents;
import chaos.sculklatch.blocks.ModBlocks;
import chaos.sculklatch.commands.ModCommands;
import chaos.sculklatch.items.ModItems;
import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SculkLatch implements ModInitializer {
    public static final Logger LOGGER = LoggerFactory.getLogger("sculk-latch");
	public static final String MOD_ID = "sculk-latch";

	@Override
	public void onInitialize() {
		ModItems.registerModItems();
		ModBlocks.registerModItems();
		ModCommands.registerModCommands();
		ModGameEvents.registerModGameEvents();

		LOGGER.info("Hello Sculky world");
	}
}