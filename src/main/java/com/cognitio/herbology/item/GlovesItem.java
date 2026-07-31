package com.cognitio.herbology.item;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.Equipable;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class GlovesItem extends Item implements Equipable {
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
        if (player.getItemInHand(InteractionHand.MAIN_HAND).getItem() instanceof GlovesItem) {
            glovesStack = player.getItemInHand(InteractionHand.MAIN_HAND);
        } else if (player.getItemInHand(InteractionHand.OFF_HAND).getItem() instanceof GlovesItem) {
            glovesStack = player.getItemInHand(InteractionHand.OFF_HAND);
        }

        if (glovesStack != null && player instanceof ServerPlayer serverPlayer) {
            // Aplica o dano. Se quebrar, toca o som e destrói o item
            glovesStack.hurtAndBreak(damage, serverPlayer, player.getEquipmentSlotForItem(glovesStack));
            return true; // Dano foi absorvido pelas luvas
        }
        
        return false;
    }

    @Override
    public EquipmentSlot getEquipmentSlot() {
        return EquipmentSlot.MAINHAND;
    }

    @Override
    public net.minecraft.core.Holder<net.minecraft.sounds.SoundEvent> getEquipSound() {
        return net.minecraft.sounds.SoundEvents.ARMOR_EQUIP_LEATHER;
    }

    @Override
    public net.minecraft.world.InteractionResultHolder<ItemStack> use(net.minecraft.world.level.Level level, Player player, InteractionHand hand) {
        return this.swapWithEquipmentSlot(this, level, player, hand);
    }
}
