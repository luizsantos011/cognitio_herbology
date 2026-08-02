package com.cognitio.herbology.network;

import com.cognitio.herbology.registry.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LayeredCauldronBlock;
import net.minecraft.world.phys.AABB;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.util.List;

public class ServerPayloadHandler {
    public static void handleCauldronProcess(CauldronProcessPayload payload, IPayloadContext context) {
        context.enqueueWork(() -> {
            Player player = context.player();
            Level level = player.level();
            BlockPos pos = payload.pos();

            // Secutiry Checks
            if (player.distanceToSqr(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5) > 64.0) return;
            if (!player.getMainHandItem().is(Items.WOODEN_SHOVEL)) return;
            if (!level.getBlockState(pos).is(Blocks.WATER_CAULDRON)) return;
            if (level.getBlockState(pos).getValue(LayeredCauldronBlock.LEVEL) != 3) return;

            // Busca os itens dentro da área do caldeirão (EntityItems flutuando na agua)
            AABB cauldronBounds = new AABB(pos);
            List<ItemEntity> entities = level.getEntitiesOfClass(ItemEntity.class, cauldronBounds);

            boolean foundMandrake = false;
            for (ItemEntity entity : entities) {
                if (entity.getItem().is(ModItems.MANDRAKE_ROOT.get())) {
                    foundMandrake = true;
                    // Consome 1 mandrágora
                    ItemStack stack = entity.getItem();
                    stack.shrink(1);
                    if (stack.isEmpty()) {
                        entity.discard();
                    } else {
                        entity.setItem(stack);
                    }
                    break;
                }
            }

            if (foundMandrake) {
                // Independentemente do resultado, a água e a raiz são consumidas
                level.setBlockAndUpdate(pos, Blocks.CAULDRON.defaultBlockState());

                if (level.getBlockState(pos.below()).is(Blocks.SOUL_FIRE)) {
                    // Sucesso: Transforma o líquido no extrato de homúnculo (Nível 3)
                    level.playSound(null, pos, SoundEvents.BREWING_STAND_BREW, SoundSource.BLOCKS, 1.0F, 1.0F);
                    level.setBlockAndUpdate(pos, com.cognitio.herbology.registry.ModBlocks.HOMUNCULUS_CAULDRON.get().defaultBlockState().setValue(LayeredCauldronBlock.LEVEL, 3));
                } else {
                    // Falha: Faltou o calor espiritual correto, a mistura vira Poção Mundana
                    level.playSound(null, pos, SoundEvents.FIRE_EXTINGUISH, SoundSource.BLOCKS, 1.0F, 1.0F);
                    for (int i = 0; i < 5; i++) {
                        level.addParticle(net.minecraft.core.particles.ParticleTypes.LARGE_SMOKE, 
                            pos.getX() + 0.5, pos.getY() + 1.0, pos.getZ() + 0.5, 
                            0.0, 0.05, 0.0);
                    }
                    level.setBlockAndUpdate(pos, com.cognitio.herbology.registry.ModBlocks.MUNDANE_CAULDRON.get().defaultBlockState().setValue(LayeredCauldronBlock.LEVEL, 3));
                }
            }
        });
    }
}
