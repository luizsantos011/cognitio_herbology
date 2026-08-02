package com.cognitio.herbology.event;

import com.cognitio.core.perception.FrenzyEngine;
import com.cognitio.core.perception.PerceptionEngine;
import com.cognitio.herbology.CognitioHerbology;
import com.cognitio.herbology.registry.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.level.BlockEvent;

@EventBusSubscriber(modid = CognitioHerbology.MODID, bus = EventBusSubscriber.Bus.GAME)
public class SaylorsEyeEvents {

    @SubscribeEvent
    public static void onBlockBreak(BlockEvent.BreakEvent event) {
        if (!(event.getLevel() instanceof ServerLevel serverLevel)) return;
        
        if (event.getState().is(ModBlocks.SAYLORS_EYE.get()) || event.getState().is(ModBlocks.DISCERNED_SAYLORS_EYE.get())) {
            Player player = event.getPlayer();
            ItemStack tool = player.getMainHandItem();
            
            boolean hasSilkTouch = false;
            ItemEnchantments enchs = tool.get(net.minecraft.core.component.DataComponents.ENCHANTMENTS);
            if (enchs != null) {
                for (var holder : enchs.keySet()) {
                    if (holder.unwrapKey().isPresent() && holder.unwrapKey().get().location().getPath().equals("silk_touch")) {
                        hasSilkTouch = true;
                        break;
                    }
                }
            }

            if (!hasSilkTouch) {
                BlockPos pos = event.getPos();

                // 1. Solta tinta preta (partículas de lula)
                serverLevel.sendParticles(ParticleTypes.SQUID_INK, 
                    pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, 
                    20, 0.3, 0.3, 0.3, 0.05);

                // 2. Verifica percepção (Insight)
                int insight = PerceptionEngine.getEffectivePerception(player);
                if (insight < 100) {
                    // Ignorante: Cegueira curta (5 segundos = 100 ticks)
                    player.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 100, 0, false, true, true));
                } else {
                    // Sapiente: Aumenta a barra de Frenesi em 15 pontos (max 100)
                    FrenzyEngine.addFrenzy(player, 15.0f);
                }
            } else {
                // Tem Silk Touch, spawna o item!
                BlockPos pos = event.getPos();
                int pickles = event.getState().getValue(net.minecraft.world.level.block.SeaPickleBlock.PICKLES);
                ItemStack drop = new ItemStack(com.cognitio.herbology.registry.ModItems.SAYLORS_EYE.get(), pickles);
                net.minecraft.world.entity.item.ItemEntity itemEntity = new net.minecraft.world.entity.item.ItemEntity(
                        serverLevel, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, drop);
                itemEntity.setDefaultPickUpDelay();
                serverLevel.addFreshEntity(itemEntity);
            }
        }
    }
}
