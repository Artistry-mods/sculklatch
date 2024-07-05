package chaos.sculklatch.mixin;

import chaos.sculklatch.blocks.ModBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.HopperBlock;
import net.minecraft.block.entity.Hopper;
import net.minecraft.block.entity.HopperBlockEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(HopperBlockEntity.class)
public class HopperBlockEntityMixin {
    // Prevent hoppers from taking out items from Sculk Chests
    @Inject(at = @At("HEAD"), method = "extract(Lnet/minecraft/world/World;Lnet/minecraft/block/entity/Hopper;)Z", cancellable = true)
    private static void extract(World world, Hopper hopper, CallbackInfoReturnable<Boolean> cir) {
        BlockPos blockPos = BlockPos.ofFloored(hopper.getHopperX(), hopper.getHopperY() + 1, hopper.getHopperZ());
        BlockState blockState = world.getBlockState(blockPos);
        Block block = blockState.getBlock();
        if (block == ModBlocks.SCULK_CHEST) cir.setReturnValue(false);
    }

    // Prevent hoppers from inserting items into Sculk Chests
    @Inject(at = @At("HEAD"), method = "insert", cancellable = true)
    private static void insert(World world, BlockPos pos, BlockState state, Inventory inventory, CallbackInfoReturnable<Boolean> cir) {
        Direction direction = state.get(HopperBlock.FACING);
        pos = pos.offset(direction);
        BlockPos blockPos = BlockPos.ofFloored(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5);
        BlockState blockState = world.getBlockState(blockPos);
        Block block = blockState.getBlock();
        if (block == ModBlocks.SCULK_CHEST) cir.setReturnValue(false);
    }
}
