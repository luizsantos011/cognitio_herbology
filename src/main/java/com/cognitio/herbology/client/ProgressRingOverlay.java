package com.cognitio.herbology.client;

import com.cognitio.herbology.CognitioHerbology;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.BufferUploader;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.RenderGuiEvent;
import org.joml.Matrix4f;

@EventBusSubscriber(modid = CognitioHerbology.MODID, value = Dist.CLIENT, bus = EventBusSubscriber.Bus.GAME)
public class ProgressRingOverlay {

    private static int progressTicks = 0;
    private static final int MAX_TICKS = 300; // 15 seconds to full ring

    @SubscribeEvent
    public static void onClientTick(ClientTickEvent.Pre event) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null || mc.level == null) return;

        boolean isProcessing = false;

        // Verifica se está segurando o botão direito com a pá de madeira mirando no caldeirão
        if (mc.options.keyUse.isDown() && mc.player.getMainHandItem().is(com.cognitio.herbology.registry.ModItems.WOODEN_SPOON.get())) {
            if (mc.hitResult != null && mc.hitResult.getType() == HitResult.Type.BLOCK) {
                BlockHitResult blockHit = (BlockHitResult) mc.hitResult;
                if (mc.level.getBlockState(blockHit.getBlockPos()).is(Blocks.CAULDRON) || 
                    mc.level.getBlockState(blockHit.getBlockPos()).is(Blocks.WATER_CAULDRON)) {
                    isProcessing = true;
                }
            }
        }
        
        if (isProcessing) {
            if (progressTicks < MAX_TICKS) {
                progressTicks++;
                if (progressTicks >= MAX_TICKS) {
                    BlockHitResult hit = (BlockHitResult) mc.hitResult;
                    net.neoforged.neoforge.network.PacketDistributor.sendToServer(
                        new com.cognitio.herbology.network.CauldronProcessPayload(hit.getBlockPos())
                    );
                    progressTicks = 0; // Reseta depois de enviar
                }
            }
        } else {
            // Regride 2x mais rápido
            if (progressTicks > 0) {
                progressTicks -= 2;
                if (progressTicks < 0) progressTicks = 0;
            }
        }
    }

    @SubscribeEvent
    public static void onRenderGui(RenderGuiEvent.Post event) {
        if (progressTicks <= 0) return;

        Minecraft mc = Minecraft.getInstance();
        
        float partialTick = event.getPartialTick().getGameTimeDeltaPartialTick(true);
        boolean isHolding = mc.options.keyUse.isDown() && mc.player != null && mc.player.getMainHandItem().is(com.cognitio.herbology.registry.ModItems.WOODEN_SPOON.get());
        
        float smoothProgressTicks = progressTicks;
        if (isHolding && progressTicks < MAX_TICKS) {
            smoothProgressTicks += partialTick;
        } else if (!isHolding && progressTicks > 0) {
            smoothProgressTicks -= (partialTick * 2.0f);
        }
        
        float progress = net.minecraft.util.Mth.clamp(smoothProgressTicks / MAX_TICKS, 0.0f, 1.0f);

        int screenWidth = mc.getWindow().getGuiScaledWidth();
        int screenHeight = mc.getWindow().getGuiScaledHeight();
        
        float x = screenWidth / 2.0f;
        float y = screenHeight / 2.0f;

        float t = 2.0f; // Espessura
        float r = 10.0f; // Raio

        float totalLength = 80.0f; // 4 lados de 20
        float drawLength = progress * totalLength;

        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.setShader(GameRenderer::getPositionColorShader);

        Tesselator tesselator = Tesselator.getInstance();
        BufferBuilder bufferbuilder = tesselator.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);
        Matrix4f matrix4f = event.getGuiGraphics().pose().last().pose();

        // 1. Metade Direita do Topo (Comprimento 10)
        if (drawLength > 0) {
            float len = Math.min(drawLength, r);
            addQuad(bufferbuilder, matrix4f, x, y - r, x + len, y - r + t);
            drawLength -= len;
        }
        
        // 2. Lado Direito (Comprimento 20)
        if (drawLength > 0) {
            float len = Math.min(drawLength, r * 2);
            addQuad(bufferbuilder, matrix4f, x + r - t, y - r, x + r, y - r + len);
            drawLength -= len;
        }

        // 3. Lado Inferior (Comprimento 20, desenhando da direita para a esquerda)
        if (drawLength > 0) {
            float len = Math.min(drawLength, r * 2);
            addQuad(bufferbuilder, matrix4f, x + r - len, y + r - t, x + r, y + r);
            drawLength -= len;
        }

        // 4. Lado Esquerdo (Comprimento 20, desenhando de baixo para cima)
        if (drawLength > 0) {
            float len = Math.min(drawLength, r * 2);
            addQuad(bufferbuilder, matrix4f, x - r, y + r - len, x - r + t, y + r);
            drawLength -= len;
        }

        // 5. Metade Esquerda do Topo (Comprimento 10, desenhando da esquerda para o centro)
        if (drawLength > 0) {
            float len = Math.min(drawLength, r);
            addQuad(bufferbuilder, matrix4f, x - r, y - r, x - r + len, y - r + t);
        }

        BufferUploader.drawWithShader(bufferbuilder.buildOrThrow());
    }

    private static void addQuad(BufferBuilder builder, Matrix4f matrix, float x1, float y1, float x2, float y2) {
        // Vertex mapping for QUADS
        builder.addVertex(matrix, x1, y2, 0).setColor(255, 255, 255, 255);
        builder.addVertex(matrix, x2, y2, 0).setColor(255, 255, 255, 255);
        builder.addVertex(matrix, x2, y1, 0).setColor(255, 255, 255, 255);
        builder.addVertex(matrix, x1, y1, 0).setColor(255, 255, 255, 255);
    }
}
