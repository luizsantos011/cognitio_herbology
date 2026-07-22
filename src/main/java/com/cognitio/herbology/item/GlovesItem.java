package com.cognitio.herbology.item;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class GlovesItem extends Item {
    public GlovesItem(Properties properties) {
        super(properties);
    }

    /**
     * Verifica se o jogador está com as luvas equipadas (na mão principal ou secundária).
     */
    public static boolean hasGloves(Player player) {
        return player.getItemInHand(InteractionHand.MAIN_HAND).getItem() instanceof GlovesItem ||
               player.getItemInHand(InteractionHand.OFF_HAND).getItem() instanceof GlovesItem;
    }

    /**
     * Aplica dano de desgaste à luva equipada. 
     * Se estiver usando nas duas mãos, danifica a da mão principal.
     * Retorna true se a luva absorveu o dano com sucesso.
     */
    public static boolean damageGloves(Player player, int damage) {
        ItemStack glovesStack = null;
        InteractionHand handUsed = null;

        if (player.getItemInHand(InteractionHand.MAIN_HAND).getItem() instanceof GlovesItem) {
            glovesStack = player.getItemInHand(InteractionHand.MAIN_HAND);
            handUsed = InteractionHand.MAIN_HAND;
        } else if (player.getItemInHand(InteractionHand.OFF_HAND).getItem() instanceof GlovesItem) {
            glovesStack = player.getItemInHand(InteractionHand.OFF_HAND);
            handUsed = InteractionHand.OFF_HAND;
        }

        if (glovesStack != null && player instanceof ServerPlayer serverPlayer) {
            // Aplica o dano. Se quebrar, toca o som e destrói o item
            glovesStack.hurtAndBreak(damage, serverPlayer, player.getEquipmentSlotForItem(glovesStack));
            return true; // Dano foi absorvido pelas luvas
        }
        
        return false;
    }
}
