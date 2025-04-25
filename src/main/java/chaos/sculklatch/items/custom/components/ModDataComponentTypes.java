package chaos.sculklatch.items.custom.components;

import chaos.sculklatch.SculkLatch;
import chaos.sculklatch.items.custom.components.custom.OverfilledBundleContentComponent;
import com.mojang.serialization.Codec;
import net.minecraft.component.ComponentType;
import net.minecraft.predicate.component.ComponentPredicate;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

import java.util.function.UnaryOperator;

public class ModDataComponentTypes {
    public static final ComponentType<OverfilledBundleContentComponent> OVERFILLED_BUNDLE_CONTENTS = register("overfilled_bundle_contents", (builder) -> {
        return builder.codec(OverfilledBundleContentComponent.CODEC).packetCodec(OverfilledBundleContentComponent.PACKET_CODEC).cache();
    });

    public static void registerModDataComponentTypes() {

    }

    private static <T> ComponentType<T> register(String id, UnaryOperator<ComponentType.Builder<T>> builderOperator) {
        return Registry.register(Registries.DATA_COMPONENT_TYPE, Identifier.of(SculkLatch.MOD_ID, id), (builderOperator.apply(ComponentType.builder())).build());
    }
}
