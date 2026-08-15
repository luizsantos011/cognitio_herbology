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
    public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        super.randomTick(state, level, pos, random);
        
        // O arado vanilla tambem lida com umidade no randomTick, ja chamamos super.
        
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
}
