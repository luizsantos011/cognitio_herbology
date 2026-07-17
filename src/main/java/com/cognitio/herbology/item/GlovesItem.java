package com.cognitio.herbology.item;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.InteractionHand;
import org.jetbrains.annotations.NotNull;

public class GlovesItem extends Item {
    public GlovesItem(Properties properties) {
        super(properties);
    }

    @Override
    public void inventoryTick(@NotNull ItemStack stack, @NotNull Level level, @NotNull Entity entity, int slotId, boolean isSelected) {
        super.inventoryTick(stack, level, entity, slotId, isSelected);
        
        if (!level.isClientSide() && entity instanceof Player player) {
            // Verifica se o item está na offhand
            if (player.getItemInHand(InteractionHand.OFF_HAND) == stack) {
                // TODO: Adicionar lógica das propriedades aqui
            }
        }
    }
}
