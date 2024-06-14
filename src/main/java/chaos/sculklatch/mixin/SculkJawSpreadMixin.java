package chaos.sculklatch.mixin;

import chaos.sculklatch.blocks.ModBlocks;
import chaos.sculklatch.blocks.blockEntities.custom.SculkChestBlockEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.SculkBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.ChestBlockEntity;
import net.minecraft.block.entity.SculkSpreadManager;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.WorldAccess;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Map;

import static net.minecraft.block.ChestBlock.FACING;
import static net.minecraft.block.ChestBlock.WATERLOGGED;


@Mixin(SculkBlock.class)
public class SculkJawSpreadMixin {
    @Shadow
    private static int getDecay(SculkSpreadManager spreadManager, BlockPos cursorPos, BlockPos catalystPos, int charge) {
        return 0;
    }

    @Shadow
    private static boolean shouldNotDecay(WorldAccess world, BlockPos pos) {
        return false;
    }

    @Shadow
    private BlockState getExtraBlockState(WorldAccess world, BlockPos pos, Random random, boolean allowShrieker) {
        return null;
    }

    @Inject(at = @At("HEAD"), method = "spread", cancellable = true)
    public void spread(SculkSpreadManager.Cursor cursor, WorldAccess world, BlockPos catalystPos, Random random, SculkSpreadManager spreadManager, boolean shouldConvertToBlock, CallbackInfoReturnable<Integer> cir) {
        int cursorCharge = cursor.getCharge();
        BlockPos blockPos = cursor.getPos();
        BlockPos raisedBlockPos = blockPos.up();
        //replace chest with sculk chest
        for (BlockPos potentialChestPos : BlockPos.iterate(blockPos.add(1, 0, 1), blockPos.add(-1, 0, -1))) {
            BlockState chestState = world.getBlockState(potentialChestPos);
            if (chestState.isOf(Blocks.CHEST)) {
                BlockState sculkChestBlockState = ModBlocks.SCULK_CHEST.getDefaultState().with(FACING, chestState.get(FACING)).with(WATERLOGGED, chestState.get(WATERLOGGED));
                BlockEntity chestBlockEntity = world.getBlockEntity(potentialChestPos);
                if (chestBlockEntity instanceof ChestBlockEntity) {
                    Map<ItemStack, Integer> itemStackMap = new java.util.HashMap<>(Map.of());
                    for (int slot = 0; slot < ((ChestBlockEntity) chestBlockEntity).size(); slot++) {
                        itemStackMap.put(((ChestBlockEntity) chestBlockEntity).getStack(slot), slot);
                    }

                    ((ChestBlockEntity) chestBlockEntity).clear();
                    world.setBlockState(potentialChestPos, sculkChestBlockState, Block.NOTIFY_ALL);
                    world.playSound(null, blockPos, sculkChestBlockState.getSoundGroup().getPlaceSound(), SoundCategory.BLOCKS, 1.0F, 1.0F);
                    BlockEntity amethystblockEntity = world.getBlockEntity(potentialChestPos);

                    if (amethystblockEntity instanceof SculkChestBlockEntity) {
                        itemStackMap.forEach((stack, slot) -> ((SculkChestBlockEntity) amethystblockEntity).setStack(slot, stack));
                    }
                }
            }
        }
        if (cursorCharge != 0 && random.nextInt(spreadManager.getSpreadChance()) == 0) {
            boolean bl = blockPos.isWithinDistance(catalystPos, spreadManager.getMaxDistance());
            if (!bl && shouldNotDecay(world, blockPos)) {
                //make sensor, shrieker and sculk jaw spawn.
                int j = spreadManager.getExtraBlockChance();
                if (random.nextInt(j) < cursorCharge) {
                    BlockState blockState = this.getExtraBlockState(world, raisedBlockPos, random, spreadManager.isWorldGen());
                    //sculk jaw edit
                    if (random.nextInt(3) == 1) {
                        raisedBlockPos = raisedBlockPos.down();
                        blockState = ModBlocks.SCULK_JAW.getDefaultState();
                    }
                    world.setBlockState(raisedBlockPos, blockState, Block.NOTIFY_ALL);
                    world.playSound(null, blockPos, blockState.getSoundGroup().getPlaceSound(), SoundCategory.BLOCKS, 1.0F, 1.0F);
                }

                cir.setReturnValue(Math.max(0, cursorCharge - j));
            } else {
                cir.setReturnValue(random.nextInt(spreadManager.getDecayChance()) != 0 ? cursorCharge : cursorCharge - (bl ? 1 : getDecay(spreadManager, blockPos, catalystPos, cursorCharge)));
            }
        } else {
            cir.setReturnValue(cursorCharge);
        }
        cir.cancel();
    }
}
