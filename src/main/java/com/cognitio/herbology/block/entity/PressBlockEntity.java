package com.cognitio.herbology.block.entity;

import com.cognitio.herbology.registry.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.items.ItemStackHandler;

public class PressBlockEntity extends BlockEntity {
    private final ItemStackHandler itemHandler = new ItemStackHandler(1) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
            if (level != null && !level.isClientSide) {
                level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 3);
            }
        }
    };

    public float crankProgress = 0.0f;
    public float renderProgress = 0.0f;
    public float prevRenderProgress = 0.0f;
    private boolean isFinished = false;

    public static void clientTick(net.minecraft.world.level.Level level, BlockPos pos, BlockState state, PressBlockEntity pBlockEntity) {
        pBlockEntity.prevRenderProgress = pBlockEntity.renderProgress;
        if (pBlockEntity.renderProgress < pBlockEntity.crankProgress) {
            pBlockEntity.renderProgress += 0.02f;
            if (pBlockEntity.renderProgress > pBlockEntity.crankProgress) {
                pBlockEntity.renderProgress = pBlockEntity.crankProgress;
            }
        } else if (pBlockEntity.renderProgress > pBlockEntity.crankProgress) {
            pBlockEntity.renderProgress -= 0.1f;
            if (pBlockEntity.renderProgress < pBlockEntity.crankProgress) {
                pBlockEntity.renderProgress = pBlockEntity.crankProgress;
            }
        }
    }

    public PressBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.PRESS_BE.get(), pos, state);
    }

    public ItemStackHandler getItemHandler() {
        return itemHandler;
    }

    public float getCrankProgress() {
        return crankProgress;
    }

    public void setCrankProgress(float progress) {
        this.crankProgress = progress;
        setChanged();
        if (level != null && !level.isClientSide) {
            level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 3);
        }
    }

    public boolean isFinished() {
        return isFinished;
    }

    public void setFinished(boolean finished) {
        this.isFinished = finished;
        setChanged();
        if (level != null && !level.isClientSide) {
            level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 3);
        }
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.put("inventory", itemHandler.serializeNBT(registries));
        tag.putFloat("crankProgress", crankProgress);
        tag.putBoolean("isFinished", isFinished);
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        itemHandler.deserializeNBT(registries, tag.getCompound("inventory"));
        crankProgress = tag.getFloat("crankProgress");
        isFinished = tag.getBoolean("isFinished");
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        CompoundTag tag = super.getUpdateTag(registries);
        saveAdditional(tag, registries);
        return tag;
    }

    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }
}
