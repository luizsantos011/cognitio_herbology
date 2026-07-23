package com.cognitio.herbology.registry;

import com.cognitio.herbology.CognitioHerbology;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemNameBlockItem;
import com.cognitio.herbology.item.GlovesItem;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(CognitioHerbology.MODID);

    public static final DeferredItem<Item> MANDRAKE_ROOT = ITEMS.registerSimpleItem("mandrake_root", new Item.Properties());
    
    public static final DeferredItem<Item> DISCERNED_MANDRAKE_ROOT = ITEMS.registerSimpleItem("discerned_mandrake_root", new Item.Properties());
    
    public static final DeferredItem<Item> MANDRAKE_SEEDS = ITEMS.register("mandrake_seeds", 
            () -> new ItemNameBlockItem(ModBlocks.MANDRAKE_CROP.get(), new Item.Properties()));

    public static final DeferredItem<Item> GLOVES = ITEMS.register("gloves", 
            () -> new GlovesItem(new Item.Properties().durability(256)));

    public static final DeferredItem<Item> EARMUFFS = ITEMS.register("earmuffs", 
            () -> new net.minecraft.world.item.ArmorItem(
                    net.minecraft.world.item.ArmorMaterials.LEATHER, 
                    net.minecraft.world.item.ArmorItem.Type.HELMET, 
                    new Item.Properties().durability(55)
            ));

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
