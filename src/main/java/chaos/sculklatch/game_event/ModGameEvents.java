package chaos.sculklatch.game_event;

import chaos.sculklatch.SculkLatch;
import net.fabricmc.fabric.api.registry.SculkSensorFrequencyRegistry;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;
import net.minecraft.world.event.GameEvent;

public class ModGameEvents {
    public static final RegistryEntry.Reference<GameEvent> AMETHYST_BELL_HIT = register("amethyst_bell_hit");

    //public static final GameEvent AMETHYST_BELL_HIT = register("amethyst_bell_hit", 32);

    private static RegistryEntry.Reference<GameEvent> register(String id) {
        return Registry.registerReference(Registries.GAME_EVENT, Identifier.of(SculkLatch.MOD_ID, id), new GameEvent(16));
    }

    public static void registerModGameEvents() {
        // Give the Amethyst Bell game event a frequency of 11
        SculkSensorFrequencyRegistry.register(ModGameEvents.AMETHYST_BELL_HIT.registryKey(), 11);
    }
}
