package com.bedmen.odyssey.items;

import com.bedmen.odyssey.Odyssey;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.Optional;

public class WarpTotem extends Item {

    private boolean isCracked;
    private final String FOIL_TAG = Odyssey.MOD_ID + ":FoilTag";

    public WarpTotem(Properties p_41383_, boolean isCracked) {
        super(p_41383_);
        this.isCracked = isCracked;
    }

    //TODO: this is buggy asf
    @Override
    public void inventoryTick(ItemStack itemStack, Level level, Entity entity, int p_41407_, boolean p_41408_) {
        super.inventoryTick(itemStack, level, entity, p_41407_, p_41408_);
        /** claim: inventoryTick should be triggered serverside w/ dim changes and experience leveling */
        if ((entity instanceof ServerPlayer serverPlayer) && (serverPlayer.experienceLevel >= 5)
        && serverPlayer.getRespawnDimension().equals(level.dimension()))
            itemStack.getOrCreateTag().putBoolean(FOIL_TAG, true);
        /** inventoryTick doesn't get triggered when levels get spent*/
        if ((entity instanceof Player player) && (player.experienceLevel < 5))
            itemStack.getOrCreateTag().putBoolean(FOIL_TAG, false);
    }

    @Override
    public boolean isFoil(ItemStack itemStack) {
        return itemStack.getOrCreateTag().getBoolean(FOIL_TAG);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack itemStack = player.getItemInHand(hand);
        if ((player instanceof ServerPlayer serverPlayer) && (level.dimension().equals(serverPlayer.getRespawnDimension()))
                && (serverPlayer.experienceLevel >= 5)) {

            // ServerPlayer spawn info
            BlockPos spawn = serverPlayer.getRespawnPosition();
            float respawnAngle = serverPlayer.getRespawnAngle();
            boolean respawnForced = serverPlayer.isRespawnForced();

            // Try to get respawn position if its set
            Optional<Vec3> optional = Optional.empty();
            if (level != null && spawn != null)
                optional = Player.findRespawnPositionAndUseSpawnBlock((ServerLevel) level, spawn, respawnAngle, respawnForced, false);

            // If we have a bed/respawn anchor, go to it. Otherwise go to world spawn
            if (optional.isPresent()) {
                Vec3 vec3 = optional.get();
                serverPlayer.teleportTo(vec3.x, vec3.y, vec3.z);
            } else {
                serverPlayer.fudgeSpawnLocation((ServerLevel) level);
            }

            level.playSound((Player)null, serverPlayer.blockPosition(), SoundEvents.ENDERMAN_TELEPORT, SoundSource.PLAYERS, 1.0f, 1.0f);
            level.playSound((Player)null, serverPlayer.blockPosition(), SoundEvents.BEACON_AMBIENT, SoundSource.PLAYERS, 1.0f, 1.0f);

            serverPlayer.giveExperienceLevels(-5);
            if (this.isCracked) {
                itemStack.hurtAndBreak(1, player, (player1) -> {
                    player1.broadcastBreakEvent(player.getUsedItemHand());
                });
                return InteractionResultHolder.consume(itemStack);
            }

            return InteractionResultHolder.success(itemStack);
        }
        return InteractionResultHolder.pass(itemStack);
    }
}
