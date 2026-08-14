package com.cognitio.herbology.registry;

import net.minecraft.world.item.Item;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class ExtractColorRegistry {
    public static class ExtractData {
        public final Supplier<Item> inputPlant;
        public final Supplier<Item> outputExtract;
        public final int hexColor;
        public final float[] rgb;

        public ExtractData(Supplier<Item> inputPlant, Supplier<Item> outputExtract, int hexColor) {
            this.inputPlant = inputPlant;
            this.outputExtract = outputExtract;
            this.hexColor = hexColor;
            
            // Extract RGB floats from hex for the renderer (0.0f - 1.0f)
            float r = ((hexColor >> 16) & 0xFF) / 255.0f;
            float g = ((hexColor >> 8) & 0xFF) / 255.0f;
            float b = (hexColor & 0xFF) / 255.0f;
            this.rgb = new float[]{r, g, b};
        }
    }

    private static final Map<Item, ExtractData> BY_INPUT = new HashMap<>();

    public static ExtractData getData(Item inputItem) {
        if (BY_INPUT.isEmpty()) {
            // Lazy initialization to ensure Items are registered
            BY_INPUT.put(ModItems.BELLADONNA.get(), new ExtractData(ModItems.BELLADONNA, ModItems.PHANTOM_LYMPH, 0x8033B3));
            BY_INPUT.put(ModItems.BLACK_HELLEBORE.get(), new ExtractData(ModItems.BLACK_HELLEBORE, ModItems.HERMETIC_NECTAR, 0x30252F));
            BY_INPUT.put(ModItems.MANDRAKE_ROOT.get(), new ExtractData(ModItems.MANDRAKE_ROOT, ModItems.HOMUNCULUS_EXTRACT, 0xF0F0AA));
        }
        return BY_INPUT.get(inputItem);
    }
}
