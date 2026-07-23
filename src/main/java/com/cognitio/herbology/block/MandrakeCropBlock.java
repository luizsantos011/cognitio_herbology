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
        // A Mandrágora agora não faz nada de especial ao quebrar, o efeito ocorre no inventário
        return super.playerWillDestroy(level, pos, state, player);
    }
}
