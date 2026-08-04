package com.cognitio.herbology.item;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LayeredCauldronBlock;

public class WoodenSpoonItem extends Item {
    public WoodenSpoonItem(Properties properties) {
        super(properties);
    }

    @Override
    public int getUseDuration(ItemStack stack, LivingEntity entity) {
        return 72000; // Duração longa o suficiente para a fervura
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.BRUSH; // Animação de escovar (vai e vem suave), excelente para "mexer" o caldeirão
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        Player player = context.getPlayer();

        if (player != null && level.getBlockState(pos).is(Blocks.WATER_CAULDRON)) {
            if (level.getBlockState(pos).getValue(LayeredCauldronBlock.LEVEL) == 3) {
                // Inicia o uso contínuo (ativa a animação)
                player.startUsingItem(context.getHand());
                return InteractionResult.CONSUME;
            }
        }
        return super.useOn(context);
    }
}
