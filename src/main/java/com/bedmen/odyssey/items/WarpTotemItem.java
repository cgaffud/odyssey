package com.bedmen.odyssey.items;

import com.bedmen.odyssey.magic.ExperienceCost;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.Optional;

import net.minecraft.world.item.Item.Properties;

public class WarpTotemItem extends MagicItem {

    private final boolean isCracked;

    public WarpTotemItem(Properties properties, boolean isCracked) {
        super(properties, new ExperienceCost(5.0f));
        this.isCracked = isCracked;
    }

    public boolean canBeUsed(ServerPlayer serverPlayer, ItemStack itemStack){
        return super.canBeUsed(serverPlayer, itemStack) && serverPlayer.getRespawnDimension().equals(serverPlayer.level.dimension());
    }

    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand interactionHand) {
        ItemStack itemStack = player.getItemInHand(interactionHand);
        if(this.markedAsCanBeUsed(itemStack)){
            level.playSound(null, player.blockPosition(), SoundEvents.BEACON_AMBIENT, SoundSource.PLAYERS, 1.0f, 1.0f);
            player.startUsingItem(interactionHand);
            return InteractionResultHolder.consume(itemStack);
        }
        return InteractionResultHolder.pass(itemStack);
    }

    public int getUseDuration(ItemStack itemStack) {
        return 60;
    }

    public ItemStack finishUsingItem(ItemStack itemStack, Level level, LivingEntity livingEntity) {
        if(this.markedAsCanBeUsed(itemStack) && livingEntity instanceof ServerPlayer serverPlayer){

            // ServerPlayer spawn info
            BlockPos spawn = serverPlayer.getRespawnPosition();
            float respawnAngle = serverPlayer.getRespawnAngle();
            boolean respawnForced = serverPlayer.isRespawnForced();

            // Try to get respawn position if its set
            Optional<Vec3> optionalRespawnPosition = spawn != null
                    ? Player.findRespawnPositionAndUseSpawnBlock((ServerLevel) level, spawn, respawnAngle, respawnForced, false)
                    : Optional.empty();

            // If we have a bed/respawn anchor, go to it. Otherwise, go to world spawn
            optionalRespawnPosition.ifPresentOrElse(
                    respawnPosition -> serverPlayer.teleportTo(respawnPosition.x, respawnPosition.y, respawnPosition.z),
                    () -> {
                        ServerLevel serverLevel = serverPlayer.getLevel();
                        BlockPos.MutableBlockPos worldSpawn = serverLevel.getSharedSpawnPos().mutable();
                        while(!serverLevel.noCollision(serverPlayer) && worldSpawn.getY() < (double)(serverLevel.getMaxBuildHeight() - 1)) {
                            worldSpawn.move(Direction.UP);
                        }
                        serverPlayer.teleportTo(worldSpawn.getX(), worldSpawn.getY(), worldSpawn.getZ());
                    }
            );

            level.playSound(null, serverPlayer.blockPosition(), SoundEvents.ENDERMAN_TELEPORT, SoundSource.PLAYERS, 1.0f, 1.0f);
            this.experienceCost.pay(serverPlayer);
            if (this.isCracked) {
                itemStack.hurtAndBreak(1, serverPlayer, (player1) -> {
                    player1.broadcastBreakEvent(serverPlayer.getUsedItemHand());
                });
            }
        }
        return itemStack;
    }
}
