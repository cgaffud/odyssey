package com.bedmen.odyssey.entity.boss;

import com.bedmen.odyssey.registry.EntityTypeRegistry;
import net.minecraft.entity.CreatureAttribute;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.world.Difficulty;
import net.minecraft.world.World;

import java.util.function.Predicate;

public class BossEntity extends MonsterEntity {
    protected static final Predicate<LivingEntity> ENTITY_SELECTOR = (entity) -> {
        return entity.attackable() && !(entity instanceof MonsterEntity);
    };
    protected BossEntity(EntityType<? extends MonsterEntity> p_i48576_1_, World p_i48576_2_) {
        super(p_i48576_1_, p_i48576_2_);
    }

    public void checkDespawn() {
        if (this.level.getDifficulty() == Difficulty.PEACEFUL && this.shouldDespawnInPeaceful()) {
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
}
