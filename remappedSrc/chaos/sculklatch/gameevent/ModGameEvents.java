package chaos.sculklatch.game_event;

import net.fabricmc.fabric.api.registry.SculkSensorFrequencyRegistry;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.minecraft.world.event.GameEvent;

public class ModGameEvents {
    public static final GameEvent AMETHYST_BELL_HIT = register("amethyst_bell_hit", 32);

    private static GameEvent register(String id, int range) {
        return Registry.register(Registries.GAME_EVENT, new Identifier("sculk-latch", id), new GameEvent(id, range));
    }

    public static void registerModGameEvents() {
        // Give the Amethyst Bell game event a frequency of 11
        SculkSensorFrequencyRegistry.register(ModGameEvents.AMETHYST_BELL_HIT, 11);
    }
}
