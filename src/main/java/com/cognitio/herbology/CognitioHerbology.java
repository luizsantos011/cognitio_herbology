package com.cognitio.herbology;

import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraft.client.Minecraft;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

// The value here should match an entry in the META-INF/neoforge.mods.toml file
@Mod(CognitioHerbology.MODID)
public class CognitioHerbology {
    // Define mod id in a common place for everything to reference
    public static final String MODID = "cognitio_herbology";
    // Directly reference a slf4j logger
    public static final Logger LOGGER = LogUtils.getLogger();
    // Registra a aba criativa do mod
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MODID);

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> HERBOLOGY_TAB = CREATIVE_MODE_TABS.register("herbology_tab", () -> CreativeModeTab.builder()
            .title(Component.translatable("itemGroup.cognitio_herbology")) 
            .withTabsBefore(CreativeModeTabs.COMBAT)
            .icon(() -> com.cognitio.herbology.registry.ModItems.MANDRAKE_ROOT.get().getDefaultInstance())
            .displayItems((parameters, output) -> {
                output.accept(com.cognitio.herbology.registry.ModItems.MANDRAKE_ROOT.get()); 
                output.accept(com.cognitio.herbology.registry.ModItems.TOASTED_MANDRAKE_ROOT.get());
                output.accept(com.cognitio.herbology.registry.ModItems.MANDRAKE_SEEDS.get());
                output.accept(com.cognitio.herbology.registry.ModItems.DITTANY_LEAF.get());
                output.accept(com.cognitio.herbology.registry.ModItems.DITTANY_SAPLING.get());
                output.accept(com.cognitio.herbology.registry.ModItems.BELLADONNA.get());
                output.accept(com.cognitio.herbology.registry.ModItems.BLACK_HELLEBORE.get());
                output.accept(com.cognitio.herbology.registry.ModItems.PRESS.get());
                output.accept(com.cognitio.herbology.registry.ModItems.EMPTY_FLASK.get());
                output.accept(com.cognitio.herbology.registry.ModItems.MORTAR_AND_PESTLE.get());
                output.accept(com.cognitio.herbology.registry.ModItems.MANDRAKE_POWDER.get());
                output.accept(com.cognitio.herbology.registry.ModItems.PHANTOM_LYMPH.get());
                output.accept(com.cognitio.herbology.registry.ModItems.HERMETIC_NECTAR.get());
                output.accept(com.cognitio.herbology.registry.ModItems.GLOVES.get());
                output.accept(com.cognitio.herbology.registry.ModItems.EARMUFFS.get());
                output.accept(com.cognitio.herbology.registry.ModItems.APOTHECARY_GRIMOIRE.get());
                output.accept(com.cognitio.herbology.registry.ModItems.HOMUNCULUS_EXTRACT.get()); 
                output.accept(com.cognitio.herbology.registry.ModItems.SAYLORS_EYE.get()); 
                output.accept(com.cognitio.herbology.registry.ModItems.WOODEN_SPOON.get()); 
            }).build());

    // The constructor for the mod class is the first code that is run when your mod is loaded.
    // FML will recognize some parameter types like IEventBus or ModContainer and pass them in automatically.
    public CognitioHerbology(IEventBus modEventBus, ModContainer modContainer) {
        // Registros
        com.cognitio.herbology.registry.ModBlocks.register(modEventBus);
        com.cognitio.herbology.registry.ModBlockEntities.register(modEventBus);
        com.cognitio.herbology.registry.ModItems.register(modEventBus);
        CREATIVE_MODE_TABS.register(modEventBus);
        
        // Register the commonSetup method for modloading
        modEventBus.addListener(this::commonSetup);

        // Register ourselves for server and other game events we are interested in.
        // Note that this is necessary if and only if we want *this* class (CognitioHerbology) to respond directly to events.
        // Do not add this line if there are no @SubscribeEvent-annotated functions in this class, like onServerStarting() below.
        NeoForge.EVENT_BUS.register(this);

        // (Removido addCreative)

        // Register our mod's ModConfigSpec so that FML can create and load the config file for us
        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    private void commonSetup(FMLCommonSetupEvent event) {
        // Some common setup code
        LOGGER.info("HELLO FROM COMMON SETUP");

        if (Config.LOG_DIRT_BLOCK.getAsBoolean()) {
            LOGGER.info("DIRT BLOCK >> {}", BuiltInRegistries.BLOCK.getKey(Blocks.DIRT));
        }

        LOGGER.info("{}{}", Config.MAGIC_NUMBER_INTRODUCTION.get(), Config.MAGIC_NUMBER.getAsInt());

        Config.ITEM_STRINGS.get().forEach((item) -> LOGGER.info("ITEM >> {}", item));

        // Registrar transmutação da mandrágora
        event.enqueueWork(() -> {
            com.cognitio.api.perception.TransmutationAPI.register(
                com.cognitio.herbology.registry.ModItems.MANDRAKE_ROOT.get(),
                com.cognitio.herbology.registry.ModItems.DISCERNED_MANDRAKE_ROOT.get(),
                com.cognitio.api.perception.EnlightenmentTier.TIER_2
            );
        });
    }

    // (Removido addCreative method)

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        // Do something when the server starts
        LOGGER.info("HELLO from server starting");
    }

    @EventBusSubscriber(modid = CognitioHerbology.MODID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    static class ClientModEvents {
        @SubscribeEvent
        static void onClientSetup(FMLClientSetupEvent event) {
            // Some client setup code
            LOGGER.info("HELLO FROM CLIENT SETUP");
            LOGGER.info("MINECRAFT NAME >> {}", Minecraft.getInstance().getUser().getName());
        }

        @SubscribeEvent
        static void registerBlockColors(net.neoforged.neoforge.client.event.RegisterColorHandlersEvent.Block event) {
            event.register((state, level, pos, tintIndex) -> {
                return com.cognitio.herbology.registry.ExtractColorRegistry.getData(com.cognitio.herbology.registry.ModItems.MANDRAKE_ROOT.get()).hexColor; // Verde Escuro (Homúnculo)
            }, com.cognitio.herbology.registry.ModBlocks.HOMUNCULUS_CAULDRON.get());

            event.register((state, level, pos, tintIndex) -> {
                return com.cognitio.herbology.registry.ExtractColorRegistry.getData(com.cognitio.herbology.registry.ModItems.BLACK_HELLEBORE.get()).hexColor; // Néctar Hermético
            }, com.cognitio.herbology.registry.ModBlocks.HERMETIC_CAULDRON.get());

            event.register((state, level, pos, tintIndex) -> {
                return 0x808080; // Cinza (Mundana)
            }, com.cognitio.herbology.registry.ModBlocks.MUNDANE_CAULDRON.get());
        }

        @SubscribeEvent
        static void onModifyBakingResult(net.neoforged.neoforge.client.event.ModelEvent.ModifyBakingResult event) {
            java.util.Map<net.minecraft.client.resources.model.ModelResourceLocation, net.minecraft.client.resources.model.BakedModel> models = event.getModels();
            
            // Variantes do bloco normal Saylor's Eye
            String[] pickles = {"1", "2", "3", "4"};
            String[] waterlogged = {"false", "true"};
            
            for (String p : pickles) {
                for (String w : waterlogged) {
                    String variantStr = "pickles=" + p + ",waterlogged=" + w;
                    
                    net.minecraft.client.resources.model.ModelResourceLocation normalLoc = 
                        new net.minecraft.client.resources.model.ModelResourceLocation(net.minecraft.resources.ResourceLocation.fromNamespaceAndPath(MODID, "saylors_eye"), variantStr);
                        
                    net.minecraft.client.resources.model.ModelResourceLocation discernedLoc = 
                        new net.minecraft.client.resources.model.ModelResourceLocation(net.minecraft.resources.ResourceLocation.fromNamespaceAndPath(MODID, "discerned_saylors_eye"), variantStr);
                        
                    if (models.containsKey(normalLoc) && models.containsKey(discernedLoc)) {
                        net.minecraft.client.resources.model.BakedModel normalModel = models.get(normalLoc);
                        net.minecraft.client.resources.model.BakedModel discernedModel = models.get(discernedLoc);
                        
                        // Disfarce Duplo: Ambos os blocos vão obedecer à alucinação de Insight!
                        com.cognitio.herbology.client.model.InsightBakedModel proxy = 
                            new com.cognitio.herbology.client.model.InsightBakedModel(normalModel, discernedModel, 100);
                            
                        models.put(normalLoc, proxy);
                        models.put(discernedLoc, proxy);
                    }
                }
            }

            // Itens de Mandrágora no Inventário
            net.minecraft.client.resources.model.ModelResourceLocation mandrakeLoc = 
                new net.minecraft.client.resources.model.ModelResourceLocation(net.minecraft.resources.ResourceLocation.fromNamespaceAndPath(MODID, "mandrake_root"), "inventory");
            net.minecraft.client.resources.model.ModelResourceLocation discernedMandrakeLoc = 
                new net.minecraft.client.resources.model.ModelResourceLocation(net.minecraft.resources.ResourceLocation.fromNamespaceAndPath(MODID, "discerned_mandrake_root"), "inventory");
            
            if (models.containsKey(mandrakeLoc) && models.containsKey(discernedMandrakeLoc)) {
                net.minecraft.client.resources.model.BakedModel mandrakeModel = models.get(mandrakeLoc);
                net.minecraft.client.resources.model.BakedModel discernedMandrakeModel = models.get(discernedMandrakeLoc);
                
                com.cognitio.herbology.client.model.InsightBakedModel mandrakeProxy = 
                    new com.cognitio.herbology.client.model.InsightBakedModel(mandrakeModel, discernedMandrakeModel, 100);
                    
                models.put(mandrakeLoc, mandrakeProxy);
                models.put(discernedMandrakeLoc, mandrakeProxy);
            }

            // Item de Saylor's Eye no Inventário
            net.minecraft.client.resources.model.ModelResourceLocation saylorsEyeLoc = 
                new net.minecraft.client.resources.model.ModelResourceLocation(net.minecraft.resources.ResourceLocation.fromNamespaceAndPath(MODID, "saylors_eye"), "inventory");
            net.minecraft.client.resources.model.ModelResourceLocation discernedSaylorsEyeLoc = 
                new net.minecraft.client.resources.model.ModelResourceLocation(net.minecraft.resources.ResourceLocation.fromNamespaceAndPath(MODID, "discerned_saylors_eye"), "inventory");
            
            if (models.containsKey(saylorsEyeLoc) && models.containsKey(discernedSaylorsEyeLoc)) {
                net.minecraft.client.resources.model.BakedModel saylorsEyeModel = models.get(saylorsEyeLoc);
                net.minecraft.client.resources.model.BakedModel discernedSaylorsEyeModel = models.get(discernedSaylorsEyeLoc);
                
                com.cognitio.herbology.client.model.InsightBakedModel saylorsEyeProxy = 
                    new com.cognitio.herbology.client.model.InsightBakedModel(saylorsEyeModel, discernedSaylorsEyeModel, 100);
                    
                models.put(saylorsEyeLoc, saylorsEyeProxy);
                // Não precisamos colocar de volta no discernedSaylorsEyeLoc porque ele nem existe como item no jogo, só usamos o BakedModel dele como alvo.
            }
            
            // Bloco de Belladonna no mundo
            net.minecraft.client.resources.model.ModelResourceLocation belladonnaLoc = 
                new net.minecraft.client.resources.model.ModelResourceLocation(net.minecraft.resources.ResourceLocation.fromNamespaceAndPath(MODID, "belladonna"), "");
            net.minecraft.client.resources.model.ModelResourceLocation discernedBelladonnaLoc = 
                new net.minecraft.client.resources.model.ModelResourceLocation(net.minecraft.resources.ResourceLocation.fromNamespaceAndPath(MODID, "discerned_belladonna"), "");
            
            if (models.containsKey(belladonnaLoc) && models.containsKey(discernedBelladonnaLoc)) {
                net.minecraft.client.resources.model.BakedModel belladonnaModel = models.get(belladonnaLoc);
                net.minecraft.client.resources.model.BakedModel discernedBelladonnaModel = models.get(discernedBelladonnaLoc);
                
                com.cognitio.herbology.client.model.InsightBakedModel belladonnaProxy = 
                    new com.cognitio.herbology.client.model.InsightBakedModel(belladonnaModel, discernedBelladonnaModel, 100);
                    
                models.put(belladonnaLoc, belladonnaProxy);
                models.put(discernedBelladonnaLoc, belladonnaProxy);
            }
        }
    }

    @EventBusSubscriber(modid = CognitioHerbology.MODID, bus = EventBusSubscriber.Bus.GAME, value = Dist.CLIENT)
    public static class ClientForgeEvents {
        private static int lastEffectiveInsight = -1;

        @SubscribeEvent
        public static void onClientTick(net.neoforged.neoforge.client.event.ClientTickEvent.Post event) {
            Minecraft mc = Minecraft.getInstance();
            if (mc.player != null && mc.level != null) {
                int currentEffective = com.cognitio.core.perception.PerceptionEngine.getEffectivePerception(mc.player);
                if (lastEffectiveInsight != -1) {
                    if ((lastEffectiveInsight < 100 && currentEffective >= 100) || (lastEffectiveInsight >= 100 && currentEffective < 100)) {
                        LOGGER.info("INSIGHT THRESHOLD CROSSED: {} -> {}. Forcing allChanged()!", lastEffectiveInsight, currentEffective);
                        mc.levelRenderer.allChanged();
                    }
                }
                lastEffectiveInsight = currentEffective;
            } else {
                lastEffectiveInsight = -1;
            }
        }
    }
}
