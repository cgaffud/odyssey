package com.bedmen.odyssey.entity.monster;

import com.bedmen.odyssey.Odyssey;
import com.bedmen.odyssey.entity.ai.SculkFollowSoundsGoal;
import com.mojang.serialization.Dynamic;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.WrappedGoal;
import net.minecraft.world.entity.monster.Spider;
import net.minecraft.world.entity.monster.warden.AngerManagement;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.DynamicGameEventListener;
import net.minecraft.world.level.gameevent.EntityPositionSource;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.gameevent.GameEventListener;
import net.minecraft.world.level.gameevent.vibrations.VibrationListener;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.function.BiConsumer;

public class SculkSpider extends Spider implements VibrationListener.VibrationListenerConfig, SculkMob{

    // This is required to get vibrations
    private final DynamicGameEventListener<VibrationListener> dynamicGameEventListener;
    // Sorts targets via who triggers the most vibrations
    private AngerManagement angerManagement = new AngerManagement(this::canTargetEntity, Collections.emptyList());
    // Copy of MemoryModuleType.DISTURBANCE_LOCATION from Warden Brain
    public BlockPos sourceLocation = null;

    public SculkSpider(EntityType<? extends Spider> p_33786_, Level p_33787_) {
        super(p_33786_, p_33787_);
        this.dynamicGameEventListener = new DynamicGameEventListener<>(new VibrationListener(new EntityPositionSource(this, this.getEyeHeight()), 16, this, null, 0.0F, 0));
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        // Spider is readonly, but maybe we can rip out these goals before they start?
        this.targetSelector.getAvailableGoals().stream().filter((wrappedGoal) -> {
            return wrappedGoal.getPriority() > 1;
        }).filter(WrappedGoal::isRunning).forEach(WrappedGoal::stop);
        this.targetSelector.getAvailableGoals().removeIf((wrappedGoal) -> {
            return wrappedGoal.getPriority() > 1;
        });
        this.goalSelector.addGoal(5, new SculkFollowSoundsGoal(this, 1.0D));
    }

    // Stops vibrations from being emitted from mob movement
    public boolean dampensVibrations() {
        return true;
    }

    // Hooks for SculkMob Interface
    public AngerManagement getAngerManagement() {return this.angerManagement; }
    public void setAngerManagement(AngerManagement angerManagement) {this.angerManagement = angerManagement;}
    public BlockPos getSourceBlockPos() {return this.sourceLocation;}
    public void setSourceBlockPos(BlockPos blockPos) {this.sourceLocation = blockPos;}

    public void updateDynamicGameEventListener(BiConsumer<DynamicGameEventListener<?>, ServerLevel> p_219413_) {
        Level level = this.level;
        if (level instanceof ServerLevel serverlevel) {
            p_219413_.accept(this.dynamicGameEventListener, serverlevel);
        }
    }

    @Override
    protected void customServerAiStep() {
//        this.targetSelector.removeGoal(this.visionTargetGoal);
        this.setTargetIfAngry();
    }

    public void tick() {
        Level level = this.level;
        if (level instanceof ServerLevel serverlevel) {
            this.dynamicGameEventListener.getListener().tick(serverlevel);
        }
        super.tick();
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Spider.createAttributes().add(Attributes.MAX_HEALTH, 40.0D).add(Attributes.MOVEMENT_SPEED, 0.35D).add(Attributes.ATTACK_DAMAGE, 4D);
    }

    public boolean doHurtTarget(Entity entity) {
        boolean flag = super.doHurtTarget(entity);
        if (flag && entity instanceof LivingEntity) {
            float f = this.level.getCurrentDifficultyAt(this.blockPosition()).getEffectiveDifficulty();
            ((LivingEntity)entity).addEffect(new MobEffectInstance(MobEffects.DARKNESS, 70 * (int)f), this);
        }
        return flag;
    }

    @Override
    public void addAdditionalSaveData(CompoundTag pCompound) {
        super.addAdditionalSaveData(pCompound);
        VibrationListener.codec(this).encodeStart(NbtOps.INSTANCE, this.dynamicGameEventListener.getListener()).resultOrPartial(Odyssey.LOGGER::error).ifPresent((p_219418_) -> {
            pCompound.put("listener", p_219418_);
        });
        this.addSculkSaveData(pCompound);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag pCompound) {
        super.readAdditionalSaveData(pCompound);
        if (pCompound.contains("listener", 10)) {
            VibrationListener.codec(this).parse(new Dynamic<>(NbtOps.INSTANCE, pCompound.getCompound("listener"))).resultOrPartial(Odyssey.LOGGER::error).ifPresent((p_219408_) -> {
                this.dynamicGameEventListener.updateListener(p_219408_, this.level);
            });
        }
        this.readSculkSaveData(pCompound);
    }

    @Override
    public boolean shouldListen(ServerLevel p_223872_, GameEventListener p_223873_, BlockPos p_223874_, GameEvent p_223875_, GameEvent.Context p_223876_) {
        return this.sculkShouldListen(p_223872_,p_223873_,p_223874_,p_223875_,p_223876_);
    }

    @Override
    public void onSignalReceive(ServerLevel p_223865_, GameEventListener p_223866_, BlockPos p_223867_, GameEvent p_223868_, @Nullable Entity p_223869_, @Nullable Entity p_223870_, float p_223871_) {
        this.sculkOnSignalReceive(p_223865_,p_223866_,p_223867_,p_223868_,p_223869_,p_223870_,p_223871_);
    }
}
