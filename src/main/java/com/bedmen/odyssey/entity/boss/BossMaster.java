package com.bedmen.odyssey.entity.boss;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

import java.util.List;

public abstract class BossMaster extends BossEntity {
    public BossMaster(EntityType<? extends BossEntity> entityType, Level level) {
        super(entityType, level);
    }

    protected abstract List<PuppetEntity> getPuppetList();

    public abstract void removeAndDiscardPuppet(PuppetEntity puppet);

    public abstract void regeneratePuppet(PuppetEntity puppet, int i);

    public void serverTick() {
        super.serverTick();
        List<PuppetEntity> puppetList = getPuppetList();
        for(int i = 0; i < puppetList.size(); i++) {
            PuppetEntity puppet = puppetList.get(i);
            if(puppet.needsDiscardingOrRegeneration()) {
                if(puppet.shouldBeDiscarded())  {
                    this.removeAndDiscardPuppet(puppet);
                } else {
                    this.regeneratePuppet(puppet, i);
                }
            }
        }
    }

    public void disard(){
        for(PuppetEntity puppetEntity : getPuppetList()) {
            puppetEntity.discard();
        }
        super.discard();
    }
}
