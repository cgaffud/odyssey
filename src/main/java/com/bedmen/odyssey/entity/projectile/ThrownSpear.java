package com.bedmen.odyssey.entity.projectile;

import com.bedmen.odyssey.combat.SpearType;
import com.bedmen.odyssey.registry.EntityTypeRegistry;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class ThrownSpear extends ThrownWeapon {
    public ThrownSpear(EntityType<? extends ThrownSpear> type, Level level) {
        super(type, level);
    }

    public ThrownSpear(Level level, LivingEntity thrower, ItemStack thrownStackIn, boolean isMultishotClone) {
        super(EntityTypeRegistry.THROWN_SPEAR.get(), level, thrower, thrownStackIn, isMultishotClone);
    }

    protected boolean isDoneDealingDamage() {
        return this.inGroundTime > 4;
    }

    protected SoundEvent getDefaultHitGroundSoundEvent() {
        return SoundEvents.ARROW_HIT;
    }

    public SpearType getSpearType(){
        return (SpearType)this.throwableType;
    }
}
