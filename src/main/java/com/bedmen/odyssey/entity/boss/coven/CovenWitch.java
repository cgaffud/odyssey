package com.bedmen.odyssey.entity.boss.coven;

import com.bedmen.odyssey.entity.boss.SubEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.LookControl;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkHooks;

import java.util.Optional;

public abstract class CovenWitch extends Monster implements SubEntity<CovenMaster> {
    private static final EntityDataAccessor<Integer> DATA_MASTER_ID = SynchedEntityData.defineId(CovenWitch.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> DATA_PHASE = SynchedEntityData.defineId(CovenWitch.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Boolean> DATA_ENRAGED = SynchedEntityData.defineId(CovenWitch.class, EntityDataSerializers.BOOLEAN);

    public enum Phase {
        IDLE,
        CHASING,
        CASTING
    }

    public CovenWitch(EntityType<? extends CovenWitch> entityType, Level level) {
        super(entityType, level);
        this.setHealth(this.getMaxHealth());
        this.noCulling = true;
        // TODO: look controller?
        this.lookControl = new LookControl(this);

    }

    protected SoundEvent getCastingSoundEvent() {
        return SoundEvents.EVOKER_CAST_SPELL;
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_MASTER_ID, -1);
        this.entityData.define(DATA_PHASE, 0);
        this.entityData.define(DATA_ENRAGED, false);
    }


    public void aiStep() {
        if(!this.isNoAi()){
            if(!this.level.isClientSide){
                //Damage
                this.level.getEntitiesOfClass(LivingEntity.class, this.getBoundingBox())
                        .stream().filter(livingEntity -> !(livingEntity instanceof CovenWitch || livingEntity instanceof CovenMaster))
                        .forEach(livingEntity -> livingEntity.hurt(DamageSource.mobAttack(this).setScalesWithDifficulty(), (float)this.getAttributeBaseValue(Attributes.ATTACK_DAMAGE)));
            }
        }
        super.aiStep();
    }

    protected float getDamageAfterMagicAbsorb(DamageSource p_34149_, float p_34150_) {
        p_34150_ = super.getDamageAfterMagicAbsorb(p_34149_, p_34150_);
        if (p_34149_.getEntity() == this) {
            p_34150_ = 0.0F;
        }

        if (p_34149_.isMagic()) {
            p_34150_ *= 0.15F;
        }

        return p_34150_;
    }

    public void setPhase(Phase phase) {
        this.entityData.set(DATA_PHASE, phase.ordinal());
    }
    public Phase getPhase() {
        return Phase.values()[(this.entityData.get(DATA_PHASE))];
    }

    public void setEnraged(boolean enraged) { this.entityData.set(DATA_ENRAGED, enraged);}
    public boolean isEnraged() { return this.entityData.get(DATA_ENRAGED); }

    // Triggered by CovenMaster when this witch reaches 0 health
    // Server-side
    public void becomeEnraged() {
        if(!this.isEnraged()){
            if(this.level.canSeeSky(this.eyeBlockPosition())){
                LightningBolt lightningBolt = EntityType.LIGHTNING_BOLT.create(this.level);
                lightningBolt.moveTo(this.position());
                lightningBolt.setVisualOnly(true);
                this.level.addFreshEntity(lightningBolt);
            }
            this.setEnraged(true);
        }
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes().add(Attributes.MOVEMENT_SPEED, 0.25D).add(Attributes.MAX_HEALTH, 1.0d).add(Attributes.ATTACK_DAMAGE, CovenMaster.DAMAGE * 0.5d);
    }


    public void addAdditionalSaveData(CompoundTag compoundNBT) {
        super.addAdditionalSaveData(compoundNBT);
        compoundNBT.putInt("WitchPhase", this.getPhase().ordinal());
        compoundNBT.putBoolean("WitchEnraged", this.isEnraged());
    }

    public void readAdditionalSaveData(CompoundTag compoundNBT) {
        super.readAdditionalSaveData(compoundNBT);
        if(compoundNBT.contains("WitchPhase"))
            this.setPhase(Phase.values()[compoundNBT.getInt("WitchPhase")]);
        if(compoundNBT.contains("WitchEnraged"))
            this.setEnraged(compoundNBT.getBoolean("WitchEnraged"));
    }

    public boolean hurtDirectly(DamageSource damageSource, float amount) {
        return super.hurt(damageSource, amount);
    }

    public boolean hurt(DamageSource damageSource, float amount) {
        return this.getMaster().map(covenMaster ->
                covenMaster.hurtWitch(damageSource, amount, this)
        ).orElseGet(() ->
                super.hurt(damageSource, amount)
        );
    }

    @Override
    public Packet<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    public Optional<CovenMaster> getMaster() {
        int masterId = this.entityData.get(DATA_MASTER_ID);
        Entity entity = this.level.getEntity(masterId);
        // instanceof also checks if it is null
        if(entity instanceof CovenMaster covenMaster) {
            return Optional.of(covenMaster);
        }
        return Optional.empty();
    }

    public void setMasterId(int masterId) {
        this.entityData.set(DATA_MASTER_ID, masterId);
    }

    public void kill() {
        this.hurtDirectly(DamageSource.OUT_OF_WORLD, Float.MAX_VALUE);
    }

    public void remove(RemovalReason removalReason) {
        if(removalReason == RemovalReason.DISCARDED || removalReason == RemovalReason.KILLED || this.getMaster().isEmpty()) {
            super.remove(removalReason);
        } else {
            this.getMaster().ifPresent(master -> master.handleSubEntity(this));
        }
    }

    @Override
    public void checkDespawn() {
        if (!this.getMaster().isPresent())
            this.discard();
    }

    protected boolean teleportTowards(Entity entity) {
        Vec3 distToMe = new Vec3(this.getX() - entity.getX(), this.getY(0.5D) - entity.getEyeY(), this.getZ() - entity.getZ());
        distToMe = distToMe.normalize();
        double d1 = this.getX() + (this.random.nextDouble() - 0.5D) * 8.0D - distToMe.x * 16.0D;
        // The y variance has been removed
        double d2 = this.getY() - distToMe.y * 16.0D;
        double d3 = this.getZ() + (this.random.nextDouble() - 0.5D) * 8.0D - distToMe.z * 16.0D;
        return this.teleport(d1, d2, d3);
    }

    protected boolean teleport(double randX, double randY, double randZ) {
        BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos(randX, randY, randZ);

        while(blockpos$mutableblockpos.getY() > this.level.getMinBuildHeight() && !this.level.getBlockState(blockpos$mutableblockpos).getMaterial().blocksMotion()) {
            blockpos$mutableblockpos.move(Direction.DOWN);
        }

        BlockState blockstate = this.level.getBlockState(blockpos$mutableblockpos);
        boolean flag = blockstate.getMaterial().blocksMotion();
        boolean flag1 = blockstate.getFluidState().is(FluidTags.WATER);
        if (flag && !flag1) {
            net.minecraftforge.event.entity.EntityTeleportEvent.EnderEntity event = net.minecraftforge.event.ForgeEventFactory.onEnderTeleport(this, randX, randY, randZ);
            if (event.isCanceled()) return false;
            boolean flag2 = this.randomTeleport(event.getTargetX(), event.getTargetY(), event.getTargetZ(), true);
            if (flag2 && !this.isSilent()) {
                this.level.playSound((Player)null, this.xo, this.yo, this.zo, SoundEvents.ENDERMAN_TELEPORT, this.getSoundSource(), 1.0F, 1.0F);
                this.playSound(SoundEvents.ENDERMAN_TELEPORT, 1.0F, 1.0F);
            }

            return flag2;
        } else {
            return false;
        }
    }

    protected void doWhenReturnToMaster() {
        this.setPhase(Phase.IDLE);
    }

    protected boolean isValidTarget(LivingEntity target, CovenMaster covenMaster) { return ((target != null) && (covenMaster != null) && (covenMaster.validTargetPredicate((ServerPlayer) target))); }

    protected float attackTimeMultiplier(int playerNumber) {
        if (playerNumber < 3)
            return 1.75f - 0.25f*(playerNumber);
        else if (playerNumber > 3)
            return Mth.fastInvSqrt(Mth.sqrt(((float )playerNumber)/3));
        else
            return 1.0f;
    }

    public boolean save(CompoundTag compoundTag) {
        return false;
    }

}
