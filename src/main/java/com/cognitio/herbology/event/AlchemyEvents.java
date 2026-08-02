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
            // A antiga interação instantânea (CauldronInteraction) foi removida.
            // Agora o processo é orgânico: jogar a Mandrágora na água e mexer com a pá de madeira.
        });
    }

}
