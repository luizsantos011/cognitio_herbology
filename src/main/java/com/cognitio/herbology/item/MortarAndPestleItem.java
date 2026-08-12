package com.cognitio.herbology.item;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class MortarAndPestleItem extends Item {
    public MortarAndPestleItem(Properties properties) {
        super(properties);
    }

    @Override
    public boolean hasCraftingRemainingItem(ItemStack stack) {
        return true;
    }

    @Override
    public ItemStack getCraftingRemainingItem(ItemStack itemStack) {
        ItemStack remainder = itemStack.copy();
        remainder.setCount(1);
        int damage = remainder.getDamageValue() + 1;
        if (damage >= remainder.getMaxDamage()) {
            return ItemStack.EMPTY;
        }
        remainder.setDamageValue(damage);
        return remainder;
    }
}
