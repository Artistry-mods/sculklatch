package chaos.sculklatch.blocks.entities.custom.renderers;

import chaos.sculklatch.SculkLatchClient;
import chaos.sculklatch.blocks.entities.custom.SculkChestBlockEntity;
import net.minecraft.block.*;
import net.minecraft.client.render.*;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.block.entity.ChestBlockEntityRenderer;
import net.minecraft.client.render.block.entity.model.ChestBlockModel;
import net.minecraft.client.render.block.entity.state.ChestBlockEntityRenderState;
import net.minecraft.client.render.command.OrderedRenderCommandQueue;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.client.render.state.CameraRenderState;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.texture.SpriteHolder;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.RotationAxis;

public class SculkChestBlockEntityRenderer extends ChestBlockEntityRenderer<SculkChestBlockEntity> {

    private final ChestBlockModel singleChest;
    private final SpriteHolder materials;

    public SculkChestBlockEntityRenderer(BlockEntityRendererFactory.Context ctx) {
        super(ctx);
        this.materials = ctx.spriteHolder();
        this.singleChest = new ChestBlockModel(ctx.getLayerModelPart(EntityModelLayers.CHEST));
    }

    @Override
    public void render(ChestBlockEntityRenderState chestBlockEntityRenderState, MatrixStack matrixStack, OrderedRenderCommandQueue orderedRenderCommandQueue, CameraRenderState cameraRenderState) {
        matrixStack.push();
        matrixStack.translate(0.5F, 0.5F, 0.5F);
        matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(-chestBlockEntityRenderState.yaw));
        matrixStack.translate(-0.5F, -0.5F, -0.5F);
        float f = chestBlockEntityRenderState.lidAnimationProgress;
        f = 1.0F - f;
        f = 1.0F - f * f * f;
        SpriteIdentifier spriteIdentifier = SculkLatchClient.FULLY_SCULKED_CHEST_TEXTURE;
        RenderLayer renderLayer = spriteIdentifier.getRenderLayer(RenderLayer::getEntityCutout);
        Sprite sprite = this.materials.getSprite(spriteIdentifier);
        orderedRenderCommandQueue.submitModel(this.singleChest, f, matrixStack, renderLayer, chestBlockEntityRenderState.lightmapCoordinates, OverlayTexture.DEFAULT_UV, -1, sprite, 0, chestBlockEntityRenderState.crumblingOverlay);


        matrixStack.pop();
    }
}
