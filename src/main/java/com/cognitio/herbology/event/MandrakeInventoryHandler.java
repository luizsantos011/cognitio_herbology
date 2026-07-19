package com.cognitio.herbology.event;

import com.cognitio.core.perception.FrenzyEngine;
import com.cognitio.herbology.CognitioHerbology;
import com.cognitio.herbology.registry.ModItems;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

@EventBusSubscriber(modid = CognitioHerbology.MODID, bus = EventBusSubscriber.Bus.GAME)
public class MandrakeInventoryHandler {

    @SubscribeEvent
    public static void onPlayerTick(PlayerTickEvent.Post event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            // Roda a verificação apenas a cada 20 ticks (1 segundo) para não sobrecarregar o servidor
            if (player.tickCount % 20 == 0) {
                boolean hasDiscernedRoot = false;
                
                // Varre o inventário do jogador
                for (ItemStack stack : player.getInventory().items) {
                    if (stack.is(ModItems.DISCERNED_MANDRAKE_ROOT.get())) {
                        hasDiscernedRoot = true;
                        break; // Taxa fixa: basta ter UMA para sofrer
                    }
                }
                
                if (hasDiscernedRoot) {
                    // A raiz compreendida pulsa conhecimento proibido: 2.0f de Frenesi por segundo
                    FrenzyEngine.addFrenzy(player, 2.0f);
                }
            }
        }
    }
}
