package com.bedmen.odyssey.mixin;

import com.bedmen.odyssey.entity.IOdysseyLivingEntity;
import com.bedmen.odyssey.registry.EffectRegistry;
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
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class MixinLivingEntity extends Entity implements IOdysseyLivingEntity {
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

    public void incrementGlidingTicks(){
        this.glidingTicks++;
    }

    public void resetGlidingTicks(){
        this.glidingTicks = 0;
    }

    public int getGlidingTicks(){
        return this.glidingTicks;
    }
}