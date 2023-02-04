package com.bedmen.odyssey.entity.projectile;

import com.bedmen.odyssey.aspect.object.Aspects;
import com.bedmen.odyssey.combat.BoomerangType;
import com.bedmen.odyssey.registry.EntityTypeRegistry;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class Boomerang extends ThrownWeapon {

    private static final int BOOMERANG_TURNAROUND_TIME = 10;
    private static final float SPIN_RATE = 60.0f;

    private float spinRotationO = 0.0f;
    private float spinRotation = 0.0f;

    public Boomerang(EntityType<? extends Boomerang> type, Level level) {
        super(type, level);
    }

    public Boomerang(Level level, LivingEntity thrower, ItemStack thrownStackIn, boolean isMultishotClone) {
        super(EntityTypeRegistry.BOOMERANG.get(), level, thrower, thrownStackIn, isMultishotClone);
    }

    protected boolean isDoneDealingDamage(){
        return this.inGroundTime > 0 || (this.tickCount > BOOMERANG_TURNAROUND_TIME && this.hasAspect(Aspects.LOYALTY));
    }

    public void tick() {
        this.setNoGravity(this.hasAspect(Aspects.LOYALTY));
        super.tick();

        Entity owner = this.getOwner();
        float velocity = this.throwableType.velocity;
        int tickFrequency = Integer.max((int)(5f/velocity), 2);
        if (!this.level.isClientSide && this.tickCount % tickFrequency == tickFrequency - 1 && isSpinning()) {
            this.level.playSound(null, this, SoundEvents.PLAYER_ATTACK_SWEEP, owner instanceof Player ? SoundSource.PLAYERS : SoundSource.HOSTILE, 1.0f, velocity);
        }
        if(this.level.isClientSide){
            this.spinRotationO = this.spinRotation;
            if(this.isSpinning()){
                this.spinRotation += this.getBoomerangType().velocity * SPIN_RATE;
            }
        }
    }

    public boolean isSpinning(){
        return !this.doneDealingDamage || this.hasAspect(Aspects.LOYALTY);
    }

    public float getSpinRotation(float partialTick){
        return Mth.lerp(partialTick, this.spinRotationO, this.spinRotation);
    }


    protected SoundEvent getDefaultHitGroundSoundEvent() {
        return SoundEvents.ARROW_HIT;
    }

    public BoomerangType getBoomerangType(){
        return (BoomerangType)this.throwableType;
    }
}
