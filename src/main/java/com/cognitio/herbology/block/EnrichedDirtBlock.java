package com.cognitio.herbology.block;

import com.cognitio.herbology.registry.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.common.ItemAbilities;
import net.neoforged.neoforge.common.ItemAbility;
import org.jetbrains.annotations.Nullable;

public class EnrichedDirtBlock extends Block {

    public EnrichedDirtBlock() {
        super(BlockBehaviour.Properties.of()
                .mapColor(net.minecraft.world.level.material.MapColor.DIRT)
                .sound(SoundType.GRAVEL)
                .strength(0.5F)
                .randomTicks()
        );
    }

    @Override
    public boolean canSustainPlant(BlockState state, BlockGetter level, BlockPos pos, Direction facing, net.neoforged.neoforge.common.IPlantable plantable) {
        // Assume all dirt plants can grow on it
        net.minecraft.world.level.block.state.BlockState plant = plantable.getPlant(level, pos.relative(facing));
        return true; 
    }

    @Nullable
    @Override
    public BlockState getToolModifiedState(BlockState state, UseOnContext context, ItemAbility itemAbility, boolean simulate) {
        if (itemAbility == ItemAbilities.HOE_TILL) {
            return ModBlocks.ENRICHED_FARMLAND.get().defaultBlockState();
        }
        return super.getToolModifiedState(state, context, itemAbility, simulate);
    }

    @Override
    public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        super.randomTick(state, level, pos, random);
        
        // 75% chance to grant an extra random tick to the plant above (1.75x growth rate)
        if (random.nextFloat() < 0.75f) {
            BlockPos abovePos = pos.above();
            BlockState aboveState = level.getBlockState(abovePos);
            
            if (aboveState.isRandomlyTicking()) {
                if (aboveState.getBlock() instanceof net.minecraft.world.level.block.CropBlock || 
                    aboveState.getBlock() instanceof net.minecraft.world.level.block.SaplingBlock ||
                    aboveState.getBlock() instanceof net.minecraft.world.level.block.StemBlock ||
                    aboveState.getBlock() instanceof net.minecraft.world.level.block.SweetBerryBushBlock ||
                    aboveState.getBlock() instanceof net.minecraft.world.level.block.SugarCaneBlock ||
                    aboveState.getBlock() instanceof net.minecraft.world.level.block.CactusBlock) {
                    
                    aboveState.randomTick(level, abovePos, random);
                }
            }
        }
    }
}
