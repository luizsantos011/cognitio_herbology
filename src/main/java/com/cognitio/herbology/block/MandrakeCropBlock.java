package com.cognitio.herbology.block;

import com.cognitio.core.perception.FrenzyEngine;
import com.cognitio.herbology.registry.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;

public class MandrakeCropBlock extends CropBlock {

    public MandrakeCropBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected ItemLike getBaseSeedId() {
        return ModItems.MANDRAKE_SEEDS.get();
    }

    @Override
    public BlockState playerWillDestroy(Level level, BlockPos pos, BlockState state, Player player) {
        if (!level.isClientSide()) {
            // Verifica se a planta está no estágio final de crescimento
            if (this.isMaxAge(state)) {
                
                // O Grito da Mandrágora (Apenas sonoro, o Frenesi agora vem do inventário)
                level.playSound(null, pos, SoundEvents.GHAST_SCREAM, SoundSource.BLOCKS, 2.0F, 1.5F);
            }
        }
        return super.playerWillDestroy(level, pos, state, player);
    }
}
