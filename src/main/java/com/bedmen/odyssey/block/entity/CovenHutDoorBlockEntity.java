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
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

public class CovenHutDoorBlockEntity extends BlockEntity {
    public CovenHutDoorBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(BlockEntityTypeRegistry.COVEN_HUT_DOOR.get(), blockPos, blockState);
    }

    public static void clientTick(Level level, BlockPos blockPos, BlockState blockState, CovenHutDoorBlockEntity covenHutDoorBlockEntity) {
//        if(blockState.getValue(CovenHutDoorBlock.LOCKED)){
//        }
    }

    public static void serverTick(Level level, BlockPos blockPos, BlockState blockState, CovenHutDoorBlockEntity covenHutDoorBlockEntity) {
        if(blockState.getValue(CovenHutDoorBlock.LOCKED)){
            AABB boundingBox = new AABB(blockPos).inflate(20.0d, 15.0d, 20.0d);
            level.getEntitiesOfClass(ServerPlayer.class, boundingBox)
                    .stream()
                    .filter(serverPlayer ->
                            !serverPlayer.isCreative()
                            && !serverPlayer.isSpectator()
                            && !serverPlayer.getItemInHand(InteractionHand.MAIN_HAND).is(ItemRegistry.COVEN_HUT_KEY.get())
                            && !serverPlayer.getItemInHand(InteractionHand.OFF_HAND).is(ItemRegistry.COVEN_HUT_KEY.get()))
                    .forEach(serverPlayer -> {
                        serverPlayer.addEffect(FireEffect.getFireEffectInstance(EffectRegistry.HEXFLAME.get(), 80, 0));
                        teleportRandomlyOutsideBoundingBox(serverPlayer, boundingBox);
                    });
        }
    }

    protected static boolean teleportRandomlyOutsideBoundingBox(ServerPlayer serverPlayer, AABB boundingBox) {
        if (!serverPlayer.level.isClientSide() && serverPlayer.isAlive()) {
            double x = serverPlayer.getX();
            double y = serverPlayer.getY();
            double z = serverPlayer.getZ();
            int attempts = 0;
            AABB wideBoundingBox = boundingBox.inflate(serverPlayer.getBbWidth(), serverPlayer.getBbHeight(), serverPlayer.getBbWidth());
            while(wideBoundingBox.contains(x, y, z) && attempts < 20){
                x = serverPlayer.getX() + (serverPlayer.getRandom().nextDouble() - 0.5D) * 64.0D;
                y = serverPlayer.getY() + (double)(serverPlayer.getRandom().nextInt(64) - 32);
                z = serverPlayer.getZ() + (serverPlayer.getRandom().nextDouble() - 0.5D) * 64.0D;
                attempts++;
            }
            return teleport(serverPlayer, x, y, z);
        } else {
            return false;
        }
    }

    private static boolean teleport(ServerPlayer serverPlayer, double x, double y, double z) {
        BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos(x, y, z);

        while(blockpos$mutableblockpos.getY() > serverPlayer.level.getMinBuildHeight() && !serverPlayer.level.getBlockState(blockpos$mutableblockpos).getMaterial().blocksMotion()) {
            blockpos$mutableblockpos.move(Direction.DOWN);
        }

        BlockState blockstate = serverPlayer.level.getBlockState(blockpos$mutableblockpos);
        boolean blocksMotion = blockstate.getMaterial().blocksMotion();
        if (blocksMotion) {
            boolean flag2 = serverPlayer.randomTeleport(x, y, z, true);
            if (flag2 && !serverPlayer.isSilent()) {
                serverPlayer.level.playSound(null, serverPlayer.xo, serverPlayer.yo, serverPlayer.zo, SoundEvents.ENDERMAN_TELEPORT, serverPlayer.getSoundSource(), 1.0F, 1.0F);
                serverPlayer.playSound(SoundEvents.ENDERMAN_TELEPORT, 1.0F, 1.0F);
            }

            return flag2;
        } else {
            return false;
        }
    }
}
