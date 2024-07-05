package chaos.sculklatch.mixin;

import chaos.sculklatch.SculkLatch;
import chaos.sculklatch.blocks.ModBlocks;
import net.minecraft.block.SculkBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.gen.feature.SculkPatchFeature;
import net.minecraft.world.gen.feature.SculkPatchFeatureConfig;
import net.minecraft.world.gen.feature.util.FeatureContext;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SculkPatchFeature.class)
public abstract class SculkSpreadJawMixin {

    @Shadow
    protected abstract boolean canGenerate(WorldAccess world, BlockPos pos);

    @Inject(at = @At("RETURN"), method = "generate")
    public void generate(FeatureContext<SculkPatchFeatureConfig> context, CallbackInfoReturnable<Boolean> cir) {
        BlockPos blockPos = context.getOrigin();
        StructureWorldAccess structureWorldAccess = context.getWorld();

        if (this.canGenerate(structureWorldAccess, blockPos)) {
            Random random = context.getRandom();
            for (int i = 0; i < SculkLatch.SCULK_JAW_PLACE_TRIES; ++i) {
                BlockPos blockPos3 = blockPos.add(random.nextInt(5) - 2, 0, random.nextInt(5) - 2);
                if (structureWorldAccess.getBlockState(blockPos3).getBlock() instanceof SculkBlock && structureWorldAccess.getBlockState(blockPos3.up()).isAir() && structureWorldAccess.getBlockState(blockPos3.down()).isSideSolidFullSquare(structureWorldAccess, blockPos3.down(), Direction.UP)) {
                    structureWorldAccess.setBlockState(blockPos3, ModBlocks.SCULK_JAW.getDefaultState(), 3);
                    break;
                }
            }
        }
    }
}
