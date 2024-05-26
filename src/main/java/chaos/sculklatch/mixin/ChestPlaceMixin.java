package chaos.sculklatch.mixin;

import chaos.sculklatch.blocks.ModBlocks;
import chaos.sculklatch.blocks.custom.SculkChestBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.ChestBlock;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ChestBlock.class)
public class ChestPlaceMixin {
    @Shadow @Final public static DirectionProperty FACING;

    @Shadow @Final public static BooleanProperty WATERLOGGED;

    @Inject(at = @At("HEAD"), method = "onPlaced")
    public void onPlaced(World world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack itemStack, CallbackInfo ci) {
        if (world.getBlockState(pos.down()).getBlock() == Blocks.SCULK_CATALYST) {
            world.setBlockState(pos, ModBlocks.SCULK_CHEST.getDefaultState()
                    .with(SculkChestBlock.FACING, state.get(FACING))
                    .with(SculkChestBlock.WATERLOGGED, state.get(WATERLOGGED)));
            world.scheduleBlockTick(pos, world.getBlockState(pos).getBlock(), 10);
        }
    }
}
