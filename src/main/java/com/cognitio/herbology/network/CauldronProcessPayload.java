package com.cognitio.herbology.network;

import com.cognitio.herbology.CognitioHerbology;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public record CauldronProcessPayload(BlockPos pos) implements CustomPacketPayload {
    public static final Type<CauldronProcessPayload> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(CognitioHerbology.MODID, "cauldron_process"));

    public static final StreamCodec<FriendlyByteBuf, CauldronProcessPayload> STREAM_CODEC = StreamCodec.composite(
        BlockPos.STREAM_CODEC, CauldronProcessPayload::pos,
        CauldronProcessPayload::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
