package com.bedmen.odyssey.mixin;

import com.bedmen.odyssey.entity.IOdysseyLivingEntity;
import com.bedmen.odyssey.registry.EffectRegistry;
import com.bedmen.odyssey.util.EnchantmentUtil;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.Tag;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class MixinLivingEntity extends Entity implements IOdysseyLivingEntity {
    private int glidingLevel = 0;
    private int glidingTicks = 0;
    public MixinLivingEntity(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }

    @Shadow
    public boolean hasEffect(MobEffect p_21024_) {return false;}

    public void setAirSupply(int amount) {
        super.setAirSupply(Integer.max(-20, amount));
    }

    @Redirect(method = "baseTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;isEyeInFluid(Lnet/minecraft/tags/Tag;)Z"))
    public boolean onBaseTick$isEyeInFluid(LivingEntity livingEntity, Tag tag){
        return livingEntity.isEyeInFluid(tag) || this.hasEffect(EffectRegistry.DROWNING.get());
    }

    @Redirect(method = "baseTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/state/BlockState;is(Lnet/minecraft/world/level/block/Block;)Z"))
    public boolean onBaseTick$is(BlockState blockState, Block block){
        return blockState.is(block) && !this.hasEffect(EffectRegistry.DROWNING.get());
    }

    @Inject(method = "addAdditionalSaveData", at = @At(value = "RETURN"))
    public void onAddAdditionalSaveData(CompoundTag compoundTag, CallbackInfo ci){
        compoundTag.putInt("GlidingLevel", this.glidingLevel);
        compoundTag.putInt("GlidingTicks", this.glidingTicks);
    }

    @Inject(method = "readAdditionalSaveData", at = @At(value = "RETURN"))
    public void onReadAdditionalSaveData(CompoundTag compoundTag, CallbackInfo ci){
        this.glidingLevel = compoundTag.getInt("GlidingLevel");
        this.glidingTicks = compoundTag.getInt("GlidingTicks");
    }

    public void setGlidingLevel(int i){
        this.glidingLevel = i;
    }

    public int getGlidingLevel(){
        return this.glidingLevel;
    }

    public void incrementGlidingTicks(){
        this.glidingTicks++;
    }

    public void decrementGlidingTicks(){
        this.glidingTicks = Integer.max(0, this.glidingTicks - this.glidingLevel);
    }

    public int getGlidingTicks(){
        return this.glidingTicks;
    }

    public int getMaxGlidingTicks(){
        return this.glidingLevel * 20;
    }

    private LivingEntity getLivingEntity(){
        return (LivingEntity)(Object)this;
    }
}