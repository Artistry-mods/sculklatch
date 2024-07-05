package chaos.sculklatch.items.custom.components;

import chaos.sculklatch.items.custom.components.custom.OverfilledBundleContentComponent;
import net.minecraft.component.ComponentType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

import java.util.function.UnaryOperator;

public class ModDataComponentTypes {
    public static final ComponentType<OverfilledBundleContentComponent> OVERFILLED_BUNDLE_CONTENTS = register("overfilled_bundle_contents", (builder) -> {
        return builder.codec(OverfilledBundleContentComponent.CODEC).packetCodec(OverfilledBundleContentComponent.PACKET_CODEC).cache();
    });

    private static <T> ComponentType<T> register(String id, UnaryOperator<ComponentType.Builder<T>> builderOperator) {
        return Registry.register(Registries.DATA_COMPONENT_TYPE, id, (builderOperator.apply(ComponentType.builder())).build());
    }
}
