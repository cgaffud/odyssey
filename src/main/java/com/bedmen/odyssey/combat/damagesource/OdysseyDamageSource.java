package com.bedmen.odyssey.combat.damagesource;

import com.bedmen.odyssey.entity.projectile.Boomerang;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.EntityDamageSource;
import net.minecraft.world.damagesource.IndirectEntityDamageSource;
import net.minecraft.world.entity.Entity;

import javax.annotation.Nullable;

public class OdysseyDamageSource extends DamageSource implements InvulnerabilityDamageSource {

    public final float invulnerabilityMultiplier;

    public OdysseyDamageSource(String msgId, float invulnerabilityMultiplier) {
        super(msgId);
        this.invulnerabilityMultiplier = invulnerabilityMultiplier;
    }

    public static DamageSource boomerang(Boomerang boomerang, @Nullable Entity entity) {
        return (new IndirectEntityDamageSource("boomerang", boomerang, entity)).setProjectile();
    }

    public static DamageSource withInvulnerabilityMultiplier(DamageSource damageSource, float invulnerabilityMultiplier){
        float originalInvulnerabilityMultiplier = getInvulnerabilityMultiplier(damageSource);
        DamageSource newDamageSource;
        if(damageSource instanceof EntityDamageSource entityDamageSource){
            EntityDamageSource odysseyEntityDamageSource = new OdysseyEntityDamageSource(damageSource.msgId, entityDamageSource.getEntity(), invulnerabilityMultiplier * originalInvulnerabilityMultiplier);
            if(entityDamageSource.isThorns()) odysseyEntityDamageSource = odysseyEntityDamageSource.setThorns();
            newDamageSource = odysseyEntityDamageSource;
        } else {
            newDamageSource = new OdysseyDamageSource(damageSource.msgId, invulnerabilityMultiplier * originalInvulnerabilityMultiplier);
        }
        if(damageSource.isDamageHelmet()) newDamageSource = newDamageSource.damageHelmet();
        if(damageSource.isBypassArmor()) newDamageSource = newDamageSource.bypassArmor();
        if(damageSource.isBypassInvul()) newDamageSource = newDamageSource.bypassInvul();
        if(damageSource.isBypassMagic()) newDamageSource = newDamageSource.bypassMagic();
        if(damageSource.isFire()) newDamageSource = newDamageSource.setIsFire();
        if(damageSource.isProjectile()) newDamageSource = newDamageSource.setProjectile();
        if(damageSource.scalesWithDifficulty()) newDamageSource = newDamageSource.setScalesWithDifficulty();
        if(damageSource.isMagic()) newDamageSource = newDamageSource.setMagic();
        if(damageSource.isExplosion()) newDamageSource = newDamageSource.setExplosion();
        if(damageSource.isFall()) newDamageSource = newDamageSource.setIsFall();
        if(damageSource.isNoAggro()) newDamageSource = newDamageSource.setNoAggro();
        return newDamageSource;
    }

    public static float getInvulnerabilityMultiplier(DamageSource damageSource){
        return damageSource instanceof InvulnerabilityDamageSource invulnerabilityMultiplier ? invulnerabilityMultiplier.getInvulnerabilityMultiplier() : 1.0f;
    }

    public float getInvulnerabilityMultiplier() {
        return this.invulnerabilityMultiplier;
    }
}
