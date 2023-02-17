package com.bedmen.odyssey.block.entity;

import com.bedmen.odyssey.block.wood.CovenHutDoorBlock;
import com.bedmen.odyssey.potions.FireEffect;
import com.bedmen.odyssey.registry.BlockEntityTypeRegistry;
import com.bedmen.odyssey.registry.EffectRegistry;
import com.bedmen.odyssey.registry.ItemRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.event.world.NoteBlockEvent;

public class CovenHutDoorBlockEntity extends BlockEntity {
    public CovenHutDoorBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(BlockEntityTypeRegistry.COVEN_HUT_DOOR.get(), blockPos, blockState);
    }

    public static void clientTick(Level level, BlockPos blockPos, BlockState blockState, CovenHutDoorBlockEntity covenHutDoorBlockEntity) {
    }

    public static void serverTick(Level level, BlockPos blockPos, BlockState blockState, CovenHutDoorBlockEntity covenHutDoorBlockEntity) {
        AABB boundingBox = new AABB(blockPos).inflate(20.0d, 15.0d, 20.0d);
        level.getEntitiesOfClass(LivingEntity.class, boundingBox)
                .stream()
                .filter(entity -> !(entity instanceof Player player)
                        || (!player.isCreative()
                        && !player.isSpectator()
                        && !player.getItemInHand(InteractionHand.MAIN_HAND).is(ItemRegistry.COVEN_HUT_KEY.get())
                        && !player.getItemInHand(InteractionHand.OFF_HAND).is(ItemRegistry.COVEN_HUT_KEY.get())))
                .forEach(livingEntity -> {
                    livingEntity.addEffect(FireEffect.getFireEffectInstance(EffectRegistry.HEXFLAME.get(), 80, 0));
                    teleportRandomlyOutsideBoundingBox(livingEntity, boundingBox);
                });
//        level.getEntitiesOfClass(ServerPlayer.class, boundingBox)
//                .stream()
//                .filter(serverPlayer ->
//                        !serverPlayer.isCreative()
//                                && !serverPlayer.isSpectator()
//                                && !serverPlayer.getItemInHand(InteractionHand.MAIN_HAND).is(ItemRegistry.COVEN_HUT_KEY.get())
//                                && !serverPlayer.getItemInHand(InteractionHand.OFF_HAND).is(ItemRegistry.COVEN_HUT_KEY.get()))
//                .forEach(serverPlayer -> {
//                    serverPlayer.addEffect(FireEffect.getFireEffectInstance(EffectRegistry.HEXFLAME.get(), 80, 0));
//                    teleportRandomlyOutsideBoundingBox(serverPlayer, boundingBox);
//                });
    }

    protected static void teleportRandomlyOutsideBoundingBox(LivingEntity livingEntity, AABB boundingBox) {
        if (!livingEntity.level.isClientSide() && livingEntity.isAlive()) {
            double x = livingEntity.getX();
            double y = livingEntity.getY();
            double z = livingEntity.getZ();
            int attempts = 0;
            AABB wideBoundingBox = boundingBox.inflate(livingEntity.getBbWidth(), livingEntity.getBbHeight(), livingEntity.getBbWidth());
            while(wideBoundingBox.contains(x, y, z) && attempts < 20){
                x = livingEntity.getX() + (livingEntity.level.random.nextDouble() - 0.5D) * 64.0D;
                y = livingEntity.getY() + (double)(livingEntity.level.random.nextInt(64) - 32);
                z = livingEntity.getZ() + (livingEntity.level.random.nextDouble() - 0.5D) * 64.0D;
                attempts++;
            }
            teleport(livingEntity, x, y, z);
        }
    }

    private static void teleport(LivingEntity livingEntity, double x, double y, double z) {
        BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos(x, y, z);

        while(blockpos$mutableblockpos.getY() > livingEntity.level.getMinBuildHeight() && !livingEntity.level.getBlockState(blockpos$mutableblockpos).getMaterial().blocksMotion()) {
            blockpos$mutableblockpos.move(Direction.DOWN);
        }

        BlockState blockstate = livingEntity.level.getBlockState(blockpos$mutableblockpos);
        boolean blocksMotion = blockstate.getMaterial().blocksMotion();
        if (blocksMotion) {
            boolean flag2 = livingEntity.randomTeleport(x, y, z, true);
            if (flag2 && !livingEntity.isSilent()) {
                livingEntity.level.playSound(null, livingEntity.xo, livingEntity.yo, livingEntity.zo, SoundEvents.ENDERMAN_TELEPORT, livingEntity.getSoundSource(), 1.0F, 1.0F);
                livingEntity.playSound(SoundEvents.ENDERMAN_TELEPORT, 1.0F, 1.0F);
            }
        }
    }
}
