package chaos.sculklatch.mixin;

import chaos.sculklatch.blocks.ModBlocks;
import chaos.sculklatch.blocks.custom.SculkChestBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.ChestBlock;
import net.minecraft.block.entity.SculkCatalystBlockEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ChestBlock.class)
public class ChestPlaceMixin {
    @Inject(at = @At("HEAD"), method = "getPlacementState", cancellable = true)
    public void onPlaced(ItemPlacementContext ctx, CallbackInfoReturnable<BlockState> cir) {
        if (ctx.getWorld().getBlockState(ctx.getBlockPos().down()).getBlock() == Blocks.SCULK_CATALYST) {
            //ctx.getWorld().setBlockState(ctx.getBlockPos().up(), ModBlocks.SCULK_CHEST.getDefaultState());
            FluidState fluidState = ctx.getWorld().getFluidState(ctx.getBlockPos());
            cir.setReturnValue(ModBlocks.SCULK_CHEST.getDefaultState()
                    .with(ChestBlock.FACING, ctx.getHorizontalPlayerFacing().getOpposite())
                    .with(ChestBlock.WATERLOGGED, fluidState.getFluid() == Fluids.WATER));
        }
    }
}
