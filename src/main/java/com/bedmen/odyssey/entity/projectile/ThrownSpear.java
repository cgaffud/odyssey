package com.bedmen.odyssey.entity.projectile;

import com.bedmen.odyssey.aspect.AspectUtil;
import com.bedmen.odyssey.aspect.object.Aspects;
import com.bedmen.odyssey.combat.SpearType;
import com.bedmen.odyssey.registry.EntityTypeRegistry;
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

    public SpearType getSpearType(){
        return (SpearType)this.throwableType;
    }

    protected void markAsDoneDealingDamage(){
        super.markAsDoneDealingDamage();
        if(this.hasAspect(Aspects.LOYALTY)){
            this.playSound(this.throwableType.soundProfile.returnSound, 10.0F, 1.0F);
        }
    }

    public boolean isFoil(){
        return AspectUtil.hasAddedModifiers(this.getThrownStack());
    }
}
