package com.cognitio.herbology.block;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.FarmBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;

public class EnrichedFarmlandBlock extends FarmBlock {

    public EnrichedFarmlandBlock() {
        super(BlockBehaviour.Properties.of()
                .mapColor(MapColor.DIRT)
                .sound(SoundType.GRAVEL)
                .strength(0.6F)
                .randomTicks()
        );
    }

    @Override
    public void fallOn(net.minecraft.world.level.Level level, net.minecraft.world.level.block.state.BlockState state, net.minecraft.core.BlockPos pos, net.minecraft.world.entity.Entity entity, float fallDistance) {
        if (!level.isClientSide && net.neoforged.neoforge.common.CommonHooks.onFarmlandTrample(level, pos, com.cognitio.herbology.registry.ModBlocks.ENRICHED_DIRT.get().defaultBlockState(), fallDistance, entity)) {
            level.setBlock(pos, com.cognitio.herbology.registry.ModBlocks.ENRICHED_DIRT.get().defaultBlockState(), 3);
        }
        entity.causeFallDamage(fallDistance, 1.0F, entity.damageSources().fall());
    }

    @Override
    public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        int i = state.getValue(MOISTURE);
        if (!isNearWater(level, pos) && !level.isRainingAt(pos.above())) {
            if (i > 0) {
                level.setBlock(pos, state.setValue(MOISTURE, Integer.valueOf(i - 1)), 2);
            } else if (!isUnderCrops(level, pos)) {
                level.setBlock(pos, com.cognitio.herbology.registry.ModBlocks.ENRICHED_DIRT.get().defaultBlockState(), 3);
            }
        } else if (i < 7) {
            level.setBlock(pos, state.setValue(MOISTURE, Integer.valueOf(7)), 2);
        }
        
        // 75% chance to grant an extra random tick to the plant above (1.75x growth rate)
        if (random.nextFloat() < 0.75f) {
            BlockPos abovePos = pos.above();
            BlockState aboveState = level.getBlockState(abovePos);
            
            if (aboveState.isRandomlyTicking()) {
                if (aboveState.getBlock() instanceof net.minecraft.world.level.block.CropBlock || 
                    aboveState.getBlock() instanceof net.minecraft.world.level.block.StemBlock) {
                    
                    aboveState.randomTick(level, abovePos, random);
                }
            }
        }
    }

    private static boolean isNearWater(net.minecraft.world.level.LevelReader pLevel, BlockPos pPos) {
        for (BlockPos blockpos : BlockPos.betweenClosed(pPos.offset(-4, 0, -4), pPos.offset(4, 1, 4))) {
            if (pLevel.getFluidState(blockpos).is(net.minecraft.tags.FluidTags.WATER)) {
                return true;
            }
        }
        return net.neoforged.neoforge.common.FarmlandWaterManager.hasBlockWaterTicket(pLevel, pPos);
    }

    private static boolean isUnderCrops(net.minecraft.world.level.BlockGetter pLevel, BlockPos pPos) {
        net.minecraft.world.level.block.state.BlockState blockstate = pLevel.getBlockState(pPos.above());
        return blockstate.is(net.minecraft.tags.BlockTags.MAINTAINS_FARMLAND) || blockstate.getBlock() instanceof net.minecraft.world.level.block.CropBlock || blockstate.getBlock() instanceof net.minecraft.world.level.block.StemBlock || blockstate.getBlock() instanceof net.minecraft.world.level.block.AttachedStemBlock;
    }
}
