package com.cognitio.herbology.block;

import com.cognitio.core.perception.PerceptionEngine;
import com.mojang.serialization.MapCodec;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FlowerBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;

public class BelladonnaBlock extends FlowerBlock {
    public static final MapCodec<BelladonnaBlock> CODEC = simpleCodec(properties -> new BelladonnaBlock(properties));

    public BelladonnaBlock(BlockBehaviour.Properties properties) {
        // Belladonna uses Night Vision effect generically for FlowerBlock signature, though it's mainly decorative/toxic
        super(net.minecraft.world.effect.MobEffects.NIGHT_VISION, 5.0F, properties);
    }

    @Override
    public MapCodec<BelladonnaBlock> codec() {
        return CODEC;
    }

    @Override
    public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        super.randomTick(state, level, pos, random);

        // Morte do solo: Transforma terra fértil em Coarse Dirt
        BlockPos below = pos.below();
        BlockState stateBelow = level.getBlockState(below);
        
        if (stateBelow.is(BlockTags.DIRT) && !stateBelow.is(Blocks.COARSE_DIRT)) {
            level.setBlock(below, Blocks.COARSE_DIRT.defaultBlockState(), 3);
        }
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        super.animateTick(state, level, pos, random);
        
        if (level.isClientSide) {
            Player player = Minecraft.getInstance().player;
            if (player != null) {
                int insight = PerceptionEngine.getEffectivePerception(player);
                if (insight >= 100) {
                    if (random.nextInt(5) == 0) {
                        net.minecraft.world.phys.Vec3 offset = state.getOffset(level, pos);
                        double x = pos.getX() + offset.x + 0.2D + random.nextDouble() * 0.6D;
                        double y = pos.getY() + offset.y + 0.4D + random.nextDouble() * 0.3D;
                        double z = pos.getZ() + offset.z + 0.2D + random.nextDouble() * 0.6D;
                        level.addParticle(ParticleTypes.FALLING_OBSIDIAN_TEAR, x, y, z, 0.0D, 0.0D, 0.0D);
                    }
                }
            }
        }
    }
}
