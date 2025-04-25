package chaos.sculklatch.mixin;

import chaos.sculklatch.blocks.ModBlocks;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.ChestBlock;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemPlacementContext;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ChestBlock.class)
public abstract class ChestPlaceMixin {
    @ModifyReturnValue(at = @At("RETURN"), method = "getPlacementState")
    public BlockState sculkLatch$placeSculkChest(BlockState original, ItemPlacementContext ctx) {
        if (ctx.getWorld().getBlockState(ctx.getBlockPos().down()).getBlock() == Blocks.SCULK_CATALYST) {
            FluidState fluidState = ctx.getWorld().getFluidState(ctx.getBlockPos());
            return ModBlocks.SCULK_CHEST.getDefaultState()
                    .with(ChestBlock.FACING, ctx.getHorizontalPlayerFacing().getOpposite())
                    .with(ChestBlock.WATERLOGGED, fluidState.getFluid() == Fluids.WATER);
        }
        return original;
    }
}
