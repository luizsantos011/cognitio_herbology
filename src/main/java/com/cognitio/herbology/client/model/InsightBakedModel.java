package com.cognitio.herbology.client.model;

import com.cognitio.core.perception.PerceptionEngine;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.client.model.data.ModelData;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class InsightBakedModel implements BakedModel {

    private final BakedModel normalModel;
    private final BakedModel discernedModel;
    private final int requiredInsight;

    public InsightBakedModel(BakedModel normalModel, BakedModel discernedModel, int requiredInsight) {
        this.normalModel = normalModel;
        this.discernedModel = discernedModel;
        this.requiredInsight = requiredInsight;
    }

    private BakedModel getActiveModel() {
        Player localPlayer = Minecraft.getInstance().player;
        if (localPlayer != null) {
            int effectiveInsight = PerceptionEngine.getEffectivePerception(localPlayer);
            if (effectiveInsight >= requiredInsight) {
                return discernedModel;
            }
        }
        return normalModel;
    }

    @Override
    public @NotNull List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, @NotNull RandomSource rand, @NotNull ModelData data, @Nullable net.minecraft.client.renderer.RenderType renderType) {
        return getActiveModel().getQuads(state, side, rand, data, renderType);
    }

    @Override
    public @NotNull net.neoforged.neoforge.client.ChunkRenderTypeSet getRenderTypes(@NotNull BlockState state, @NotNull RandomSource rand, @NotNull ModelData data) {
        return net.neoforged.neoforge.client.ChunkRenderTypeSet.of(net.minecraft.client.renderer.RenderType.cutout());
    }

    @Override
    public @NotNull List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, @NotNull RandomSource rand) {
        return getActiveModel().getQuads(state, side, rand);
    }

    @Override
    public boolean useAmbientOcclusion() {
        return getActiveModel().useAmbientOcclusion();
    }

    @Override
    public boolean isGui3d() {
        return getActiveModel().isGui3d();
    }

    @Override
    public boolean usesBlockLight() {
        return getActiveModel().usesBlockLight();
    }

    @Override
    public boolean isCustomRenderer() {
        return getActiveModel().isCustomRenderer();
    }

    @Override
    public @NotNull net.minecraft.client.renderer.texture.TextureAtlasSprite getParticleIcon() {
        return getActiveModel().getParticleIcon();
    }

    @Override
    public @NotNull ItemTransforms getTransforms() {
        return getActiveModel().getTransforms();
    }

    @Override
    public @NotNull ItemOverrides getOverrides() {
        return getActiveModel().getOverrides();
    }

    @Override
    public @NotNull net.minecraft.client.renderer.texture.TextureAtlasSprite getParticleIcon(@NotNull ModelData data) {
        return getActiveModel().getParticleIcon(data);
    }
}
