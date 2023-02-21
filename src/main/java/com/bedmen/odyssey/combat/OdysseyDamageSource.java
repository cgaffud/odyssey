package com.bedmen.odyssey.combat;

import com.bedmen.odyssey.entity.projectile.Boomerang;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.IndirectEntityDamageSource;
import net.minecraft.world.entity.Entity;

import javax.annotation.Nullable;

public class OdysseyDamageSource extends DamageSource {

    public final float invulnerabilityMultiplier;

    public OdysseyDamageSource(String msgId) {
        this(msgId, 1.0f);
    }

    public OdysseyDamageSource(String msgId, float invulnerabilityMultiplier) {
        super(msgId);
        this.invulnerabilityMultiplier = invulnerabilityMultiplier;
    }

    public static DamageSource boomerang(Boomerang boomerang, @Nullable Entity entity) {
        return (new IndirectEntityDamageSource("boomerang", boomerang, entity)).setProjectile();
    }

    public static DamageSource withInvulnerabilityMultiplier(DamageSource damageSource, float invulnerabilityMultiplier){
        DamageSource odysseyDamageSource = new OdysseyDamageSource(damageSource.msgId, invulnerabilityMultiplier);
        if(damageSource.isDamageHelmet()) odysseyDamageSource = odysseyDamageSource.damageHelmet();
        if(damageSource.isBypassArmor()) odysseyDamageSource = odysseyDamageSource.bypassArmor();
        if(damageSource.isBypassInvul()) odysseyDamageSource = odysseyDamageSource.bypassInvul();
        if(damageSource.isBypassMagic()) odysseyDamageSource = odysseyDamageSource.bypassMagic();
        if(damageSource.isFire()) odysseyDamageSource = odysseyDamageSource.setIsFire();
        if(damageSource.isProjectile()) odysseyDamageSource = odysseyDamageSource.setProjectile();
        if(damageSource.scalesWithDifficulty()) odysseyDamageSource = odysseyDamageSource.setScalesWithDifficulty();
        if(damageSource.isMagic()) odysseyDamageSource = odysseyDamageSource.setMagic();
        if(damageSource.isExplosion()) odysseyDamageSource = odysseyDamageSource.setExplosion();
        if(damageSource.isFall()) odysseyDamageSource = odysseyDamageSource.setIsFall();
        if(damageSource.isNoAggro()) odysseyDamageSource = odysseyDamageSource.setNoAggro();
        return odysseyDamageSource;
    }

    public static float getInvulnerabilityMultiplier(DamageSource damageSource){
        return damageSource instanceof OdysseyDamageSource odysseyDamageSource ? odysseyDamageSource.invulnerabilityMultiplier : 1.0f;
    }
}
