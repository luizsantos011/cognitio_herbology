package com.cognitio.herbology.client;

import com.cognitio.herbology.CognitioHerbology;
import com.cognitio.herbology.client.renderer.PressBlockEntityRenderer;
import com.cognitio.herbology.registry.ModBlockEntities;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.ModelEvent;

@EventBusSubscriber(modid = CognitioHerbology.MODID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientEvents {

    public static final ModelResourceLocation PRESS_SCREW_MODEL = new ModelResourceLocation(
        net.minecraft.resources.ResourceLocation.fromNamespaceAndPath(CognitioHerbology.MODID, "block/press_screw"), "standalone"
    );

    @SubscribeEvent
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(ModBlockEntities.PRESS_BE.get(), PressBlockEntityRenderer::new);
    }

    @SubscribeEvent
    public static void registerAdditionalModels(ModelEvent.RegisterAdditional event) {
        event.register(PRESS_SCREW_MODEL);
    }
}
