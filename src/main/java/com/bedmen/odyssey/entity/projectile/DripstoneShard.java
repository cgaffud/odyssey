package com.bedmen.odyssey.entity.projectile;

import com.bedmen.odyssey.registry.EntityTypeRegistry;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;

public class DripstoneShard extends OdysseyAbstractArrow {

    public DripstoneShard(EntityType<? extends DripstoneShard> entityType, Level level) {
        super(entityType, level);
    }

    public DripstoneShard(Level level, LivingEntity livingEntity) {
        super(EntityTypeRegistry.DRIPSTONE_SHARD.get(), livingEntity, level);
    }

    public DripstoneShard(Level level, double x, double y, double z) {
        super(EntityTypeRegistry.DRIPSTONE_SHARD.get(), x, y, z, level);
    }

    /*
    TODO: not sure how this damage will even matter if you take explosion damage. Need to rework invulnerability
     */
    @Override
    protected double getDamage() {
        return 1;
    }

    @Override
    protected void onFinalPierce() {
        this.discard();
    }

    protected void onHurt(Entity target, boolean hurtSuccessful) {

    }

    @Override
    protected SoundEvent getEntityHitSoundEvent() {
        return SoundEvents.POINTED_DRIPSTONE_HIT;
    }

    protected float getEntityHitVolume(){
        return 2.0f;
    }

    @Override
    protected ItemStack getPickupItem() {
        return ItemStack.EMPTY;
    }

    protected void onHitBlock(BlockHitResult blockHitResult) {
        this.playSound(this.getEntityHitSoundEvent(), this.getEntityHitVolume(), 1.2F / (this.random.nextFloat() * 0.2F + 0.9F));
        this.discard();
    }
}
