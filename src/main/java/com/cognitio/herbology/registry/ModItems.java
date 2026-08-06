package com.cognitio.herbology.registry;

import com.cognitio.herbology.CognitioHerbology;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemNameBlockItem;
import com.cognitio.herbology.item.GlovesItem;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;

public class ModItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(CognitioHerbology.MODID);

    public static final DeferredItem<Item> MANDRAKE_ROOT = ITEMS.registerSimpleItem("mandrake_root", 
            new Item.Properties().food(new FoodProperties.Builder()
                    .nutrition(0).saturationModifier(0)
                    .effect(() -> new MobEffectInstance(MobEffects.POISON, 600, 3), 1.0f) // Veneno severo por 30s
                    .alwaysEdible()
                    .build()));
    
    public static final DeferredItem<Item> HOMUNCULUS_EXTRACT = ITEMS.registerItem("homunculus_extract", 
            properties -> new com.cognitio.herbology.item.HomunculusExtractItem(properties),
            new Item.Properties().food(new FoodProperties.Builder()
                    .nutrition(0).saturationModifier(0)
                    .effect(() -> new MobEffectInstance(MobEffects.CONFUSION, 300, 0), 1.0f) // Náusea 15 seg, 100% chance
                    .alwaysEdible()
                    .build()));

    public static final DeferredItem<Item> DISCERNED_MANDRAKE_ROOT = ITEMS.registerSimpleItem("discerned_mandrake_root", new Item.Properties());
    
    public static final DeferredItem<Item> MANDRAKE_SEEDS = ITEMS.register("mandrake_seeds",
            () -> new ItemNameBlockItem(com.cognitio.herbology.registry.ModBlocks.MANDRAKE_CROP.get(), new Item.Properties()));

    public static final DeferredItem<Item> WOODEN_SPOON = ITEMS.register("wooden_spoon",
            () -> new com.cognitio.herbology.item.WoodenSpoonItem(new Item.Properties().stacksTo(1)));

    public static final DeferredItem<Item> SAYLORS_EYE = ITEMS.register("saylors_eye", 
            () -> new net.minecraft.world.item.BlockItem(ModBlocks.SAYLORS_EYE.get(), new Item.Properties()));

    public static final DeferredItem<Item> DISCERNED_SAYLORS_EYE = ITEMS.registerSimpleItem("discerned_saylors_eye", new Item.Properties());


    public static final DeferredItem<Item> GLOVES = ITEMS.register("gloves", 
            () -> new GlovesItem(new Item.Properties().durability(256)));

    public static final DeferredItem<Item> EARMUFFS = ITEMS.register("earmuffs", 
            () -> new com.cognitio.herbology.item.EarmuffsItem(new Item.Properties().durability(55)));

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
