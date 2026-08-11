package com.cognitio.herbology.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.LayeredCauldronBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import java.util.function.Supplier;

public class AlchemyCauldronBlock extends LayeredCauldronBlock {
    private final Supplier<Item> extractItem;
    private final boolean isMundane;

    public AlchemyCauldronBlock(Properties properties, Supplier<Item> extractItem, boolean isMundane) {
        super(net.minecraft.world.level.biome.Biome.Precipitation.NONE, net.minecraft.core.cauldron.CauldronInteraction.EMPTY, properties);
        this.extractItem = extractItem;
        this.isMundane = isMundane;
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        if (stack.is(com.cognitio.herbology.registry.ModItems.EMPTY_FLASK.get())) {
            if (!level.isClientSide) {
                ItemStack potionStack;
                if (isMundane) {
                    potionStack = new ItemStack(Items.POTION);
                    potionStack.set(net.minecraft.core.component.DataComponents.POTION_CONTENTS, new net.minecraft.world.item.alchemy.PotionContents(net.minecraft.world.item.alchemy.Potions.MUNDANE));
                } else {
                    potionStack = new ItemStack(extractItem.get());
                }

                player.awardStat(net.minecraft.stats.Stats.USE_CAULDRON);
                ItemStack newStack = net.minecraft.world.item.ItemUtils.createFilledResult(stack, player, potionStack);
                player.setItemInHand(hand, newStack);
                level.playSound(null, pos, net.minecraft.sounds.SoundEvents.BOTTLE_FILL, net.minecraft.sounds.SoundSource.BLOCKS, 1.0F, 1.0F);
                LayeredCauldronBlock.lowerFillLevel(state, level, pos);
            }
            return ItemInteractionResult.sidedSuccess(level.isClientSide);
        }
        return super.useItemOn(stack, state, level, pos, player, hand, hitResult);
    }
}
