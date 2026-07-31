package com.cognitio.herbology.event;

import com.cognitio.herbology.CognitioHerbology;
import com.cognitio.herbology.registry.ModItems;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potions;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.brewing.RegisterBrewingRecipesEvent;

@EventBusSubscriber(modid = CognitioHerbology.MODID, bus = EventBusSubscriber.Bus.GAME)
public class AlchemyGameEvents {

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
