package com.bedmen.odyssey.block.entity;

import com.bedmen.odyssey.Odyssey;
import com.bedmen.odyssey.block.SculkBulbBlock;
import com.bedmen.odyssey.registry.BlockEntityTypeRegistry;
import com.bedmen.odyssey.registry.EntityTypeRegistry;
import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Dynamic;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.util.random.SimpleWeightedRandomList;
import net.minecraft.world.Difficulty;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.SculkSensorBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.BlockPositionSource;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.gameevent.GameEventListener;
import net.minecraft.world.level.gameevent.vibrations.VibrationListener;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;

public class SculkBulbBlockEntity extends BlockEntity implements VibrationListener.VibrationListenerConfig {

    private VibrationListener listener;
    private boolean hasActivated;
    private final SimpleWeightedRandomList<List<EntityType>> SPAWNS = SimpleWeightedRandomList.<List<EntityType>>builder()
            .add(ImmutableList.of(EntityTypeRegistry.SCULK_SPIDER.get(), EntityTypeRegistry.SCULK_SPIDER.get(), EntityTypeRegistry.SCULK_SPIDER.get(), EntityTypeRegistry.SCULK_SPIDER.get()), 2)
            .add(ImmutableList.of(EntityTypeRegistry.SCULK_CREEPER.get(), EntityTypeRegistry.SCULK_CREEPER.get(), EntityTypeRegistry.SCULK_CREEPER.get()), 1)
            .add(ImmutableList.of(EntityTypeRegistry.SCULK_SPIDER.get(), EntityTypeRegistry.SCULK_CREEPER.get(), EntityTypeRegistry.SCULK_SPIDER.get()), 2).build();


    public SculkBulbBlockEntity(BlockPos p_155635_, BlockState p_155636_) {
        super(BlockEntityTypeRegistry.SCULK_BULB.get(), p_155635_, p_155636_);
        this.listener = new VibrationListener(new BlockPositionSource(this.worldPosition), ((SculkBulbBlock)p_155636_.getBlock()).getListenerRange(), this, null, 0.0F, 0);
        this.hasActivated = false;
    }

    public void load(CompoundTag p_155649_) {
        super.load(p_155649_);
        if (p_155649_.contains("has_activated"))
            this.hasActivated = p_155649_.getBoolean("has_activated");
        if (p_155649_.contains("listener", 10)) {
            VibrationListener.codec(this).parse(new Dynamic<>(NbtOps.INSTANCE, p_155649_.getCompound("listener"))).resultOrPartial(Odyssey.LOGGER::error).ifPresent((vibrationListener) -> {
                this.listener = vibrationListener;
            });
        }

    }

    protected void saveAdditional(CompoundTag p_187511_) {
        super.saveAdditional(p_187511_);
        p_187511_.putBoolean("has_activated", this.hasActivated);
        VibrationListener.codec(this).encodeStart(NbtOps.INSTANCE, this.listener).resultOrPartial(Odyssey.LOGGER::error).ifPresent((vibtag) -> {
            p_187511_.put("listener", vibtag);
        });
    }

    public VibrationListener getListener() {
        return this.listener;
    }

    public boolean canTriggerAvoidVibration() {
        return true;
    }

    public boolean shouldListen(ServerLevel p_222811_, GameEventListener p_222812_, BlockPos blockPos, GameEvent gameEvent, @Nullable GameEvent.Context p_222815_) {
        return !this.isRemoved() && (!blockPos.equals(this.getBlockPos()) || gameEvent != GameEvent.BLOCK_DESTROY && gameEvent != GameEvent.BLOCK_PLACE) && !this.hasActivated;
    }

    public void onSignalReceive(ServerLevel serverLevel, GameEventListener p_222804_, BlockPos blockPos, GameEvent p_222806_, @Nullable Entity p_222807_, @Nullable Entity p_222808_, float p_222809_) {
        this.hasActivated = true;
        SculkBulbBlock.deactivate(serverLevel, this.worldPosition, this.getBlockState());

        double trialSpawnRangeMax = 3;
        RandomSource randomSource = level.getRandom();
        int difficulty = serverLevel.getDifficulty().getId();
        Optional<List<EntityType>> optionalSpawns = SPAWNS.getRandomValue(randomSource);
        if (optionalSpawns.isPresent() && (serverLevel.getDifficulty() != Difficulty.PEACEFUL))
        {
            List<EntityType> spawns = optionalSpawns.get();
            for (int j = 0; j < spawns.size() - (3-difficulty); j++) {
                double trialSpawnRange = trialSpawnRangeMax;
                double x, y, z;
                for (int i = 0; i < 4; i++) {
                    x = (double) this.worldPosition.getX() + (randomSource.nextDouble() - 0.5) * 2 * trialSpawnRange;
                    y = this.worldPosition.getY();
                    z = (double) this.worldPosition.getZ() + (randomSource.nextDouble() - 0.5) * 2 * trialSpawnRange;
                    if (level.noCollision(spawns.get(j).getAABB(x, y, z))) {
                        Mob mob = (Mob) spawns.get(j).create(serverLevel);
                        if (mob == null)
                            continue;
                        mob.moveTo(x, y, z, 360 * randomSource.nextFloat(), 0);
                        mob.finalizeSpawn(serverLevel, serverLevel.getCurrentDifficultyAt(blockPos), MobSpawnType.TRIGGERED, null , null);
                        serverLevel.addFreshEntityWithPassengers(mob);
                        serverLevel.playSound(null, (double)this.worldPosition.getX() + 0.5D, (double)this.worldPosition.getY() + 0.5D, (double)this.worldPosition.getZ() + 0.5D, SoundEvents.SCULK_CLICKING, SoundSource.BLOCKS, 1.0F, serverLevel.random.nextFloat() * 0.2F + 0.8F);
                        break;
                    }
                    trialSpawnRangeMax -= 0.5;
                }
            }
        }
    }
}
