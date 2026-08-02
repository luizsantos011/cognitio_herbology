package com.cognitio.herbology.network;

import com.cognitio.herbology.CognitioHerbology;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

@EventBusSubscriber(modid = CognitioHerbology.MODID, bus = EventBusSubscriber.Bus.MOD)
public class NetworkRegistry {
    @SubscribeEvent
    public static void register(RegisterPayloadHandlersEvent event) {
        PayloadRegistrar registrar = event.registrar(CognitioHerbology.MODID);
        registrar.playToServer(
            CauldronProcessPayload.TYPE,
            CauldronProcessPayload.STREAM_CODEC,
            ServerPayloadHandler::handleCauldronProcess
        );
    }
}
