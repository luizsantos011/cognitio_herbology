package com.cognitio.herbology.registry;

import com.cognitio.herbology.CognitioHerbology;
import com.cognitio.herbology.block.entity.PressBlockEntity;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredHolder;

public class ModBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, CognitioHerbology.MODID);

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<PressBlockEntity>> PRESS_BE = 
        BLOCK_ENTITIES.register("press", () -> BlockEntityType.Builder.of(PressBlockEntity::new, ModBlocks.PRESS.get()).build(null));

    public static void register(IEventBus eventBus) {
        BLOCK_ENTITIES.register(eventBus);
    }
}
