package com.cognitio.herbology.block;

import com.cognitio.herbology.registry.ModItems;
import net.minecraft.core.BlockPos;
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
            boolean wearingEarmuffs = player.getItemBySlot(net.minecraft.world.entity.EquipmentSlot.HEAD).is(ModItems.EARMUFFS.get());
            if (!wearingEarmuffs) {
                level.playSound(null, pos, net.minecraft.sounds.SoundEvents.GHAST_SCREAM, net.minecraft.sounds.SoundSource.BLOCKS, 1.0F, 1.0F);
                // O grito enche frenesi!
                if (player instanceof net.minecraft.server.level.ServerPlayer serverPlayer) {
                    com.cognitio.core.perception.FrenzyEngine.addFrenzy(serverPlayer, 5.0f);
                }
            }
        }
        return super.playerWillDestroy(level, pos, state, player);
    }
}
