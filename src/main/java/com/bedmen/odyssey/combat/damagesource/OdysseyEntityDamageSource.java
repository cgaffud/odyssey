package com.bedmen.odyssey.combat.damagesource;

import net.minecraft.world.damagesource.EntityDamageSource;
import net.minecraft.world.entity.Entity;

public class OdysseyEntityDamageSource extends EntityDamageSource implements InvulnerabilityDamageSource {

    public final float invulnerabilityMultiplier;

    public OdysseyEntityDamageSource(String msgId, Entity entity, float invulnerabilityMultiplier) {
        super(msgId, entity);
        this.invulnerabilityMultiplier = invulnerabilityMultiplier;
    }

    public float getInvulnerabilityMultiplier() {
        return this.invulnerabilityMultiplier;
    }
}
