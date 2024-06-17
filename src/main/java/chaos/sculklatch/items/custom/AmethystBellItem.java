package chaos.sculklatch.items.custom;

import chaos.sculklatch.SculkLatch;
import chaos.sculklatch.blocks.ModBlocks;
import chaos.sculklatch.gameevent.ModGameEvents;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;

public class AmethystBellItem extends Item {
    public AmethystBellItem(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        if (!world.isClient()) {
            world.playSound(null, user.getBlockPos(), SoundEvents.BLOCK_BELL_USE, SoundCategory.PLAYERS, 1F, 0.9F);
            world.playSound(null, user.getBlockPos(), SoundEvents.BLOCK_AMETHYST_BLOCK_RESONATE, SoundCategory.PLAYERS, 1F, 0.9F);
            world.emitGameEvent(ModGameEvents.AMETHYST_BELL_HIT, user.getPos(), new GameEvent.Emitter(user, ModBlocks.SCULK_CHEST.getDefaultState()));
            user.getItemCooldownManager().set(this, SculkLatch.AMETHYST_BELL_COOLDOWN);
            return TypedActionResult.success(user.getStackInHand(hand));
        }
        return super.use(world, user, hand);
    }
}
