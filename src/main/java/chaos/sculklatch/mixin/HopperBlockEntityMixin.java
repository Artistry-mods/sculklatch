package chaos.sculklatch.mixin;

import chaos.sculklatch.blocks.ModBlocks;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.HopperBlock;
import net.minecraft.block.entity.Hopper;
import net.minecraft.block.entity.HopperBlockEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(HopperBlockEntity.class)
public abstract class HopperBlockEntityMixin {
    // Prevent hoppers from taking out items from Sculk Chests
    @ModifyReturnValue(at = @At("RETURN"), method = "extract(Lnet/minecraft/world/World;Lnet/minecraft/block/entity/Hopper;)Z")
    private static boolean sculkLatch$preventHopperExtraction(boolean original, World world, Hopper hopper) {
        BlockPos blockPos = BlockPos.ofFloored(hopper.getHopperX(), hopper.getHopperY() + 1, hopper.getHopperZ());
        BlockState blockState = world.getBlockState(blockPos);
        Block block = blockState.getBlock();
        if (block == ModBlocks.SCULK_CHEST) return false;
        return original;
    }

    // Prevent hoppers from inserting items into Sculk Chests
    @ModifyReturnValue(at = @At("RETURN"), method = "insert")
    private static boolean sculkLatch$preventHopperInsertion(boolean original, World world, BlockPos pos, HopperBlockEntity blockEntity) {
        Direction direction = world.getBlockState(pos).get(HopperBlock.FACING);
        pos = pos.offset(direction);
        BlockPos blockPos = BlockPos.ofFloored(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5);
        BlockState blockState = world.getBlockState(blockPos);
        Block block = blockState.getBlock();
        if (block == ModBlocks.SCULK_CHEST) return false;
        return original;
    }
}
