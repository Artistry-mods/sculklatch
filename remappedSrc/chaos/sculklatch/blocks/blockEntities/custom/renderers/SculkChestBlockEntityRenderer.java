package chaos.sculklatch.blocks.entities.custom.renderers;

import chaos.sculklatch.SculkLatch;
import chaos.sculklatch.blocks.ModBlocks;
import chaos.sculklatch.blocks.entities.custom.SculkChestBlockEntity;
import chaos.sculklatch.blocks.custom.SculkChestBlock;
import net.minecraft.block.*;
import net.minecraft.block.entity.ChestBlockEntity;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.block.entity.ChestBlockEntityRenderer;
import net.minecraft.client.render.block.entity.LightmapCoordinatesRetriever;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.world.World;

public class SculkChestBlockEntityRenderer extends ChestBlockEntityRenderer<SculkChestBlockEntity> {
    private final ModelPart singleChestLid;
    private final ModelPart singleChestBase;
    private final ModelPart singleChestLatch;

    public SculkChestBlockEntityRenderer(BlockEntityRendererFactory.Context ctx) {
        super(ctx);
        ModelPart modelPart = ctx.getLayerModelPart(EntityModelLayers.CHEST);
        this.singleChestBase = modelPart.getChild("bottom");
        this.singleChestLid = modelPart.getChild("lid");
        this.singleChestLatch = modelPart.getChild("lock");
    }

    @Override
    public void render(SculkChestBlockEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        World world = entity.getWorld();
        boolean bl = world != null;
        BlockState blockState = bl ? entity.getCachedState() : Blocks.CHEST.getDefaultState().with(ChestBlock.FACING, Direction.SOUTH);
        Block block = blockState.getBlock();
        if (block instanceof AbstractChestBlock<?> abstractChestBlock) {
            matrices.push();
            float f = blockState.get(ChestBlock.FACING).asRotation();
            matrices.translate(0.5F, 0.5F, 0.5F);
            matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(-f));
            matrices.translate(-0.5F, -0.5F, -0.5F);
            DoubleBlockProperties.PropertySource<? extends ChestBlockEntity> propertySource = abstractChestBlock.getBlockEntitySource(blockState, world, entity.getPos(), true);

            float g = propertySource.apply(ChestBlock.getAnimationProgressRetriever(entity)).get(tickDelta);
            g = 1.0F - g;
            g = 1.0F - g * g * g;
            int i = propertySource.apply(new LightmapCoordinatesRetriever<>()).applyAsInt(light);
            SpriteIdentifier spriteIdentifier = SculkLatch.FULLY_SCULKED_CHEST_TEXTURE;
            if (!entity.getWorld().getBlockState(entity.getPos()).isAir() && entity.getWorld().getBlockState(entity.getPos()).isOf(ModBlocks.SCULK_CHEST) && entity.getWorld().getBlockState(entity.getPos()).get(SculkChestBlock.IS_SCARED)) {
                spriteIdentifier = SculkLatch.SCARED_SCULKED_CHEST_TEXTURE;
            }
            VertexConsumer vertexConsumer = spriteIdentifier.getVertexConsumer(vertexConsumers, RenderLayer::getEntityCutout);

            this.render(matrices, vertexConsumer, this.singleChestLid, this.singleChestLatch, this.singleChestBase, g, i, overlay);

            matrices.pop();
        }
    }

    private void render(MatrixStack matrices, VertexConsumer vertices, ModelPart lid, ModelPart latch, ModelPart base, float openFactor, int light, int overlay) {
        lid.pitch = -(openFactor * 1.5707964F);
        latch.pitch = lid.pitch;
        lid.render(matrices, vertices, light, overlay);
        latch.render(matrices, vertices, light, overlay);
        base.render(matrices, vertices, light, overlay);
    }
}
