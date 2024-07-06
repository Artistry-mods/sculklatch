package chaos.sculklatch.commands;

import chaos.sculklatch.items.custom.components.ModDataComponentTypes;
import chaos.sculklatch.items.custom.components.custom.OverfilledBundleContentComponent;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.item.ItemStack;
import net.minecraft.server.command.ServerCommandSource;

import java.util.Objects;

import static net.minecraft.server.command.CommandManager.literal;

public class ModCommands {
    public static void registerModCommands() {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {

            LiteralArgumentBuilder<ServerCommandSource> grub = literal("fillBundle");
            grub.executes(context -> {
                ItemStack stack = Objects.requireNonNull(context.getSource().getPlayer()).getMainHandStack();
                OverfilledBundleContentComponent bundleContentsComponent = stack.get(ModDataComponentTypes.OVERFILLED_BUNDLE_CONTENTS);
                if (bundleContentsComponent == null) return 0;
                OverfilledBundleContentComponent.Builder builder = new OverfilledBundleContentComponent.Builder(bundleContentsComponent);
                builder.saveAll(context.getSource().getPlayer());
                stack.set(ModDataComponentTypes.OVERFILLED_BUNDLE_CONTENTS, builder.build());
                return 1;
            });

            dispatcher.register(grub);
        });
    }
}
