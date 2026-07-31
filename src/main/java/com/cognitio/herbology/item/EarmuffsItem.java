package com.cognitio.herbology.item;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.Equipable;
import net.minecraft.world.item.Item;

public class EarmuffsItem extends Item implements Equipable {
    public EarmuffsItem(Properties properties) {
        super(properties);
    }

    @Override
    public EquipmentSlot getEquipmentSlot() {
        return EquipmentSlot.HEAD;
    }

    @Override
    public net.minecraft.core.Holder<net.minecraft.sounds.SoundEvent> getEquipSound() {
        return net.minecraft.sounds.SoundEvents.ARMOR_EQUIP_LEATHER;
    }

    @Override
    public net.minecraft.world.InteractionResultHolder<net.minecraft.world.item.ItemStack> use(net.minecraft.world.level.Level level, net.minecraft.world.entity.player.Player player, net.minecraft.world.InteractionHand hand) {
        return this.swapWithEquipmentSlot(this, level, player, hand);
    }
}
