package com.bedmen.odyssey.entity.boss.coven;

import com.bedmen.odyssey.entity.boss.Boss;
import com.bedmen.odyssey.entity.boss.SubEntity;
import com.bedmen.odyssey.entity.boss.mineralLeviathan.MineralLeviathanMaster;
import com.bedmen.odyssey.entity.boss.mineralLeviathan.MineralLeviathanSegment;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.LookControl;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkHooks;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CovenWitch extends Monster implements SubEntity<CovenMaster> {
    private static final EntityDataAccessor<Integer> MASTER_ID_DATA = SynchedEntityData.defineId(CovenWitch.class, EntityDataSerializers.INT);
    protected static final EntityDataAccessor<Float> DATA_WITCH_HEALTH_ID = SynchedEntityData.defineId(CovenWitch.class, EntityDataSerializers.FLOAT);
    protected boolean enraged;

    public CovenWitch(EntityType<? extends CovenWitch> entityType, Level level) {
        super(entityType, level);
        this.setHealth(this.getMaxHealth());
        this.setWitchHealth(this.getMaxHealth());
        this.noCulling = true;
        // TODO: look controller?
        this.enraged = false;
        this.lookControl = new LookControl(this);

    }

    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(MASTER_ID_DATA, -1);
        this.entityData.define(DATA_WITCH_HEALTH_ID, 0.0f);
    }

    public void tick() {
        super.tick();
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

    public void setWitchHealth(float f) {
        this.entityData.set(DATA_WITCH_HEALTH_ID, f);
    }
    public float getWitchHealth() {
        return this.entityData.get(DATA_WITCH_HEALTH_ID);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes().add(Attributes.MOVEMENT_SPEED, 0.25D).add(Attributes.MAX_HEALTH, CovenMaster.MAX_HEALTH / CovenMaster.NUM_WITCHES).add(Attributes.ATTACK_DAMAGE, CovenMaster.DAMAGE * 0.5d);
    }


    public void addAdditionalSaveData(CompoundTag compoundNBT) {
        super.addAdditionalSaveData(compoundNBT);
        compoundNBT.putFloat("WitchHealth", this.getWitchHealth());
    }

    public void readAdditionalSaveData(CompoundTag compoundNBT) {
        super.readAdditionalSaveData(compoundNBT);
        if(compoundNBT.contains("WitchHealth")){
            this.setWitchHealth(compoundNBT.getFloat("WitchHealth"));
        }
    }

    public boolean hurtDirectly(DamageSource damageSource, float amount) {
        return super.hurt(damageSource, amount);
    }

    public boolean hurt(DamageSource damageSource, float amount) {
        // Ignore magic damage. They are witches
        if (this.isInvulnerableTo(damageSource) || damageSource.isMagic())
            return false;

        float witchHealth = this.getWitchHealth();
        if (witchHealth > 0.0f) {
            float newWitchHealth = witchHealth - amount * this.getMaster().map(Boss::getDamageReduction).orElse(1.0f);
            if (newWitchHealth <= 0.0f) {
                this.enraged = true;
                newWitchHealth = 0;
            }
            if(!this.level.isClientSide)
                this.setWitchHealth(newWitchHealth);
        }

        System.out.print(this.getWitchHealth());
        if(this.getMaster().isPresent()) {
            CovenMaster covenMaster = this.getMaster().get();
            return covenMaster.hurt(damageSource, (amount > witchHealth) ? witchHealth : amount);
        }
        return false;
    }

    @Override
    public Packet<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    public void writeSpawnData(FriendlyByteBuf friendlyByteBuf) {
        SubEntity.super.writeSpawnData(friendlyByteBuf);
    }

    public void readSpawnData(FriendlyByteBuf friendlyByteBuf) {
        SubEntity.super.readSpawnData(friendlyByteBuf);
    }

    public Optional<CovenMaster> getMaster() {
        int headId = this.entityData.get(MASTER_ID_DATA);
        Entity entity = this.level.getEntity(headId);
        // instanceof also checks if it is null
        if(entity instanceof CovenMaster covenMaster) {
            return Optional.of(covenMaster);
        }
        return Optional.empty();
    }

    public void setMasterId(int masterId) {
        this.entityData.set(MASTER_ID_DATA, masterId);
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

    void doWhenReturnToMaster() {}

    public boolean save(CompoundTag compoundTag) {
        return false;
    }

}
