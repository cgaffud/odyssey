package com.bedmen.odyssey.mixin;

import com.bedmen.odyssey.entity.OdysseyLivingEntity;
import com.bedmen.odyssey.registry.EffectRegistry;
import com.bedmen.odyssey.weapon.SmackPush;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.tags.TagKey;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class MixinLivingEntity extends Entity implements OdysseyLivingEntity {
    private int glidingLevel = 0;
    private boolean hasSlowFalling = false;
    private int flightTicks = 0;
    private SmackPush smackPush = new SmackPush();
    private float nextKnockbackModifier = 1.0f;
    public MixinLivingEntity(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }

    @Shadow
    public boolean hasEffect(MobEffect p_21024_) {return false;}

    public void setAirSupply(int amount) {
        super.setAirSupply(Integer.max(-20, amount));
    }

    @Redirect(method = "baseTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;isEyeInFluid(Lnet/minecraft/tags/TagKey;)Z"))
    public boolean onBaseTick$isEyeInFluid(LivingEntity livingEntity, TagKey<Fluid> tagkey){
        return livingEntity.isEyeInFluid(tagkey) || this.hasEffect(EffectRegistry.DROWNING.get());
    }

    @Redirect(method = "baseTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/state/BlockState;is(Lnet/minecraft/world/level/block/Block;)Z"))
    public boolean onBaseTick$is(BlockState blockState, Block block){
        return blockState.is(block) && !this.hasEffect(EffectRegistry.DROWNING.get());
    }

    @Inject(method = "addAdditionalSaveData", at = @At(value = "RETURN"))
    public void onAddAdditionalSaveData(CompoundTag compoundTag, CallbackInfo ci){
        compoundTag.putInt("GlidingLevel", this.glidingLevel);
        compoundTag.putInt("GlidingTicks", this.flightTicks);
    }

    @Inject(method = "readAdditionalSaveData", at = @At(value = "RETURN"))
    public void onReadAdditionalSaveData(CompoundTag compoundTag, CallbackInfo ci){
        this.glidingLevel = compoundTag.getInt("GlidingLevel");
        this.flightTicks = compoundTag.getInt("GlidingTicks");
    }

    public void setFlightLevels(boolean hasSlowFalling, int glidingLevel){
        this.hasSlowFalling = hasSlowFalling;
        this.glidingLevel = glidingLevel;
    }

    public int getGlidingLevel(){
        return this.glidingLevel;
    }

    public boolean hasSlowFalling(){
        return this.hasSlowFalling;
    }

    public void incrementFlightTicks(int i){
        this.flightTicks += i;
    }

    public void decrementFlightTicks(){
        int i = this.hasSlowFalling ? 1 : this.glidingLevel;
        this.flightTicks = Integer.max(0, this.flightTicks - i);
    }

    public int getFlightTicks(){
        return this.flightTicks;
    }

    public int getMaxFlightTicks(){
        if(this.hasSlowFalling){
            return 100;
        }
        return this.glidingLevel * 20;
    }

    public SmackPush getSmackPush(){
        return this.smackPush;
    }

    public void setSmackPush(SmackPush smackPush){
        this.smackPush = smackPush;
    }

    public float getNextKnockbackModifier(){
        return this.nextKnockbackModifier;
    }

    public void setNextKnockbackModifier(float nextKnockbackModifier){
        this.nextKnockbackModifier = nextKnockbackModifier;
    }

    private LivingEntity getLivingEntity(){
        return (LivingEntity)(Object)this;
    }
}