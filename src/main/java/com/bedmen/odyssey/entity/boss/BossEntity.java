package com.bedmen.odyssey.entity.boss;

import net.minecraft.entity.CreatureAttribute;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.DamageSource;
import net.minecraft.world.Difficulty;
import net.minecraft.world.World;

import java.util.function.Predicate;

public abstract class BossEntity extends MonsterEntity implements IBossEventEntity {
    private int despawnTimer;
    protected static final Predicate<LivingEntity> ENTITY_SELECTOR = (entity) -> {
        return entity.attackable() && !(entity instanceof MonsterEntity);
    };
    protected BossEntity(EntityType<? extends MonsterEntity> p_i48576_1_, World p_i48576_2_) {
        super(p_i48576_1_, p_i48576_2_);
    }

    public void tick(){
        super.tick();
        if(this.getTarget() == null){
            this.despawnTimer++;
        }
    }

    public void checkDespawn() {
        if ((this.level.getDifficulty() == Difficulty.PEACEFUL && this.shouldDespawnInPeaceful()) || this.despawnTimer > 1200) {
            this.remove();
        } else {
            this.noActionTime = 0;
        }
    }

    public boolean causeFallDamage(float p_225503_1_, float p_225503_2_) {
        return false;
    }

    public CreatureAttribute getMobType() {
        return CreatureAttribute.UNDEFINED;
    }

    public boolean canChangeDimensions() {
        return false;
    }

    protected boolean canRide(Entity p_184228_1_) {
        return false;
    }

    public boolean addEffect(EffectInstance p_195064_1_) {
        return false;
    }

    public boolean hurt(DamageSource damageSource, float amount) {
        amount *= this.getDamageReduction();
        return super.hurt(damageSource, amount);
    }
}
