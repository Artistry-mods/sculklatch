package chaos.sculklatch.mixin;

import chaos.sculklatch.blocks.ModBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SculkBlock;
import net.minecraft.block.entity.SculkSpreadManager;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.WorldAccess;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.HashMap;
import java.util.Map;

import static net.minecraft.block.ChestBlock.FACING;


@Mixin(SculkBlock.class)
public class SculkSpreadMixin {

    @Unique
    private static final Map<Integer, Direction> FACING_MAP = new HashMap<>(Map.of(0, Direction.NORTH, 1, Direction.EAST, 2, Direction.SOUTH, 3, Direction.WEST));

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
        ///tp -1000655.500000 100.000000 -59887.500000
        if (world != null) {
            /*
            for (BlockPos potentialChestPos : BlockPos.iterate(raisedBlockPos.add(1, 0, 1), raisedBlockPos.add(-1, 0, -1))) {
                BlockState chestState = world.getBlockState(potentialChestPos);
                if (chestState.isOf(Blocks.CHEST)) {
                    BlockEntity chestBlockEntity = world.getBlockEntity(potentialChestPos);
                    System.out.println("checking if (chestBlockEntity instanceof ChestBlockEntity)");
                    System.out.println("pos is " + potentialChestPos + " and chestBlockEntity is " + chestBlockEntity);

                    if (chestBlockEntity != null) {
                        if (chestBlockEntity instanceof ChestBlockEntity) {

                            System.out.println("pulling items from the chest");
                            List<ItemStack> inventory = new ArrayList<>();
                            //Map<ItemStack, Integer> itemStackMap = new HashMap<>(Map.of());
                            for (int slot = 0; slot < ((ChestBlockEntity)chestBlockEntity).size(); slot++) {
                                ItemStack stack = ((ChestBlockEntity)chestBlockEntity).getStack(slot);
                                System.out.println("Storing item: " + stack + " in slot: " + slot);
                                inventory.add(slot, stack);
                                //itemStackMap.put(stack, slot);
                            }

                            System.out.println("clearing chestBlockEntity");
                            ((ChestBlockEntity)chestBlockEntity).clear();
                            BlockState sculkChestBlockState = ModBlocks.SCULK_CHEST.getDefaultState().with(FACING, chestState.get(FACING)).with(WATERLOGGED, chestState.get(WATERLOGGED));
                            world.setBlockState(potentialChestPos, sculkChestBlockState, Block.NOTIFY_ALL);
                            System.out.println("playing a sound");
                            world.playSound(null, blockPos, sculkChestBlockState.getSoundGroup().getPlaceSound(), SoundCategory.BLOCKS, 1.0F, 1.0F);

                            BlockEntity amethystblockEntity = world.getBlockEntity(potentialChestPos);
                            System.out.println("pos is " + potentialChestPos + " and chestBlockEntity is " + amethystblockEntity);
                            if (amethystblockEntity instanceof SculkChestBlockEntity) {
                                System.out.println("is amethyst block entity");
                                for (int i = 0; i < inventory.size() - 1; i++) {
                                    ItemStack itemStack = inventory.get(i);
                                    ((SculkChestBlockEntity) amethystblockEntity).setStack(i, itemStack);

                                    //inventory.forEach((stack) -> ((SculkChestBlockEntity) amethystblockEntity).setStack(slot, stack));
                                }
                                //itemStackMap.forEach((stack, slot) -> ((SculkChestBlockEntity) amethystblockEntity).setStack(slot, stack));
                            }
                        }
                    }
                }
            }
             */
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
                            Integer facingIndex = random.nextInt(4);
                            blockState = ModBlocks.SCULK_JAW.getDefaultState().with(FACING, FACING_MAP.get(facingIndex));
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
}
