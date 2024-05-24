package chaos.sculklatch.commands;

import chaos.sculklatch.items.ModItems;
import chaos.sculklatch.items.custom.SculkBundleItem;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.loader.impl.lib.sat4j.core.Vec;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;

import static net.minecraft.server.command.CommandManager.literal;

public class ModCommands {
    public static void registerModCommands() {
        CommandRegistrationCallback.EVENT.register(ModCommands::addCommands);
    }

    private static void addCommands(CommandDispatcher<ServerCommandSource> serverCommandSourceCommandDispatcher, CommandRegistryAccess commandRegistryAccess, CommandManager.RegistrationEnvironment registrationEnvironment) {
        LiteralArgumentBuilder<ServerCommandSource> overFill = literal("test-over-fill");
        LiteralArgumentBuilder<ServerCommandSource> autoOverFill = literal("test-auto-over-fill");

        overFill.executes(context -> {
            if (context.getSource().getPlayer().getMainHandStack().getItem() instanceof SculkBundleItem) {
                SculkBundleItem.overFillBundle(context.getSource().getPlayer().getMainHandStack(), context.getSource().getPlayer().getOffHandStack());
            }
            return 1;
        });

        autoOverFill.executes(context -> {
            fillSculkBundles(context.getSource().getPlayer());
            return 1;
        });

        serverCommandSourceCommandDispatcher.register(overFill);
        serverCommandSourceCommandDispatcher.register(autoOverFill);
    }

    /*
    private static void addCommands(CommandDispatcher<FabricClientCommandSource> fabricClientCommandSourceCommandDispatcher, CommandRegistryAccess commandBuildContext) {
        LiteralArgumentBuilder<FabricClientCommandSource> overFill = literal("test-over-fill");
        LiteralArgumentBuilder<FabricClientCommandSource> autoOverFill = literal("test-auto-over-fill");

        overFill.executes(context -> {
            if (context.getSource().getPlayer().getMainHandStack().getItem() instanceof SculkBundleItem) {
                SculkBundleItem.overFillBundle(context.getSource().getPlayer().getMainHandStack(), context.getSource().getPlayer().getOffHandStack());
            }
            return 1;
        });

        autoOverFill.executes(context -> {
            fillSculkBundles(context.getSource().getPlayer());
            return 1;
        });

        fabricClientCommandSourceCommandDispatcher.register(overFill);
        fabricClientCommandSourceCommandDispatcher.register(autoOverFill);
    }


     */
    private static Vec<Integer> getSculkBundles(Inventory inventory) {
        Vec<Integer> slots = new Vec<>();
        for(int j = 0; j < inventory.size(); ++j) {
            ItemStack itemStack = inventory.getStack(j);
            if (itemStack.getItem().equals(ModItems.SCULK_BUNDLE)) {
                slots.push(j);
            }
        }
        return slots;
    }

    private static void fillSculkBundles(ServerPlayerEntity player) {
        int sculk_bundles = player.getInventory().count(ModItems.SCULK_BUNDLE);
        if (sculk_bundles != 0) {
            Vec<Integer> sculkBundleSlots = getSculkBundles(player.getInventory());
            for (int i = 0; i < sculkBundleSlots.size(); i++) {
                ItemStack sculkBundle = player.getInventory().getStack(sculkBundleSlots.get(i));
                for(int j = 0; j < player.getInventory().size(); ++j) {
                    ItemStack itemStack = player.getInventory().getStack(j);
                    SculkBundleItem.overFillBundle(sculkBundle, itemStack);
                }
            }
        }
    }
}
