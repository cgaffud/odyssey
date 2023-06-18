package com.bedmen.odyssey.entity.monster;

import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.EntityDamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.PickaxeItem;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Random;

public interface Encased {

    String STONE_ARMOR_HEALTH_TAG = "StoneArmorHealth";
    BlockState DRIPSTONE_BLOCKSTATE = Blocks.DRIPSTONE_BLOCK.defaultBlockState();
    enum StoneArmorBlockType {
        BYPASS,
        HURTS_ARMOR,
        BLOCKED
    }

    static StoneArmorBlockType damageSourceStoneArmorBlockType(DamageSource damageSource){
        if(damageSource.isBypassArmor()
        || damageSource == DamageSource.LIGHTNING_BOLT
        || damageSource.isFire()
        || damageSource == DamageSource.DRY_OUT
        || damageSource.isMagic()){
            return StoneArmorBlockType.BYPASS;
        }
        if(damageSource.isDamageHelmet()
        || damageSource.isExplosion()
        || isPickaxeAttack(damageSource)){
            return StoneArmorBlockType.HURTS_ARMOR;
        }
        return StoneArmorBlockType.BLOCKED;
    }

    static boolean isPickaxeAttack(DamageSource damageSource){
        if(damageSource instanceof EntityDamageSource entityDamageSource){
            String msgid = entityDamageSource.msgId;
            if(msgid.equals("mob") || msgid.equals("player")){
                Entity entity = entityDamageSource.getEntity();
                if(entity instanceof LivingEntity livingEntity){
                    ItemStack itemStack = livingEntity.getMainHandItem();
                    return itemStack.getItem() instanceof PickaxeItem;
                }
            }
        }
        return false;
    }

    default boolean hurt(LivingEntity livingEntity, DamageSource damageSource, float amount) {
        StoneArmorBlockType stoneArmorBlockType = damageSourceStoneArmorBlockType(damageSource);
        if(stoneArmorBlockType == StoneArmorBlockType.BYPASS || !this.hasStoneArmor()) {
            return this.hurtWithoutStoneArmor(damageSource, amount);
        }
        if (stoneArmorBlockType == StoneArmorBlockType.HURTS_ARMOR){
            return this.hurtWithStoneArmor(livingEntity, damageSource, amount);
        }
        return false;
    }

    default boolean hurtWithStoneArmor(LivingEntity livingEntity, DamageSource damageSource, float amount){
        float armorHealth = this.getStoneArmorHealth();
        if(armorHealth > 0.0f){
            float newArmorHealth = armorHealth - amount;
            boolean armorHealthChanged = newArmorHealth != armorHealth;
            if(!livingEntity.level.isClientSide){
                this.setStoneArmorHealth(newArmorHealth);
                if (armorHealthChanged) {
                    Random random = livingEntity.getRandom();
                    for(int i = 0; i < 8; ++i) {
                        ((ServerLevel)livingEntity.level).sendParticles(new BlockParticleOption(ParticleTypes.BLOCK, Blocks.DRIPSTONE_BLOCK.defaultBlockState()).setPos(livingEntity.blockPosition()), livingEntity.getX() + random.nextFloat()-0.5f, livingEntity.getY() + random.nextFloat()*2, livingEntity.getZ() + random.nextFloat()-0.5f, 2, 0.2D, 0.2D, 0.2D, 0.0D);
                    }
                }
            }
            if (armorHealthChanged && !livingEntity.isSilent()) {
                livingEntity.playSound(SoundEvents.DRIPSTONE_BLOCK_BREAK, 5.0F, 1.0F);
            }
            return false;
        } else {
            return this.hurtWithoutStoneArmor(damageSource, amount);
        }
    }

    boolean hurtWithoutStoneArmor(DamageSource damageSource, float amount);

    default boolean hasStoneArmor(){
        return this.getStoneArmorHealth() > 0;
    }

    float getStoneArmorHealth();

    void setStoneArmorHealth(float newStoneArmorHealth);
}
