package com.cognitio.herbology.registry;

import com.cognitio.herbology.CognitioHerbology;
import com.cognitio.herbology.block.MandrakeCropBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModBlocks {
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(CognitioHerbology.MODID);

    public static final DeferredBlock<Block> MANDRAKE_CROP = BLOCKS.register("mandrake_crop", 
            () -> new MandrakeCropBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.PLANT)
                    .noCollission()
                    .randomTicks()
                    .instabreak()
                    .noOcclusion()
                    .sound(SoundType.CROP)
                    .pushReaction(PushReaction.DESTROY)));

    public static final DeferredBlock<Block> SAYLORS_EYE = BLOCKS.register("saylors_eye",
            () -> new net.minecraft.world.level.block.SeaPickleBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.WATER)
                    .instabreak()
                    .sound(SoundType.SLIME_BLOCK)
                    .noOcclusion()
                    .lightLevel(state -> 5)
                    .pushReaction(PushReaction.DESTROY)));

    public static final DeferredBlock<Block> DISCERNED_SAYLORS_EYE = BLOCKS.register("discerned_saylors_eye",
            () -> new net.minecraft.world.level.block.SeaPickleBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.WATER)
                    .instabreak()
                    .sound(SoundType.SLIME_BLOCK)
                    .noOcclusion()
                    .lightLevel(state -> 5)
                    .pushReaction(PushReaction.DESTROY)));

    public static final DeferredBlock<Block> HOMUNCULUS_CAULDRON = BLOCKS.register("homunculus_cauldron",
            () -> new com.cognitio.herbology.block.AlchemyCauldronBlock(BlockBehaviour.Properties.ofFullCopy(net.minecraft.world.level.block.Blocks.WATER_CAULDRON),
                    com.cognitio.herbology.registry.ModItems.HOMUNCULUS_EXTRACT, false));

    public static final DeferredBlock<Block> MUNDANE_CAULDRON = BLOCKS.register("mundane_cauldron",
            () -> new com.cognitio.herbology.block.AlchemyCauldronBlock(BlockBehaviour.Properties.ofFullCopy(net.minecraft.world.level.block.Blocks.WATER_CAULDRON),
                    () -> net.minecraft.world.item.Items.POTION, true));

    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }
}
