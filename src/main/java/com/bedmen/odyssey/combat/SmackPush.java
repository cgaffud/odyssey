package com.bedmen.odyssey.combat;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;

public class SmackPush {
    public final boolean shouldPush;
    public final float attackStrengthScale;
    public final Player smacker;
    public final Entity target;

    public SmackPush(){
        this.shouldPush = false;
        this.attackStrengthScale = 0.0f;
        this.smacker = null;
        this.target = null;
    }

    public SmackPush(float attackStrengthScale, Player smacker, Entity target){
        this.shouldPush = true;
        this.attackStrengthScale = attackStrengthScale;
        this.smacker = smacker;
        this.target = target;
    }
}
