package com.bedmen.odyssey.potions;

import com.bedmen.odyssey.registry.EffectRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectUtil;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec3;

public class OdysseyEffect extends MobEffect{

    public OdysseyEffect(MobEffectCategory typeIn, int liquidColorIn) {
        super(typeIn, liquidColorIn);
    }

    private int decreaseAirSupply(LivingEntity livingEntity, int prev){
        int i = EnchantmentHelper.getRespiration(livingEntity);
        return i > 0 && livingEntity.getRandom().nextInt(i + 1) > 0 ? prev : prev - 4;
    }

    @Override
    public void applyEffectTick(LivingEntity livingEntity, int amplifier) {
        if (this == EffectRegistry.BLEEDING.get()) {
            livingEntity.hurt(DamageSource.MAGIC, amplifier);
        } if (this == EffectRegistry.DROWNING.get()){
            boolean notCreative = ((livingEntity instanceof Player) && !livingEntity.isInvulnerable()) || !(livingEntity instanceof Player) ;
            if (livingEntity.isAlive() && !livingEntity.canBreatheUnderwater() && !MobEffectUtil.hasWaterBreathing(livingEntity) && notCreative) {
                livingEntity.setAirSupply(decreaseAirSupply(livingEntity,livingEntity.getAirSupply()));
                if (livingEntity.getAirSupply() <= -20) {
                    livingEntity.setAirSupply(0);
                    Vec3 vec3 = livingEntity.getDeltaMovement();

                    for(int i = 0; i < 8; ++i) {
                        double d2 = livingEntity.getRandom().nextDouble() - livingEntity.getRandom().nextDouble();
                        double d3 = livingEntity.getRandom().nextDouble() - livingEntity.getRandom().nextDouble();
                        double d4 = livingEntity.getRandom().nextDouble() - livingEntity.getRandom().nextDouble();
                        livingEntity.level.addParticle(ParticleTypes.BUBBLE, livingEntity.getX() + d2, livingEntity.getY() + d3, livingEntity.getZ() + d4, vec3.x, vec3.y, vec3.z);
                    }
                    livingEntity.hurt(DamageSource.DROWN, 2.0F*(amplifier+1));
                }
            }
        } else {
            super.applyEffectTick(livingEntity, amplifier);
        }

    }

    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        if (this == EffectRegistry.BLEEDING.get()) {
            return true;
        }
        return super.isDurationEffectTick(duration, amplifier);
    }
}