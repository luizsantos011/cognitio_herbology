package com.cognitio.herbology.client.renderer;

import com.cognitio.herbology.block.entity.PressBlockEntity;
import com.cognitio.herbology.client.ClientEvents;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

public class PressBlockEntityRenderer implements BlockEntityRenderer<PressBlockEntity> {

    public PressBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
    }

    @Override
    public void render(PressBlockEntity blockEntity, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        float progress = net.minecraft.util.Mth.lerp(partialTick, blockEntity.prevRenderProgress, blockEntity.renderProgress);
        
        poseStack.pushPose();
        
        poseStack.translate(0.5, 0.0, 0.5);
        poseStack.mulPose(Axis.YP.rotationDegrees(progress * 1080.0f));
        poseStack.translate(-0.5, -progress * 0.35, -0.5);
        
        BakedModel screwModel = Minecraft.getInstance().getModelManager().getModel(ClientEvents.PRESS_SCREW_MODEL);
        Minecraft.getInstance().getBlockRenderer().getModelRenderer().renderModel(
            poseStack.last(),
            bufferSource.getBuffer(RenderType.cutout()),
            blockEntity.getBlockState(),
            screwModel,
            1.0f, 1.0f, 1.0f,
            packedLight,
            packedOverlay
        );
        poseStack.popPose();

        ItemStack stack = blockEntity.getItemHandler().getStackInSlot(0);
        if (!stack.isEmpty()) {
            if (!blockEntity.isFinished()) {
                poseStack.pushPose();
                poseStack.translate(0.5, 0.15, 0.5);
                poseStack.scale(0.5f, 0.5f, 0.5f);
                poseStack.mulPose(Axis.XP.rotationDegrees(90));
                
                Minecraft.getInstance().getItemRenderer().renderStatic(
                    stack,
                    ItemDisplayContext.FIXED,
                    packedLight,
                    packedOverlay,
                    poseStack,
                    bufferSource,
                    blockEntity.getLevel(),
                    0
                );
                poseStack.popPose();
            } else {
                // Render liquid puddle
                net.minecraft.client.renderer.texture.TextureAtlasSprite sprite = Minecraft.getInstance().getTextureAtlas(net.minecraft.client.renderer.texture.TextureAtlas.LOCATION_BLOCKS).apply(net.minecraft.resources.ResourceLocation.withDefaultNamespace("block/water_still"));
                com.mojang.blaze3d.vertex.VertexConsumer builder = bufferSource.getBuffer(RenderType.translucent());
                org.joml.Matrix4f matrix4f = poseStack.last().pose();

                float minU = sprite.getU0();
                float maxU = sprite.getU1();
                float minV = sprite.getV0();
                float maxV = sprite.getV1();

                // Purple tint for Belladonna liquid
                float r = 0.5f; float g = 0.2f; float b = 0.7f; float a = 0.8f;
                float y = 2.01f / 16.0f;
                float minX = 3.0f / 16.0f; float maxX = 13.0f / 16.0f;
                float minZ = 3.0f / 16.0f; float maxZ = 13.0f / 16.0f;

                builder.addVertex(matrix4f, minX, y, maxZ).setColor(r, g, b, a).setUv(minU, maxV).setLight(packedLight).setNormal(0, 1, 0);
                builder.addVertex(matrix4f, maxX, y, maxZ).setColor(r, g, b, a).setUv(maxU, maxV).setLight(packedLight).setNormal(0, 1, 0);
                builder.addVertex(matrix4f, maxX, y, minZ).setColor(r, g, b, a).setUv(maxU, minV).setLight(packedLight).setNormal(0, 1, 0);
                builder.addVertex(matrix4f, minX, y, minZ).setColor(r, g, b, a).setUv(minU, minV).setLight(packedLight).setNormal(0, 1, 0);
            }
        }
    }
}
