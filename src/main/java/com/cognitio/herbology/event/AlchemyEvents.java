package com.cognitio.herbology.event;

import com.cognitio.herbology.CognitioHerbology;
import com.cognitio.herbology.registry.ModItems;
import com.cognitio.core.registry.ModPotions;
import net.minecraft.core.cauldron.CauldronInteraction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.block.LayeredCauldronBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.event.brewing.RegisterBrewingRecipesEvent;
import net.neoforged.neoforge.common.brewing.BrewingRecipeRegistry;
import net.neoforged.neoforge.common.brewing.IBrewingRecipe;

@EventBusSubscriber(modid = CognitioHerbology.MODID, bus = EventBusSubscriber.Bus.MOD)
public class AlchemyEvents {

    @SubscribeEvent
    public static void onCommonSetup(FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            // Interação com Caldeirão de Água: Raiz + Caldeirão Cheio = 3 Extratos
            CauldronInteraction.WATER.map().put(ModItems.MANDRAKE_ROOT.get(), (state, level, pos, player, hand, stack) -> {
                if (state.getValue(LayeredCauldronBlock.LEVEL) == 3) {
                    if (!level.isClientSide) {
                        stack.shrink(1);
                        
                        // Esvazia o caldeirão
                        level.setBlockAndUpdate(pos, net.minecraft.world.level.block.Blocks.CAULDRON.defaultBlockState());
                        level.playSound(null, pos, SoundEvents.BREWING_STAND_BREW, SoundSource.BLOCKS, 1.0F, 1.0F);

                        // Dropa 3 extratos
                        ItemStack extractStack = new ItemStack(ModItems.HOMUNCULUS_EXTRACT.get(), 3);
                        ItemEntity entity = new ItemEntity(level, pos.getX() + 0.5, pos.getY() + 1.0, pos.getZ() + 0.5, extractStack);
                        level.addFreshEntity(entity);
                    }
                    return ItemInteractionResult.sidedSuccess(level.isClientSide);
                }
                return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
            });
        });
    }

    @SubscribeEvent
    public static void onRegisterBrewingRecipes(RegisterBrewingRecipesEvent event) {
        var builder = event.getBuilder();
        
        // Raiz de Mandrágora na Visão Noturna
        builder.addMix(
                Potions.NIGHT_VISION,
                ModItems.MANDRAKE_ROOT.get(),
                com.cognitio.core.registry.ModPotions.CLAIRVOYANCE
        );

        // Extrato de Homúnculo na Visão Noturna
        builder.addMix(
                Potions.NIGHT_VISION,
                ModItems.HOMUNCULUS_EXTRACT.get(),
                com.cognitio.core.registry.ModPotions.CLAIRVOYANCE
        );

        // Redstone para prolongar a Clarividência
        builder.addMix(
                com.cognitio.core.registry.ModPotions.CLAIRVOYANCE,
                Items.REDSTONE,
                com.cognitio.core.registry.ModPotions.LONG_CLAIRVOYANCE
        );
    }
}
