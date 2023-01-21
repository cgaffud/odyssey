package com.bedmen.odyssey.entity.projectile;

import com.bedmen.odyssey.combat.BoomerangType;
import com.bedmen.odyssey.registry.EntityTypeRegistry;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class Boomerang extends ThrownWeapon {
    private static final int BOOMERANG_TURNAROUND_TIME = 10;

    public Boomerang(EntityType<? extends Boomerang> type, Level level) {
        super(type, level);
    }

    public Boomerang(Level level, LivingEntity thrower, ItemStack thrownStackIn, boolean isMultishotClone) {
        super(EntityTypeRegistry.BOOMERANG.get(), level, thrower, thrownStackIn, isMultishotClone);
    }

    public Boomerang(Level level, double x, double y, double z) {
        super(EntityTypeRegistry.BOOMERANG.get(), x, y, z, level);
    }

    protected boolean isDoneDealingDamage(){
        return this.inGroundTime > 0 || this.tickCount > BOOMERANG_TURNAROUND_TIME;
    }

    public void tick() {
        this.setNoGravity(true);
        super.tick();

        Entity owner = this.getOwner();
        float velocity = this.throwableType.getVelocity();
        int tickFrequency = Integer.max((int)(5f/velocity), 2);
        if (!this.level.isClientSide && this.tickCount % tickFrequency == 1) {
            this.level.playSound(null, this, SoundEvents.PLAYER_ATTACK_SWEEP, owner instanceof Player ? SoundSource.PLAYERS : SoundSource.HOSTILE, 1.0f, velocity);
        }
    }

    protected SoundEvent getDefaultHitGroundSoundEvent() {
        return SoundEvents.ARROW_HIT;
    }

    public BoomerangType getBoomerangType(){
        return (BoomerangType)this.throwableType;
    }
}
