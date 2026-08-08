package com.cognitio.herbology.event;

import com.cognitio.herbology.CognitioHerbology;
import com.cognitio.herbology.registry.ModItems;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.trading.ItemCost;
import net.minecraft.world.item.trading.MerchantOffer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.village.VillagerTradesEvent;
import net.neoforged.neoforge.event.village.WandererTradesEvent;

import java.util.List;

@EventBusSubscriber(modid = CognitioHerbology.MODID, bus = EventBusSubscriber.Bus.GAME)
public class CommerceEvents {

    @SubscribeEvent
    public static void onVillagerTrades(VillagerTradesEvent event) {
        if (event.getType() == VillagerProfession.CLERIC) {
            List<VillagerTrades.ItemListing> tradesLevel2 = event.getTrades().get(2);
            if (tradesLevel2 != null) {
                tradesLevel2.add((trader, random) -> new MerchantOffer(
                        new ItemCost(Items.EMERALD, 4),
                        new ItemStack(ModItems.MANDRAKE_SEEDS.get(), 1),
                        12,
                        5,
                        0.05f
                ));
            }
        }
    }

    @SubscribeEvent
    public static void onWandererTrades(WandererTradesEvent event) {
        List<VillagerTrades.ItemListing> genericTrades = event.getGenericTrades();
        if (genericTrades != null) {
            genericTrades.add((trader, random) -> new MerchantOffer(
                    new ItemCost(Items.EMERALD, 5),
                    new ItemStack(ModItems.MANDRAKE_SEEDS.get(), 1),
                    8,
                    5,
                    0.05f
                ));
        }
    }
}
