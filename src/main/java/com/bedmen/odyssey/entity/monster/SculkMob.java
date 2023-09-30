package com.bedmen.odyssey.entity.monster;

import com.bedmen.odyssey.Odyssey;
import com.mojang.serialization.Dynamic;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.monster.warden.AngerLevel;
import net.minecraft.world.entity.monster.warden.AngerManagement;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.gameevent.GameEventListener;
import net.minecraft.world.level.gameevent.vibrations.VibrationListener;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.Optional;

public interface SculkMob{

    AngerManagement getAngerManagement();
    void setAngerManagement(AngerManagement angerManagement);
    BlockPos getSourceBlockPos();
    void setSourceBlockPos(BlockPos blockPos);

    default Mob asMob(){
        return (Mob)this;
    }

    default void increaseAngerAt(@javax.annotation.Nullable Entity p_219442_) {
        this.increaseAngerAt(p_219442_, 35);
    }

    default void increaseAngerAt(@javax.annotation.Nullable Entity target, int amount) {
        if (!this.asMob().isNoAi() && this.canTargetEntity(target)) {
            this.getAngerManagement().increaseAnger(target, amount);
        }
    }

    default AngerLevel getAngerLevel() {
        return AngerLevel.byAnger(this.getAngerManagement().getActiveAnger(null));
    }

    default void setTargetIfAngry() {
        if (this.asMob().tickCount % 20 == 0) {
            this.getAngerManagement().tick((ServerLevel)this.asMob().level , this::canTargetEntity);
            if (this.getAngerLevel().isAngry()) {
                this.asMob().setTarget(this.getAngerManagement().getActiveEntity().get());
            }
        }
    }

    @Contract("null->false")
    default boolean canTargetEntity(@javax.annotation.Nullable Entity p_219386_) {
        if (p_219386_ instanceof LivingEntity livingentity) {
            return this.asMob().level == p_219386_.level && EntitySelector.NO_CREATIVE_OR_SPECTATOR.test(p_219386_) && !this.asMob().isAlliedTo(p_219386_) && livingentity.getType() != EntityType.ARMOR_STAND && livingentity.getType() != EntityType.WARDEN && !livingentity.isInvulnerable() && !livingentity.isDeadOrDying() && this.asMob().level.getWorldBorder().isWithinBounds(livingentity.getBoundingBox());
        }

        return false;
    }

    default boolean sculkShouldListen(ServerLevel p_223872_, GameEventListener p_223873_, BlockPos p_223874_, GameEvent p_223875_, GameEvent.Context p_223876_) {
        if (!this.asMob().isNoAi() && !this.asMob().isDeadOrDying()) {
            Entity entity = p_223876_.sourceEntity();
            if (entity instanceof LivingEntity livingEntity) {
                if (!this.canTargetEntity(livingEntity)) {
                    return false;
                }
            }

            return true;
        }
        return false;
    }

    default void sculkOnSignalReceive(ServerLevel p_223865_, GameEventListener p_223866_, BlockPos sourcePos, GameEvent p_223868_, @Nullable Entity source, @Nullable Entity projectile, float p_223871_) {
        if (!this.asMob().isDeadOrDying()) {
            this.asMob().playSound(SoundEvents.SCULK_CLICKING, 5.0F, this.asMob().getVoicePitch());
            // Update anger based on projectile shot
            if (projectile != null) {
                if (this.asMob().closerThan(projectile, 30.0D)) {
                    if (this.canTargetEntity(projectile)) {
                        sourcePos = projectile.blockPosition();
                    }
                    this.increaseAngerAt(projectile, 5);

                }
            } else {
                this.increaseAngerAt(source);
            }

            // Update source position to be position of disturbance - only if not angry
            if (!this.getAngerLevel().isAngry()) {
                Optional<LivingEntity> optional = this.getAngerManagement().getActiveEntity();
                if (source != null || optional.isEmpty() || optional.get() == source) {
                    this.setSourceBlockPos(sourcePos);
                }
            }

        }
    }



    default void addSculkSaveData(CompoundTag pCompound) {
        AngerManagement.codec(this::canTargetEntity).encodeStart(NbtOps.INSTANCE, this.getAngerManagement()).resultOrPartial(Odyssey.LOGGER::error).ifPresent((p_219437_) -> {
            pCompound.put("anger", p_219437_);
        });
        pCompound.putIntArray("pos", new int[]{this.getSourceBlockPos().getX(), this.getSourceBlockPos().getY(), this.getSourceBlockPos().getZ()});
    }

    default void readSculkSaveData(CompoundTag pCompound) {
        if (pCompound.contains("anger")) {
            AngerManagement.codec(this::canTargetEntity).parse(new Dynamic<>(NbtOps.INSTANCE, pCompound.get("anger"))).resultOrPartial(Odyssey.LOGGER::error).ifPresent((p_219394_) -> {
                this.setAngerManagement(p_219394_);
            });
        }
        if (pCompound.contains("pos")) {
            int[] rawPos = pCompound.getIntArray("pos");
            this.setSourceBlockPos( new BlockPos(rawPos[0], rawPos[1], rawPos[2]) );
        }

    }

}
