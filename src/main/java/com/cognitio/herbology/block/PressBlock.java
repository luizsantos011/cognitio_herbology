package com.cognitio.herbology.block;

import com.cognitio.herbology.block.entity.PressBlockEntity;
import com.cognitio.herbology.registry.ModItems;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

public class PressBlock extends Block implements EntityBlock {
    public static final MapCodec<PressBlock> CODEC = simpleCodec(properties -> new PressBlock(properties));

    private static final net.minecraft.world.phys.shapes.VoxelShape SHAPE = net.minecraft.world.phys.shapes.Shapes.or(
        Block.box(2.0D, 0.0D, 2.0D, 14.0D, 16.0D, 14.0D), // Base e armação de madeira
        Block.box(5.0D, 16.0D, 5.0D, 11.0D, 20.0D, 11.0D) // Fuso e manivela
    );

    public PressBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    @Override
    public net.minecraft.world.phys.shapes.VoxelShape getShape(BlockState state, net.minecraft.world.level.BlockGetter level, BlockPos pos, net.minecraft.world.phys.shapes.CollisionContext context) {
        return SHAPE;
    }

    @Override
    public MapCodec<PressBlock> codec() {
        return CODEC;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new PressBlockEntity(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> net.minecraft.world.level.block.entity.BlockEntityTicker<T> getTicker(Level level, BlockState state, net.minecraft.world.level.block.entity.BlockEntityType<T> blockEntityType) {
        if (level.isClientSide()) {
            return (lvl, p, blockState, blockEntity) -> {
                if (blockEntity instanceof PressBlockEntity press) {
                    PressBlockEntity.clientTick(lvl, p, blockState, press);
                }
            };
        }
        return null;
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        if (level.getBlockEntity(pos) instanceof PressBlockEntity press) {
            if (press.isFinished()) {
                if (stack.is(ModItems.EMPTY_FLASK.get())) {
                    if (!level.isClientSide) {
                        // TODO: Verify if Homunculus Extract or Phantom Lymph should be dropped. 
                        // For now we will drop Phantom Lymph.
                        ItemStack lymph = new ItemStack(ModItems.PHANTOM_LYMPH.get());
                        ItemStack newStack = net.minecraft.world.item.ItemUtils.createFilledResult(stack, player, lymph);
                        player.setItemInHand(hand, newStack);
                        
                        press.setFinished(false);
                        press.setCrankProgress(0.0f);
                        press.getItemHandler().setStackInSlot(0, ItemStack.EMPTY);
                        level.playSound(null, pos, net.minecraft.sounds.SoundEvents.BOTTLE_FILL, net.minecraft.sounds.SoundSource.BLOCKS, 1.0f, 1.0f);
                    }
                    return ItemInteractionResult.sidedSuccess(level.isClientSide);
                }
                return ItemInteractionResult.FAIL;
            }

            if (press.getCrankProgress() == 0.0f && press.getItemHandler().getStackInSlot(0).isEmpty()) {
                if (stack.is(ModItems.BELLADONNA.get())) {
                    if (!level.isClientSide) {
                        ItemStack copy = stack.copy();
                        copy.setCount(1);
                        press.getItemHandler().setStackInSlot(0, copy);
                        if (!player.isCreative()) {
                            stack.shrink(1);
                        }
                        level.playSound(null, pos, net.minecraft.sounds.SoundEvents.GRASS_PLACE, net.minecraft.sounds.SoundSource.BLOCKS, 1.0f, 1.0f);
                    }
                    return ItemInteractionResult.sidedSuccess(level.isClientSide);
                }
            }
        }
        return super.useItemOn(stack, state, level, pos, player, hand, hitResult);
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        if (level.getBlockEntity(pos) instanceof PressBlockEntity press) {
            if (!press.getItemHandler().getStackInSlot(0).isEmpty() && !press.isFinished()) {
                if (!level.isClientSide) {
                    float newProgress = press.getCrankProgress() + 0.05f;
                    if (newProgress >= 1.0f) {
                        newProgress = 1.0f;
                        press.setFinished(true);
                        level.playSound(null, pos, net.minecraft.sounds.SoundEvents.BREWING_STAND_BREW, net.minecraft.sounds.SoundSource.BLOCKS, 1.0f, 1.0f);
                    } else {
                        level.playSound(null, pos, net.minecraft.sounds.SoundEvents.WOOD_BREAK, net.minecraft.sounds.SoundSource.BLOCKS, 0.5f, 1.0f + newProgress);
                    }
                    press.setCrankProgress(newProgress);
                }
                return InteractionResult.sidedSuccess(level.isClientSide);
            }
        }
        return super.useWithoutItem(state, level, pos, player, hitResult);
    }
}
