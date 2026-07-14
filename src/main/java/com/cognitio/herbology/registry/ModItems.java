package com.cognitio.herbology.registry;

import com.cognitio.herbology.CognitioHerbology;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemNameBlockItem;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(CognitioHerbology.MODID);

    public static final DeferredItem<Item> MANDRAGORA_ROOT = ITEMS.registerSimpleItem("mandragora_root", new Item.Properties());
    
    public static final DeferredItem<Item> DISCERNED_MANDRAGORA_ROOT = ITEMS.registerSimpleItem("discerned_mandragora_root", new Item.Properties());
    
    public static final DeferredItem<Item> MANDRAGORA_SEEDS = ITEMS.register("mandragora_seeds", 
            () -> new ItemNameBlockItem(ModBlocks.MANDRAGORA_CROP.get(), new Item.Properties()));

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
