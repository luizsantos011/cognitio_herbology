import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.block.Block;
import java.lang.reflect.Method;
import java.util.Arrays;

public class Test {
    public static void main(String[] args) {
        try {
            Class<?> clazz = Class.forName("net.minecraft.data.loot.BlockLootSubProvider");
            Method m = clazz.getDeclaredMethod("hasSilkTouch");
            m.setAccessible(true);
            Object builder = m.invoke(null);
            System.out.println("Method exists! Return type: " + builder.getClass().getName());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
