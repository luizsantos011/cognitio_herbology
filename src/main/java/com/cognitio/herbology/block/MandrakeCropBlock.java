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
                
                // O Grito da Mandrágora
                level.playSound(null, pos, SoundEvents.GHAST_SCREAM, SoundSource.BLOCKS, 2.0F, 1.5F);

                // TODO: Adicionar luvas de couro grosso como checagem
                boolean wearingGloves = false; 

                if (!wearingGloves) {
                    // Dano físico nos tímpanos do jogador
                    player.hurt(player.damageSources().magic(), 4.0F);

                    // A Dor Existencial - Integração com o CognitioCore!
                    // Injeta 20 pontos base de Sobrecarga Mental.
                    FrenzyEngine.addFrenzy(player, 20f);
                }
            }
        }
        return super.playerWillDestroy(level, pos, state, player);
    }
}
